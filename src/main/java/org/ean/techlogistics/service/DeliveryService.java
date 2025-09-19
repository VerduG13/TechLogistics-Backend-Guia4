package org.ean.techlogistics.service;

import lombok.RequiredArgsConstructor;
import org.ean.techlogistics.entity.*;
import org.ean.techlogistics.repository.DeliveryRepository;
import org.ean.techlogistics.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Delivery assignDelivery(Long orderId, User courier) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        if (order.getStatus() != OrderStatus.CONFIRMADA) {
            throw new IllegalStateException("Solo se pueden asignar órdenes CONFIRMADAS");
        }

        Delivery delivery = new Delivery();
        delivery.setCourier(courier);
        delivery.setPickedUpDate(LocalDateTime.now());
        delivery.setOrder(order);

        order.setStatus(OrderStatus.EN_CAMINO);
        order.setDelivery(delivery);

        return deliveryRepository.save(delivery);
    }

    public List<Order> getOrdersAssignedToCourier(Long courierId) {
        return deliveryRepository.findByCourierId(courierId).stream()
                .map(Delivery::getOrder)
                .collect(Collectors.toList());
    }

    @Transactional
    public Delivery markAsDelivered(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();
        if (delivery.getOrder().getStatus() != OrderStatus.EN_CAMINO) {
            throw new IllegalStateException("Solo se pueden asignar órdenes EN CAMINO");
        }
        delivery.setDeliveryDate(LocalDateTime.now());

        Order order = delivery.getOrder();
        order.setStatus(OrderStatus.ENTREGADA);

        return delivery;
    }
}
