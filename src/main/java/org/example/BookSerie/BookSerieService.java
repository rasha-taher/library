package org.example.BookSerie;

import lombok.RequiredArgsConstructor;
import org.example.User.User;
import org.example.User.UserService;
import org.example.dtos.BookSerieCreationDTO;
import org.example.dtos.BookSerieUpdateDTO;
import org.example.dtos.BookSerieViewDTO;
import org.example.utils.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSerieService {
    private final BookSerieRepository bookSerieRepository;

    public Long createBookSerie(BookSerieCreationDTO dto, User user) {

        BookSerie bookSerie = new BookSerie();
        bookSerie.setName(dto.getName());
        bookSerie.setOwner(user);

        return bookSerieRepository.save(bookSerie).getId();
    }

    public BookSerieViewDTO updateBookSerie(Long id, BookSerieUpdateDTO dto) {
        BookSerie bookSerie = findById(id);
        bookSerie.setName(dto.getName());
        return new BookSerieViewDTO(bookSerieRepository.save(bookSerie));
    }

    public BookSerie findById(Long id) {
        return bookSerieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book serie not found"));
    }

    public BookSerieViewDTO getBookSerie(Long id) {
        return new BookSerieViewDTO(findById(id));
    }

    public List<BookSerieViewDTO> getBookSeriesByOwner(Long ownerId) {
        return bookSerieRepository.findByOwnerId(ownerId)
                .stream()
                .map(BookSerieViewDTO::new)
                .toList();
    }

    public List<BookSerieViewDTO> getAllBookSeries() {
        return bookSerieRepository.findAll()
                .stream()
                .map(BookSerieViewDTO::new)
                .toList();
    }
}
