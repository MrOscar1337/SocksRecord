package ru.tagirov.SocksRecord.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tagirov.SocksRecord.Models.Socks;
import ru.tagirov.SocksRecord.Services.SocksServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
public class SocksController {
    private final SocksServiceImpl service;

    @PostMapping("/income")
    public ResponseEntity<Void> registerIncome(
            @RequestParam String color,
            @RequestParam int cottonPercentage,
            @RequestParam int quantity) {
        service.registerIncome(color, cottonPercentage, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/outcome")
    public ResponseEntity<Void> registerOutcome(
            @RequestParam String color,
            @RequestParam int cottonPercentage,
            @RequestParam int quantity) {
        service.registerOutcome(color, cottonPercentage, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Socks>> getSocks(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer minCotton,
            @RequestParam(required = false) Integer maxCotton) {
        return ResponseEntity.ok(service.getSocks(color, minCotton != null ? minCotton : 0, maxCotton != null ? maxCotton : 100));
    }
}
