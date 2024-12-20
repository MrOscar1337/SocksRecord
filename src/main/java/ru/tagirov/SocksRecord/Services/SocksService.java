package ru.tagirov.SocksRecord.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.tagirov.SocksRecord.Models.Socks;

import java.util.List;

@Service
public interface SocksService {
    void registerIncome(String color, int cottonPercentage, int quantity);
    void registerOutcome(String color, int cottonPercentage, int quantity);
    List<Socks> getSocks(String color, int minCotton, int maxCotton);
    void updateSocks(int id, String color, Integer cottonPercentage, Integer quantity);
    void processBatchFile(MultipartFile file);
}
