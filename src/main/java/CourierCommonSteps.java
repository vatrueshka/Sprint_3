import POJO.CourierPOJO;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierCommonSteps extends RestAssuredSpecification {

    public static final String COURIER_PATH = "api/v1/courier/";

    @Step("Создание курьера")
    public static Response create(CourierPOJO courierAuthorizationCreds) {
        return given()
                .spec(getBaseSpec())
                .and()
                .body(courierAuthorizationCreds)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Авторизация под курьером")
    public Response login(CourierPOJO courierLoginCreds) {
        return given()
                .spec(getBaseSpec())
                .and()
                .body(courierLoginCreds)
                .when()
                .post(COURIER_PATH + "login");
    }

    @Step("Авторизация под курьером без пароля")
    public Response loginWithoutPassword(String bodyWithoutPassword) {
        return given()
                .spec(getBaseSpec())
                .and()
                .body(bodyWithoutPassword)
                .when()
                .post(COURIER_PATH + "login");
    }

    @Step("Удаление курьера, используя его id")
    public Response delete(long courierId) {
        return given()
                .spec(getBaseSpec())
                .and()
                .when()
                .delete(COURIER_PATH + courierId);
    }

    @Step("Сравнение кода ответа с ожидаемым")
    static public void compareStatusCode(Response response, int expectedCode) {
        response.then().statusCode(expectedCode);
    }

    @Step("Получение id курьера после авторизации.")
    public int returnCourierId (CourierPOJO courierIdPOJO) {
        return given()
                .spec(getBaseSpec())
                .body(courierIdPOJO)
                .when()
                .post(COURIER_PATH + "login")
                .then()
                .extract()
                .path("id");
    }
}
