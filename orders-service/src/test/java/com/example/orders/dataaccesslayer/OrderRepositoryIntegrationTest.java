package com.example.orders.dataaccesslayer;

import com.example.orders.datalayer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class OrderRepositoryIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUpDb() { orderRepository.deleteAll(); }

    @Test
    public void whenOrderExists_ReturnOrderByOrderId(){
        // Initialize order
        Order order = new Order(
                new OrderIdentifier(),
                new ProductIdentifier(),
                new CustomerIdentifier(), // Last Name
                new EmployeeIdentifier(), // First Name
                DeliveryStatus.PENDING,
                new BigDecimal("20.00"),
                new BigDecimal("39.99")
        );

        orderRepository.save(order);

        //act
        Order orderFound = orderRepository.findByOrderIdentifier_OrderId(order.getOrderIdentifier().getOrderId());

        //assert
        assertNotNull(orderFound);
        assertEquals(order, orderFound);

    }
}
