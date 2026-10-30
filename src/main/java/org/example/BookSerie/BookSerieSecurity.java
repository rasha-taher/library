package org.example.BookSerie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("bookSerieSecurity")
public class BookSerieSecurity {
    private final BookSerieRepository bookSerieRepository;

    public boolean isOwner(Long serieId, Long userId) {
        return bookSerieRepository.existsByIdAndOwnerId(serieId, userId);
    }
}
