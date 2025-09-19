package org.ean.techlogistics.controller;

import lombok.RequiredArgsConstructor;
import org.ean.techlogistics.dto.DeliveryDTO;
import org.ean.techlogistics.dto.OrderDTO;
import org.ean.techlogistics.entity.Delivery;
import org.ean.techlogistics.entity.User;
import org.ean.techlogistics.entity.UserRole;
import org.ean.techlogistics.mapper.EntityMapper;
import org.ean.techlogistics.service.DeliveryService;
import org.ean.techlogistics.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final UserService userService;

    @PostMapping("/assign")
    public ResponseEntity<DeliveryDTO> assign(@RequestParam Long orderId, @RequestParam Long courierId) {
        User courier = userService.getAllUsers().stream()
                .filter(u -> u.getId().equals(courierId))
                .findFirst()
                .orElseThrow();
        if(!courier.getRole().equals(UserRole.TRANSPORTISTA)) {
            throw new IllegalArgumentException("El usuario no tiene un rol de transportista");
        }
        Delivery delivery = deliveryService.assignDelivery(orderId, courier);
        return ResponseEntity.ok(EntityMapper.toDeliveryDTO(delivery));
    }

    @PostMapping("/{id}/delivered")
    public ResponseEntity<DeliveryDTO> markAsDelivered(@PathVariable Long id) {
        Delivery delivery = deliveryService.markAsDelivered(id);
        return ResponseEntity.ok(EntityMapper.toDeliveryDTO(delivery));
    }

    @GetMapping("/orders/{courierId}")
    public List<OrderDTO> getCourierOrders(@PathVariable Long courierId) {
        return deliveryService.getOrdersAssignedToCourier(courierId).stream()
                .map(EntityMapper::toOrderDTO)
                .toList();
    }
}
