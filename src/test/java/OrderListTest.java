import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {


    @Test
    @DisplayName("Получение списка заказов")
    public void getOrderListTest() {
        OrderCommonSteps orderCommonSteps = new OrderCommonSteps();
        Response response = orderCommonSteps.getAvailableOrders();
        response.then().assertThat().statusCode(200)
                .and()
                .body("orders", notNullValue()); // Проверка, что метод работает и в тело ответа возвращается список
    }
}
