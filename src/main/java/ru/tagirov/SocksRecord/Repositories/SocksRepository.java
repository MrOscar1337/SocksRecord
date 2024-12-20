package ru.tagirov.SocksRecord.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tagirov.SocksRecord.Models.Socks;

import java.util.List;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Integer> {
    List<Socks> findByColor(String color);

    @Query("SELECT s FROM Socks s WHERE s.cottonPercentage >= :min AND s.cottonPercentage <= :max")
    List<Socks> findByCottonPercentage(int min, int max);
}
