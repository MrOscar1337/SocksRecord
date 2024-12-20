package ru.tagirov.SocksRecord.Services;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import ru.tagirov.SocksRecord.Models.Socks;
import ru.tagirov.SocksRecord.Repositories.SocksRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocksServiceImpl implements SocksService{
    private final SocksRepository repository;


    @Override
    public void registerIncome(String color, int cottonPercentage, int quantity) {
        log.info("Registering income: color={}, cottonPercentage={}, quantity={}",
                color, cottonPercentage, quantity);
        Socks socks = repository.findByColor(color)
                .stream()
                .filter(s -> s.getCottonPercentage() == cottonPercentage)
                .findFirst()
                .orElse(new Socks());

        socks.setColor(color);
        socks.setCottonPercentage(cottonPercentage);
        socks.setQuantity(socks.getQuantity() + quantity);

        repository.save(socks);
    }

    @Override
    public void registerOutcome(String color, int cottonPercentage, int quantity) {
        log.info("Registering outcome: color={}, cottonPercentage={}, quantity={}",
                color, cottonPercentage, quantity);
        Socks socks = repository.findByColor(color)
                .stream()
                .filter(s -> s.getCottonPercentage() == cottonPercentage)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not enough socks available"));

        if (socks.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough socks available");
        }

        socks.setQuantity(socks.getQuantity() - quantity);

        repository.save(socks);
    }

    @Override
    public List<Socks> getSocks(String color, int minCotton, int maxCotton) {
        return repository.findByCottonPercentage(minCotton, maxCotton);
    }

    @Override
    public void updateSocks(int id, String color, Integer cottonPercentage, Integer quantity) {
        Socks socks = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Socks with id " + id + " not found"));

        if (color != null) {
            socks.setColor(color);
        }
        if (cottonPercentage != null) {
            if (cottonPercentage < 0 || cottonPercentage > 100) {
                throw new IllegalArgumentException("Cotton percentage must be between 0 and 100");
            }
            socks.setCottonPercentage(cottonPercentage);
        }
        if (quantity != null) {
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
            socks.setQuantity(quantity);
        }

        repository.save(socks);
    }

    @Override
    public void processBatchFile(MultipartFile file) {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропускаем заголовок

                String color = row.getCell(0).getStringCellValue();
                int cottonPercentage = (int) row.getCell(1).getNumericCellValue();
                int quantity = (int) row.getCell(2).getNumericCellValue();

                registerIncome(color, cottonPercentage, quantity);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to process the file: " + e.getMessage());
        }
    }
}
