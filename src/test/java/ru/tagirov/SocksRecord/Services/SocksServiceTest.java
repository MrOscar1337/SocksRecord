package ru.tagirov.SocksRecord.Services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.tagirov.SocksRecord.Models.Socks;
import ru.tagirov.SocksRecord.Repositories.SocksRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SocksServiceTest {

    @Mock
    private SocksRepository socksRepository;

    @InjectMocks
    private SocksService socksService;

    @Test
    void registerIncome_shouldIncreaseQuantity_whenSocksExist() {
        String color = "red";
        int cottonPercentage = 80;
        int quantity = 50;

        Socks existingSocks = new Socks(1, color, cottonPercentage, 100);

        socksService.registerIncome(color, cottonPercentage, quantity);

        Assertions.assertEquals(150, existingSocks.getQuantity());
        Mockito.verify(socksRepository).save(existingSocks);
    }

    @Test
    void registerIncome_shouldCreateNewSocks_whenNoSocksExist() {

        String color = "blue";
        int cottonPercentage = 70;
        int quantity = 30;


        socksService.registerIncome(color, cottonPercentage, quantity);

        Mockito.verify(socksRepository).save(Mockito.argThat(socks ->
                socks.getColor().equals(color) &&
                        socks.getCottonPercentage() == cottonPercentage &&
                        socks.getQuantity() == quantity
        ));
    }

    @Test
    void registerOutcome_shouldDecreaseQuantity_whenEnoughSocksExist() {
        String color = "red";
        int cottonPercentage = 80;
        int quantity = 30;

        Socks existingSocks = new Socks(1, color, cottonPercentage, 100);

        socksService.registerOutcome(color, cottonPercentage, quantity);

        Assertions.assertEquals(70, existingSocks.getQuantity());
        Mockito.verify(socksRepository).save(existingSocks);
    }

    @Test
    void registerOutcome_shouldThrowException_whenNotEnoughSocksExist() {
        String color = "red";
        int cottonPercentage = 80;
        int quantity = 150;

        Socks existingSocks = new Socks(1, color, cottonPercentage, 100);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                socksService.registerOutcome(color, cottonPercentage, quantity)
        );
        Assertions.assertEquals("Not enough socks in stock", exception.getMessage());
    }

    @Test
    void getSocks_shouldReturnFilteredResults() {
        String color = "red";
        int minCotton = 50;
        int maxCotton = 80;

        List<Socks> socksList = List.of(
                new Socks(1, "red", 60, 100),
                new Socks(2, "red", 70, 200)
        );

        List<Socks> result = socksService.getSocks(color, minCotton, maxCotton);

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().allMatch(socks -> socks.getColor().equals(color)));
    }
    @Test
    void updateSocks_shouldUpdateFieldsCorrectly() {
        int id = 1;
        String newColor = "green";
        int newCottonPercentage = 50;
        int newQuantity = 120;

        Socks existingSocks = new Socks(id, "red", 80, 100);
        Mockito.when(socksRepository.findById(id)).thenReturn(Optional.of(existingSocks));

        socksService.updateSocks(id, newColor, newCottonPercentage, newQuantity);

        Assertions.assertEquals(newColor, existingSocks.getColor());
        Assertions.assertEquals(newCottonPercentage, existingSocks.getCottonPercentage());
        Assertions.assertEquals(newQuantity, existingSocks.getQuantity());
        Mockito.verify(socksRepository).save(existingSocks);
    }
    @Test
    void processBatchFile_shouldProcessValidFile() throws IOException {
        // Arrange
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        InputStream inputStream = new ByteArrayInputStream((
                "Color,CottonPercentage,Quantity\n" +
                        "red,80,50\n" +
                        "blue,70,30\n"
        ).getBytes());
        Mockito.when(mockFile.getInputStream()).thenReturn(inputStream);

        socksService.processBatchFile(mockFile);
        Mockito.verify(socksRepository, Mockito.times(2)).save(Mockito.any(Socks.class));
    }
}
