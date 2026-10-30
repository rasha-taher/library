package org.example.Book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("bookSecurity")
public class BookSecurity {
    private final BookRepository bookRepository;

    public boolean isOwner(Long bookId, Long userId) {
        return bookRepository.existsByIdAndOwnerId(bookId, userId);
    }
}
