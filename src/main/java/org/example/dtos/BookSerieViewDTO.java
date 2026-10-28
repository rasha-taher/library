package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.BookSerie.BookSerie;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSerieViewDTO {
    private Long id;
    private String name;
    private Long ownerId;
    private String ownerName;

    public BookSerieViewDTO(BookSerie bookSerie) {
        this.id = bookSerie.getId();
        this.name = bookSerie.getName();
        this.ownerId = bookSerie.getOwner().getId();
        this.ownerName = bookSerie.getOwner().getFirstName() + " " + bookSerie.getOwner().getLastName();
    }
}
