import POJO.OrderCreatePOJO;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;



@RunWith(Parameterized.class)
public class OrderCreationTest {
    private OrderCommonSteps orderCommonSteps;
    private final OrderCreatePOJO orderCreatePOJO;

    public OrderCreationTest(
            String firstName,
            String lastName,
            String address,
            Integer metroStation,
            String phone,
            Integer rentTime,
            String deliveryDate,
            String comment,
            List<String> color
    ) {
        this.orderCreatePOJO = new OrderCreatePOJO(
                firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color
        );
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][]{
                {
                        "Naruto",
                        "Uchiha",
                        "Konoha, 142 apt.",
                        4,
                        "+7 800 355 35 35",
                        5,
                        "2022-09-09",
                        "Saske, come back to Konoha",
                        List.of(
                                "BLACK",
                                "GREY"
                        )
                },
                {
                        "Naruto",
                        "Uchiha",
                        "Konoha, 142 apt.",
                        4,
                        "+7 800 355 35 35",
                        5,
                        "2022-09-09",
                        "Saske, come back to Konoha",
                        List.of(
                                "BLACK"
                        )
                },
                {
                        "Naruto",
                        "Uchiha",
                        "Konoha, 142 apt.",
                        4,
                        "+7 800 355 35 35",
                        5,
                        "2022-09-09",
                        "Saske, come back to Konoha",
                        List.of(
                                "GREY"
                        )
                },
                {
                        "Naruto",
                        "Uchiha",
                        "Konoha, 142 apt.",
                        4,
                        "+7 800 355 35 35",
                        5,
                        "2022-09-09",
                        "Saske, come back to Konoha",
                        null
                }
        };
    }

    @Before
    public void setUp() {
        orderCommonSteps = new OrderCommonSteps();
    }

    @Test
    @DisplayName("Проверка создания заказа с различными вариантами цвета")
    public void successfulOrderCreationWithVariousColourTest() {
        Response response = orderCommonSteps.createOrder(orderCreatePOJO);

        response.then().statusCode(201); // Проверка успешного создания заказа

        int track = response.then().extract().path("track");
        assertThat("Номер заказа не отображается",
                track,
                notNullValue()); // Проверка, что поле track содержится в ответе

        // Удаление заказа
        orderCommonSteps.deleteOrder(track);
    }
}
