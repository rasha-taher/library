package org.example.BookSerie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSerieRepository extends JpaRepository<BookSerie, Long> {
    List<BookSerie> findByOwnerId(Long ownerId);
}
