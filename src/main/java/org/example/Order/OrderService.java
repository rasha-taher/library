package org.example.Order;

import lombok.RequiredArgsConstructor;
import org.example.Book.Book;
import org.example.Book.BookService;
import org.example.User.User;
import org.example.dtos.*;
import org.example.utils.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BookService bookService;

    @Transactional
    public Long createOrder(OrderCreationDTO dto, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        for (OrderItemDTO itemDto : dto.getItems()) {
            Book book = bookService.findById(itemDto.getBookId());
            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(book.getPrice()); // Snapshot price at order time
            order.addItem(item);
        }

        return orderRepository.save(order).getId();
    }

    @Transactional
    public OrderViewDTO addItemToOrder(Long orderId, OrderItemDTO dto) {
        Order order = findById(orderId);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot modify order that is not in PENDING status");
        }

        Book book = bookService.findById(dto.getBookId());
        OrderItem item = new OrderItem();
        item.setBook(book);
        item.setQuantity(dto.getQuantity());
        item.setPrice(book.getPrice());
        order.addItem(item);

        return new OrderViewDTO(orderRepository.save(order));
    }

    @Transactional
    public OrderViewDTO removeItemFromOrder(Long orderId, Long itemId) {
        Order order = findById(orderId);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot modify order that is not in PENDING status");
        }

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));

        order.removeItem(item);
        orderItemRepository.delete(item);

        return new OrderViewDTO(orderRepository.save(order));
    }

    @Transactional
    public OrderViewDTO updateItemQuantity(Long orderId, Long itemId, Integer quantity) {
        Order order = findById(orderId);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot modify order that is not in PENDING status");
        }

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));

        item.setQuantity(quantity);
        order.recalculateTotal();

        return new OrderViewDTO(orderRepository.save(order));
    }

    @Transactional
    public OrderViewDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = findById(orderId);
        order.setStatus(status);
        return new OrderViewDTO(orderRepository.save(order));
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = findById(orderId);

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel order that has been shipped or delivered");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public OrderViewDTO getOrder(Long id) {
        return new OrderViewDTO(findById(id));
    }

    public List<OrderViewDTO> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(OrderViewDTO::new)
                .toList();
    }

    public List<OrderViewDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status)
                .stream()
                .map(OrderViewDTO::new)
                .toList();
    }

    public List<OrderViewDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderViewDTO::new)
                .toList();
    }
}
