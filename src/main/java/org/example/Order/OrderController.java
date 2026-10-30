package org.example.Order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.User.User;
import org.example.dtos.OrderCreationDTO;
import org.example.dtos.OrderItemDTO;
import org.example.dtos.OrderStatusUpdateDTO;
import org.example.dtos.OrderViewDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public Long createOrder(@Valid @RequestBody OrderCreationDTO dto, @AuthenticationPrincipal User currentUser) {
        return orderService.createOrder(dto, currentUser);
    }

    @PostMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#orderId, principal.id)")
    public OrderViewDTO addItemToOrder(@PathVariable Long orderId, @Valid @RequestBody OrderItemDTO dto) {
        return orderService.addItemToOrder(orderId, dto);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#orderId, principal.id)")
    public OrderViewDTO removeItemFromOrder(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.removeItemFromOrder(orderId, itemId);
    }

    @PatchMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#orderId, principal.id)")
    public OrderViewDTO updateItemQuantity(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        return orderService.updateItemQuantity(orderId, itemId, quantity);
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderViewDTO updateOrderStatus(@PathVariable Long orderId, @Valid @RequestBody OrderStatusUpdateDTO dto) {
        return orderService.updateOrderStatus(orderId, dto.getStatus());
    }

    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#orderId, principal.id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#id, principal.id)")
    public OrderViewDTO getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('CLIENT')")
    public List<OrderViewDTO> getMyOrders(@AuthenticationPrincipal User currentUser) {
        return orderService.getOrdersByUser(currentUser.getId());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderViewDTO> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderViewDTO> getOrdersByStatus(@PathVariable OrderStatus status) {
        return orderService.getOrdersByStatus(status);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderViewDTO> getAllOrders() {
        return orderService.getAllOrders();
    }
}
