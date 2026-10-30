package org.example.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Order.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateDTO {
    @NotNull(message = "Status is required")
    private OrderStatus status;
}
