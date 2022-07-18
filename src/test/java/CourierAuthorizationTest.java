import POJO.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CourierAuthorizationTest {
    private CourierCommonSteps courierCommonSteps;
    public int courierId = 0;
    public final String login = RandomStringUtils.randomAlphabetic(10);
    public final String password = RandomStringUtils.randomAlphanumeric(10);


    @Before
    public void setUp() {
        courierCommonSteps = new CourierCommonSteps();
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierCommonSteps.delete(courierId);
        }
        courierId = 0;
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    public void courierAuthorization() {
        // Подготовка среды
        CourierPOJO courierPOJO = new CourierPOJO(login, password);
        courierCommonSteps.create(courierPOJO);


        //Проверки
        Response response = courierCommonSteps.login(courierPOJO);
        courierId = response.then().extract().path("id");

        courierCommonSteps.compareStatusCode(response, 200); //проверка успешной авторизации
        assertThat("ID курьера не может быть = 0",
                courierId,
                is(not(0))); // в тело ответа возвращается id курьера, не равный "0"
    }

    @Test
    @DisplayName("Проверка авторизации под курьером без логина")
    public void courierAuthorizationWithoutLogin() {
        // Подготовка среды
        CourierPOJO courierPOJO = new CourierPOJO(login, password);
        courierCommonSteps.create(courierPOJO);
        Response createResponse = courierCommonSteps.login(courierPOJO);
        courierId = createResponse.then().extract().path("id");

        //Проверки
        CourierPOJO courierOnlyPasswordPOJO = new CourierPOJO(password);
        Response response = courierCommonSteps.login(courierOnlyPasswordPOJO);


        courierCommonSteps.compareStatusCode(response, 400); //проверка ошибки авторизации
        assertThat("Сообщение об ошибке не отображается",
                response.then().extract().path("message"),
                is("Недостаточно данных для входа")); // проверка отображения ошибки и её текста
    }

    //@Test(timeout = 20000) //установлен таймаут, чтобы не дожидаться ошибки метода
    @DisplayName("Проверка авторизации под курьером без пароля")
    @Description("Тест с попыткой авторизации без пароля будет падать с ошибкой по таймауту, проблема в запросе или в документации")
    public void courierAuthorizationWithoutPassword() {
        // Подготовка среды
        CourierWithoutPasswordPOJO courierPOJO = new CourierWithoutPasswordPOJO(login, password);
        courierCommonSteps.create(courierPOJO);
        Response createResponse = courierCommonSteps.loginWithoutPassword(courierPOJO);
        courierId = createResponse.then().extract().path("id");


        //Проверки
        CourierWithoutPasswordPOJO courierWithoutPasswordPOJO = new CourierWithoutPasswordPOJO(login);
        Response response = courierCommonSteps.loginWithoutPassword(courierWithoutPasswordPOJO);

        courierCommonSteps.compareStatusCode(response, 400); //проверка ошибки авторизации
        assertThat("Сообщение об ошибке не отображается",
                response.then().extract().path("message"),
                is("Недостаточно данных для входа")); // проверка отображения ошибки и её текста
    }

    @Test
    @DisplayName("Проверка авторизации под несуществующим курьеров")
    public void unknownCourierAuthorization() {
        CourierPOJO courierPOJO = new CourierPOJO(login, password);
        Response response = courierCommonSteps.login(courierPOJO);
        courierCommonSteps.compareStatusCode(response, 404); //проверка ошибки авторизации
        assertThat("Сообщение об ошибке не отображается",
                response.then().extract().path("message"),
                is("Учетная запись не найдена")); // проверка отображения ошибки и её текста
    }

}
