package dev.markgm.microservices.order_service;

import dev.markgm.microservices.order_service.dto.CreateOrderRequest;
import dev.markgm.microservices.order_service.dto.OrderResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        // tomcat server port
        RestAssured.port = port;
    }

    @Test
    void shouldCreateOrder() {
        CreateOrderRequest orderRequest = new CreateOrderRequest("galaxy-s28-titanium",
                new BigDecimal("1199.99"),
                1);

        OrderResponse orderResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/api/v1/orders")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(OrderResponse.class);

        assertThat(orderResponse.orderNumber()).isNotNull();
        assertThat(orderResponse)
                .usingRecursiveComparison()
                .ignoringFields("orderNumber")
                .isEqualTo(orderRequest);
    }
}
