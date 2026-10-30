package org.example.Order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("orderSecurity")
public class OrderSecurity {
    private final OrderRepository orderRepository;

    public boolean isOwner(Long orderId, Long userId) {
        return orderRepository.existsByIdAndUserId(orderId, userId);
    }
}
