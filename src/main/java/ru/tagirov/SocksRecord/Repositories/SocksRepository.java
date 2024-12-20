package ru.tagirov.SocksRecord.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tagirov.SocksRecord.Models.Socks;

import java.util.List;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Integer> {
    List<Socks> findByColor(String color);

    List<Socks> findByCottonPercentage(int min, int max);
}
