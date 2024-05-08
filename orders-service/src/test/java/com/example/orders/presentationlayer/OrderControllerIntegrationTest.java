package com.example.orders.presentationlayer;

import com.example.orders.datalayer.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderControllerIntegrationTest {

    private final String BASE_URI_ORDERS = "api/v1/orders";
    private final String FOUND_ORDER_ID = "223e4567-oooo-12d3-a456-556642440001";
    private final String FOUND_EMPLOYEE_LAST_NAME = "Doe";
    private final String NOT_FOUND_EMPLOYEE_ID = "c3333333-3333-3333-444444444444";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebTestClient webTestClient;

    /*
    @Test
    public void whenGetOrders_thenReturnAllOrders(){

        long sizeDB = orderRepository.count();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!"+ orderRepository.count());

        //act & assert
        webTestClient.get().uri(BASE_URI_ORDERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(OrderResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertEquals(list.size(), sizeDB);
                });
    }

    @Test
    public void whenDeleteClient_thenDeleteClientSuccessfully() {
        // Act
        webTestClient.delete().uri(BASE_URI_ORDERS + "/" + FOUND_ORDER_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        //Assert
        assertFalse(orderRepository.existsOrderByOrderIdentifier_OrderId(FOUND_ORDER_ID));

    }

     */
}
