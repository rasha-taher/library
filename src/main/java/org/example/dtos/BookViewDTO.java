package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Book.Book;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookViewDTO {
    private Long id;
    private String name;
    private Double price;
    private LocalDate publishedDate;
    private Long ownerId;
    private String ownerName;
    private Long bookSerieId;
    private String bookSerieName;

    public BookViewDTO(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.price = book.getPrice();
        this.publishedDate = book.getPublishedDate();
        this.ownerId = book.getOwner().getId();
        this.ownerName = book.getOwner().getFirstName() + " " + book.getOwner().getLastName();
        if (book.getBookSerie() != null) {
            this.bookSerieId = book.getBookSerie().getId();
            this.bookSerieName = book.getBookSerie().getName();
        }
    }
}
