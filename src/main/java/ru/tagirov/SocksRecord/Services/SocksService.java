package ru.tagirov.SocksRecord.Services;

import org.springframework.stereotype.Service;
import ru.tagirov.SocksRecord.Models.Socks;

import java.util.List;

@Service
public interface SocksService {
    void registerIncome(String color, int cottonPercentage, int quantity);
    void registerOutcome(String color, int cottonPercentage, int quantity);
    List<Socks> getSocks(String color, int minCotton, int maxCotton);
}
