package org.ean.techlogistics.service;

import lombok.RequiredArgsConstructor;
import org.ean.techlogistics.dto.OrderDTO;
import org.ean.techlogistics.entity.*;
import org.ean.techlogistics.repository.OrderRepository;
import org.ean.techlogistics.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Order createOrder(User client, String address) {
        Order order = new Order();
        order.setCode("ORD-" + System.currentTimeMillis());
        order.setAddress(address);
        order.setStatus(OrderStatus.CREADA);
        order.setClient(client);
        return orderRepository.save(order);
    }

    @Transactional
    public Order confirmOrder(User client, Long orderId, List<OrderItem> items) {
        Order order = getOrder(orderId);
        if(!Objects.equals(order.getClient().getId(), client.getId())) {
            throw new IllegalArgumentException("El cliente dado no es dueño de la orden");
        }
        if(!order.getStatus().equals(OrderStatus.CREADA)) {
            throw new IllegalArgumentException("La orden no puede ser confirmada ya que su estado no es el esperado");
        }
        order.setStatus(OrderStatus.CONFIRMADA);
        order.setConfirmationDate(LocalDateTime.now());
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow();
            if (product.getStock() < item.getQty()) {
                throw new IllegalArgumentException("Stock insuficiente para " + product.getName());
            }
            product.setStock(product.getStock() - item.getQty());
            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
            order.addItem(item);
        }
        return orderRepository.save(order);
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public Order getOrderByCode(String code) {
        return orderRepository.findByCode(code).orElseThrow();
    }

    public List<Order> getOrdersByClient(Long clientId) {
        return orderRepository.findByClientId(clientId);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Transactional
    public Order cancelOrderAsClient(Long orderId, User client) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (!order.getClient().getId().equals(client.getId())
            && !client.getRole().equals(UserRole.ADMIN)) {
            throw new SecurityException("No puedes cancelar una orden de otro usuario");
        }
        if (order.getStatus() != OrderStatus.CREADA) {
            throw new IllegalStateException("Solo se pueden cancelar órdenes en estado CREADA");
        }
        order.setStatus(OrderStatus.CANCELADA);
        return order;
    }
}
