package org.example.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByOwnerId(Long ownerId);

    List<Book> findByBookSerieId(Long bookSerieId);

    Boolean existsByIdAndOwnerId(Long id, Long ownerId);
}
