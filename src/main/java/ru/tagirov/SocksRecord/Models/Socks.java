package ru.tagirov.SocksRecord.Models;


import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "socks")
public class Socks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String color;
    private int cottonPercentage;
    private int quantity;
}
