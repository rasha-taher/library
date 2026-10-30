package org.example.Book;

import lombok.RequiredArgsConstructor;
import org.example.BookSerie.BookSerie;
import org.example.BookSerie.BookSerieService;
import org.example.User.User;
import org.example.dtos.BookCreationDTO;
import org.example.dtos.BookUpdateDTO;
import org.example.dtos.BookViewDTO;
import org.example.utils.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookSerieService bookSerieService;

    public Long createBook(BookCreationDTO dto, User user) {
        Book book = new Book();
        book.setName(dto.getName());
        book.setPrice(dto.getPrice());
        book.setPublishedDate(dto.getPublishedDate());
        book.setOwner(user);

        if (dto.getBookSerieId() != null) {
            BookSerie bookSerie = bookSerieService.findById(dto.getBookSerieId());
            book.setBookSerie(bookSerie);
        }

        return bookRepository.save(book).getId();
    }

    public BookViewDTO updateBook(Long id, BookUpdateDTO dto) {
        Book book = findById(id);
        book.setName(dto.getName());
        book.setPrice(dto.getPrice());
        book.setPublishedDate(dto.getPublishedDate());

        if (dto.getBookSerieId() != null) {
            BookSerie bookSerie = bookSerieService.findById(dto.getBookSerieId());
            book.setBookSerie(bookSerie);
        } else {
            book.setBookSerie(null);
        }

        return new BookViewDTO(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    public BookViewDTO getBook(Long id) {
        return new BookViewDTO(findById(id));
    }

    public List<BookViewDTO> getBooksByOwner(Long ownerId) {
        return bookRepository.findByOwnerId(ownerId)
                .stream()
                .map(BookViewDTO::new)
                .toList();
    }

    public List<BookViewDTO> getBooksByBookSerie(Long bookSerieId) {
        return bookRepository.findByBookSerieId(bookSerieId)
                .stream()
                .map(BookViewDTO::new)
                .toList();
    }

    public List<BookViewDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookViewDTO::new)
                .toList();
    }
}
