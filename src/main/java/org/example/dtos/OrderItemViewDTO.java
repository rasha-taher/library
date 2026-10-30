package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Order.OrderItem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemViewDTO {
    private Long id;
    private Long bookId;
    private String bookName;
    private Integer quantity;
    private Double price;
    private Double subtotal;

    public OrderItemViewDTO(OrderItem item) {
        this.id = item.getId();
        this.bookId = item.getBook().getId();
        this.bookName = item.getBook().getName();
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
        this.subtotal = item.getPrice() * item.getQuantity();
    }
}
