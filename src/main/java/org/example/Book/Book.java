package org.example.Book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.BookSerie.BookSerie;
import org.example.User.User;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name ;

    @ManyToOne
    private User owner;

    @ManyToOne
    @JoinColumn(name = "book_serie")
    private BookSerie bookSerie;

    private Double price;

    @Column(name = "published_date")
    private LocalDate publishedDate;

}
