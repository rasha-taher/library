package org.example.BookSerie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.User.User;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book_series")
public class BookSerie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String name;

    @ManyToOne
    private User owner;

}
