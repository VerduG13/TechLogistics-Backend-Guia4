package org.ean.techlogistics.controller;

import lombok.RequiredArgsConstructor;
import org.ean.techlogistics.dto.ItemDTO;
import org.ean.techlogistics.dto.OrderDTO;
import org.ean.techlogistics.entity.*;
import org.ean.techlogistics.mapper.EntityMapper;
import org.ean.techlogistics.service.OrderService;
import org.ean.techlogistics.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestParam Long clientId,
                                                @RequestParam String address) {
        User client = userService.getAllUsers().stream()
                .filter(u -> u.getId().equals(clientId))
                .findFirst()
                .orElseThrow();
        Order saved = orderService.createOrder(client, address);
        return ResponseEntity.ok(EntityMapper.toOrderDTO(saved));
    }

    @PostMapping("/confirm")
    public ResponseEntity<OrderDTO> confirmOrder(
            @RequestParam Long clientId,
            @RequestParam Long orderId,
            @RequestBody List<ItemDTO> itemsDto) {

        User client = userService.getAllUsers().stream()
                .filter(u -> u.getId().equals(clientId))
                .findFirst()
                .orElseThrow();

        List<OrderItem> items = itemsDto.stream().map(dto -> {
            Product p = new Product();
            p.setId(dto.productId());
            OrderItem oi = new OrderItem();
            oi.setProduct(p);
            oi.setQty(dto.qty());
            return oi;
        }).toList();

        Order saved = orderService.confirmOrder(client, orderId, items);
        return ResponseEntity.ok(EntityMapper.toOrderDTO(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(EntityMapper.toOrderDTO(orderService.getOrder(id)));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<OrderDTO> getOrderByCode(@PathVariable String code) {
        return ResponseEntity.ok(EntityMapper.toOrderDTO(orderService.getOrderByCode(code)));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderDTO>> getOrderByClientId(@PathVariable Long clientId) {
        List<OrderDTO> dtos = orderService.getOrdersByClient(clientId).stream()
                .map(EntityMapper::toOrderDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/confirmed")
    public ResponseEntity<List<OrderDTO>> getConfirmedOrders() {
        List<OrderDTO> dtos = orderService.getOrdersByStatus(OrderStatus.CONFIRMADA).stream()
                .map(EntityMapper::toOrderDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<OrderDTO>> getPendingOrders() {
        List<OrderDTO> dtos = orderService.getOrdersByStatus(OrderStatus.CREADA).stream()
                .map(EntityMapper::toOrderDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancel(@PathVariable Long id, @RequestParam Long clientId) {
        User client = userService.getAllUsers().stream()
                .filter(u -> u.getId().equals(clientId))
                .findFirst()
                .orElseThrow();
        Order canceled = orderService.cancelOrderAsClient(id, client);
        return ResponseEntity.ok(EntityMapper.toOrderDTO(canceled));
    }
}
