package org.ean.techlogistics.mapper;

import org.ean.techlogistics.dto.*;
import org.ean.techlogistics.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class EntityMapper {

    // ---------- USER ----------
    public static UserDTO toUserDTO(User u) {
        if (u == null) return null;
        return new UserDTO(
                u.getId(),
                u.getEmail(),
                u.getRole().name(),
                u.getPhoneNumber(),
                u.getName()
        );
    }

    public static User toUserEntity(UserDTO dto) {
        if (dto == null) return null;
        User u = new User();
        u.setId(dto.id());
        u.setEmail(dto.email());
        u.setRole(UserRole.valueOf(dto.role()));
        u.setPhoneNumber(dto.phoneNumber());
        u.setName(dto.name());
        return u;
    }

    // ---------- PRODUCT ----------
    public static ProductDTO toProductDTO(Product p) {
        if (p == null) return null;
        return new ProductDTO(
                p.getId(),
                p.getCode(),
                p.getName(),
                p.getDescription(),
                p.getStock(),
                p.getPrice()
        );
    }

    public static Product toProductEntity(ProductDTO dto) {
        if (dto == null) return null;
        Product p = new Product();
        p.setId(dto.id());
        p.setCode(dto.code());
        p.setName(dto.name());
        p.setDescription(dto.description());
        p.setStock(dto.stock());
        p.setPrice(dto.price());
        return p;
    }

    // ---------- ORDER ITEM ----------
    public static OrderItemDTO toOrderItemDTO(OrderItem oi) {
        if (oi == null) return null;
        return new OrderItemDTO(
                oi.getId(),
                oi.getProduct() != null ? oi.getProduct().getId() : null,
                oi.getProduct() != null ? oi.getProduct().getCode() : null,
                oi.getProduct() != null ? oi.getProduct().getName() : null,
                oi.getQty(),
                oi.getUnitPrice(),
                oi.getSubtotal()
        );
    }

    public static OrderItem toOrderItemEntity(OrderItemDTO dto, Product product) {
        if (dto == null) return null;
        OrderItem oi = new OrderItem();
        oi.setId(dto.id());
        oi.setProduct(product);
        oi.setQty(dto.qty());
        oi.setUnitPrice(dto.unitPrice() != null ? dto.unitPrice() : product.getPrice());
        return oi;
    }

    // ---------- DELIVERY ----------
    public static DeliveryDTO toDeliveryDTO(Delivery d) {
        if (d == null) return null;
        return new DeliveryDTO(
                d.getId(),
                d.getPickedUpDate(),
                d.getDeliveryDate(),
                d.getCourier() != null ? d.getCourier().getId() : null,
                d.getCourier() != null ? d.getCourier().getEmail() : null
        );
    }

    public static Delivery toDeliveryEntity(DeliveryDTO dto, User courier, Order order) {
        if (dto == null) return null;
        Delivery d = new Delivery();
        d.setId(dto.id());
        d.setPickedUpDate(dto.pickedUpDate());
        d.setDeliveryDate(dto.deliveryDate());
        d.setCourier(courier);
        d.setOrder(order);
        return d;
    }

    // ---------- ORDER ----------
    public static OrderDTO toOrderDTO(Order o) {
        if (o == null) return null;

        List<OrderItemDTO> items = o.getItems().stream()
                .map(EntityMapper::toOrderItemDTO)
                .collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(OrderItemDTO::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderDTO(
                o.getId(),
                o.getCode(),
                o.getAddress(),
                o.getStatus().name(),
                o.getConfirmationDate(),
                toUserDTO(o.getClient()),
                items,
                toDeliveryDTO(o.getDelivery()),
                total
        );
    }

    public static Order toOrderEntity(OrderDTO dto, User client, List<OrderItem> items, Delivery delivery) {
        if (dto == null) return null;
        Order o = new Order();
        o.setId(dto.id());
        o.setCode(dto.code());
        o.setAddress(dto.address());
        o.setStatus(OrderStatus.valueOf(dto.status()));
        o.setConfirmationDate(dto.confirmationDate());
        o.setClient(client);
        o.setItems(items);
        if (delivery != null) {
            o.setDelivery(delivery);
        }
        return o;
    }
}
