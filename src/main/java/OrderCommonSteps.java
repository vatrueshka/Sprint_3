import POJO.OrderCreatePOJO;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class OrderCommonSteps extends RestAssuredSpecification {

    public static final String ORDER_PATH = "/api/v1/orders";

    //Параметры для запроса списка доступных заказов
    HashMap<String, String> params = new HashMap<String, String>() {{
        put("limit", "10");
        put("page", "0");
    }};



    @Step("Создание заказа.")
    public Response createOrder (OrderCreatePOJO orderCreatePOJO) {
        return given()
                .spec(getBaseSpec())
                .and()
                .body(orderCreatePOJO)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Удаление заказа")
    public Response deleteOrder(int track) {
        return given()
                .spec(getBaseSpec())
                .queryParams("track", track)
                .when()
                .put(ORDER_PATH + "/cancel");
    }

    @Step("Получение списка доступных заказов.")
    public Response getAvailableOrders() {
        return given()
                .spec(getBaseSpec())
                .queryParams(params)
                .when()
                .get(ORDER_PATH);
    }
}
