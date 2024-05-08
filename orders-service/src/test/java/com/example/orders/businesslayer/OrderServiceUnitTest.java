package com.example.orders.businesslayer;

import com.example.orders.datalayer.OrderRepository;
import com.example.orders.domainclientlayer.EmployeeServiceClientTest;
import com.example.orders.domainclientlayer.customers.CustomerServiceClient;
import com.example.orders.domainclientlayer.employees.EmployeeServiceClient;
import com.example.orders.domainclientlayer.products.ProductServiceClient;
import com.example.orders.mapperlayer.OrderResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude = org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration")
class OrderServiceUnitTest {

    @Autowired
    OrderService orderService;

    @MockBean
    CustomerServiceClient customerServiceClient;

    @MockBean
    EmployeeServiceClient employeeServiceClient;

    @MockBean
    ProductServiceClient productServiceClient;

    @MockBean
    OrderRepository orderRepository;

    @SpyBean
    OrderResponseMapper orderResponseMapper;
}
