package ru.tagirov.SocksRecord.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tagirov.SocksRecord.Models.Socks;
import ru.tagirov.SocksRecord.Services.SocksServiceImpl;

import java.util.List;

@Tag(name = "Контроллер носков", description = "Используется для учета носков")
@RestController
@RequestMapping("/api/socks")
@AllArgsConstructor
public class SocksController {
    private final SocksServiceImpl service;

    @Operation(summary = "Метод для регистрации прихода носков",
            description = "Увеличивает количество носков на складе")
    @PostMapping("/income")
    public ResponseEntity<Void> registerIncome(
            @RequestParam String color,
            @RequestParam int cottonPercentage,
            @RequestParam int quantity) {
        service.registerIncome(color, cottonPercentage, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Метод для регистрации отпуска носков",
            description = "Уменьшает количество носков на складе, если их хватает")
    @PostMapping("/outcome")
    public ResponseEntity<Void> registerOutcome(
            @RequestParam String color,
            @RequestParam int cottonPercentage,
            @RequestParam int quantity) {
        service.registerOutcome(color, cottonPercentage, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Метод для получения общего количества носков с фильтрацией",
            description = "Возвращает список носков, соответствующих критериям")
    @GetMapping
    public ResponseEntity<List<Socks>> getSocks(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer minCotton,
            @RequestParam(required = false) Integer maxCotton) {
        return ResponseEntity.ok(service.getSocks(color, minCotton != null ? minCotton : 0, maxCotton != null ? maxCotton : 100));
    }

    @Operation(summary = "Метод для обновления данных носков",
            description = "Позволяет изменить параметры носков (цвет, процент хлопка, количество)")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSocks(
            @PathVariable Integer id,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer cottonPercentage,
            @RequestParam(required = false) Integer quantity) {
        service.updateSocks(id, color, cottonPercentage, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Метож для загрузки партий носков из Excel или CSV файла",
            description = "Принимает Excel или CSV файл с партиями носков, содержащими цвет, процентное содержание хлопка и количество")
    @PostMapping("/batch")
    public ResponseEntity<Void> uploadBatch(@RequestParam("file") MultipartFile file) {
        try {
            service.processBatchFile(file);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
