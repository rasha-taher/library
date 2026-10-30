package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Order.Order;
import org.example.Order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderViewDTO {
    private Long id;
    private Long userId;
    private String userName;
    private List<OrderItemViewDTO> items;
    private OrderStatus status;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderViewDTO(Order order) {
        this.id = order.getId();
        this.userId = order.getUser().getId();
        this.userName = order.getUser().getFirstName() + " " + order.getUser().getLastName();
        this.items = order.getItems().stream()
                .map(OrderItemViewDTO::new)
                .toList();
        this.status = order.getStatus();
        this.totalAmount = order.getTotalAmount();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
    }
}
