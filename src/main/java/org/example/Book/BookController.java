package org.example.Book;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.User.User;
import org.example.dtos.BookCreationDTO;
import org.example.dtos.BookUpdateDTO;
import org.example.dtos.BookViewDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('AUTHOR')")
    public Long createBook(@Valid @RequestBody BookCreationDTO dto, @AuthenticationPrincipal User currentUser) {
        return bookService.createBook(dto, currentUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @bookSecurity.isOwner(#id, principal.id)")
    public BookViewDTO updateBook(@PathVariable Long id, @Valid @RequestBody BookUpdateDTO dto) {
        return bookService.updateBook(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @bookSecurity.isOwner(#id, principal.id)")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/{id}")
    public BookViewDTO getBook(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @GetMapping("/owner/{ownerId}")
    public List<BookViewDTO> getBooksByOwner(@PathVariable Long ownerId) {
        return bookService.getBooksByOwner(ownerId);
    }

    @GetMapping("/serie/{bookSerieId}")
    public List<BookViewDTO> getBooksByBookSerie(@PathVariable Long bookSerieId) {
        return bookService.getBooksByBookSerie(bookSerieId);
    }

    @GetMapping
    public List<BookViewDTO> getAllBooks() {
        return bookService.getAllBooks();
    }
}
