import POJO.OrderCreatePOJO;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(Parameterized.class)
public class OrderCreationTest {
    Faker faker = new Faker(new Locale("ru"));

    private OrderCommonSteps orderCommonSteps;
    private final OrderCreatePOJO orderCreatePOJO;

    private final String firstName = faker.name().firstName();
    private final String lastName = faker.name().lastName();
    private final String address = faker.address().streetAddress();
    private final Integer metroStation = faker.number().numberBetween(1, 100);
    private final String phone = faker.phoneNumber().phoneNumber();
    private final Integer rentTime = faker.number().numberBetween(1, 8);
    private final String deliveryDate = faker.backToTheFuture().date();
    private final String comment = faker.company().catchPhrase();

    public OrderCreationTest(
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
    public static Object[][] getColor() {
        return new Object[][]{
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("BLACK")},
                {null}
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
