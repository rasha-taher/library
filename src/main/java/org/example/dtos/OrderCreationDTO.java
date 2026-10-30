package org.example.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreationDTO {
    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemDTO> items;
}
