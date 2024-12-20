package ru.tagirov.SocksRecord.Services;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import ru.tagirov.SocksRecord.Models.Socks;
import ru.tagirov.SocksRecord.Repositories.SocksRepository;

@Service
@RequiredArgsConstructor
public class SocksServiceImpl implements SocksService{
    private final SocksRepository repository;


    @Override
    public void registerIncome(String color, int cottonPercentage, int quantity) {
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
}
