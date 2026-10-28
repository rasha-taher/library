package org.example.BookSerie;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dtos.BookSerieCreationDTO;
import org.example.dtos.BookSerieUpdateDTO;
import org.example.dtos.BookSerieViewDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-series")
public class BookSerieController {
    private final BookSerieService bookSerieService;

    @PostMapping
    public Long createBookSerie(@Valid @RequestBody BookSerieCreationDTO dto) {
        return bookSerieService.createBookSerie(dto);
    }

    @PutMapping("/{id}")
    public BookSerieViewDTO updateBookSerie(@PathVariable Long id, @Valid @RequestBody BookSerieUpdateDTO dto) {
        return bookSerieService.updateBookSerie(id, dto);
    }

    @GetMapping("/{id}")
    public BookSerieViewDTO getBookSerie(@PathVariable Long id) {
        return bookSerieService.getBookSerie(id);
    }

    @GetMapping("/owner/{ownerId}")
    public List<BookSerieViewDTO> getBookSeriesByOwner(@PathVariable Long ownerId) {
        return bookSerieService.getBookSeriesByOwner(ownerId);
    }

    @GetMapping
    public List<BookSerieViewDTO> getAllBookSeries() {
        return bookSerieService.getAllBookSeries();
    }
}
