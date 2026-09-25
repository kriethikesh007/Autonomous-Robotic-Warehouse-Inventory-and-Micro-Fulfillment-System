package com.warehouse.wms.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import com.warehouse.wms.order.entity.OrderStatus;
import com.warehouse.wms.order.entity.Order;
import com.warehouse.wms.order.entity.OrderItem;
import com.warehouse.wms.order.exception.OrderNotFoundException;
import com.warehouse.wms.order.repository.OrderRepository;
import com.warehouse.wms.product.entity.Product;
import com.warehouse.wms.product.repository.ProductRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order createOrder(Order order) {

        order.setCreatedAt(LocalDateTime.now());

        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.CREATED);
        }

        for (OrderItem item : order.getItems()) {

            Product product = productRepository.findById(
                    item.getProduct().getId()).orElseThrow(
                            () -> new IllegalArgumentException(
                                    "Product not found with id: "
                                            + item.getProduct().getId()));

            item.setProduct(product);
            item.setOrder(order);
        }

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Order not found with id: " + id));
    }

    @Override
    public Order updateOrder(Long id, Order order) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Order not found with id: " + id));

        existingOrder.setOrderNumber(order.getOrderNumber());
        existingOrder.setStatus(order.getStatus());

        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Order not found with id: " + id));

        orderRepository.delete(existingOrder);
    }
}