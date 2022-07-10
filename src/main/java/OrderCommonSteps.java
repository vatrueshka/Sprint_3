import POJO.OrderCreatePOJO;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderCommonSteps extends RestAssuredSpecification {

    public static final String ORDER_PATH = "/api/v1/orders";



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
                .and()
                .when()
                .put(ORDER_PATH + "/cancel?track=" + track);
    }

    @Step("Получение списка доступных заказов(лимит 10).")
    public Response getAvailableOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH +"?limit=10&page=0");
    }
}
