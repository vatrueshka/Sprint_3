import POJO.CourierPOJO;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

public class CourierCreationTest {
    private CourierCommonSteps courierCommonSteps;
    public long courierId;
    public final String login = RandomStringUtils.randomAlphabetic(10);
    public final String password = RandomStringUtils.randomAlphanumeric(10);
    public final String firstName = RandomStringUtils.randomAlphabetic(10);


    @Before
    public void setUp() {
        courierCommonSteps = new CourierCommonSteps();
    }

    @After
    public void tearDown() throws NullPointerException {
        try {
            CourierPOJO courierPOJO = new CourierPOJO(login, password);
            courierCommonSteps.login(courierPOJO);
            courierId = courierCommonSteps.returnCourierId(courierPOJO);
            if (courierId != 0) {
                courierCommonSteps.delete(courierId);
            }
        } catch (NullPointerException nullPointerException) {
        } //если курьер не найден, то и удалять ничего не нужно
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void successfulCreationCourierTest() {
        // Подготовка среды
        CourierPOJO courierPOJO = new CourierPOJO(login, password, firstName);
        Response response = courierCommonSteps.create(courierPOJO);

        // Проверки
        courierCommonSteps.compareStatusCode(response, 201); //проверка успешного создания курьера
        assertTrue("Курьер не создан", response.then().extract().path("ok")); // проверка создания курьера
        assertThat("ID курьера не может быть = 0",
                response.then().extract().path("id"), is(not(0))); // в тело ответа возвращается id курьера, не равный "0"
    }

    @Test
    @DisplayName("Проверка невозможности создать курьера с повторяющимися данными")
    public void unsuccessfulCreationDuplicationCourierTest() {
        // Подготовка среды
        CourierPOJO courierPOJO = new CourierPOJO(login, password, firstName);
        courierCommonSteps.create(courierPOJO); // создание первого курьера
        Response response = courierCommonSteps.create(courierPOJO); // создание второго курьера с повторяющимися данными

        //Проверки
        courierCommonSteps.compareStatusCode(response, 409); // проверка ошибки создания курьера
        assertThat("Сообщение об ошибке не отображается",
                response.then().extract().path("message"),
                is("Этот логин уже используется. Попробуйте другой.")); // проверка отображения сообщения об ошибке
    }

    @Test
    @DisplayName("Проверка создания курьера без поля login")
    public void unsuccessfulCreationCourierWithoutLoginTest() {
        // Подготовка среды
        CourierPOJO courierPOJO = new CourierPOJO(password);
        Response response = courierCommonSteps.create(courierPOJO);

        //Проверки
        courierCommonSteps.compareStatusCode(response, 400); // проверка ошибки создания курьера
        assertThat("Сообщение об ошибке не отображается",
                response.then().extract().path("message"),
                is("Недостаточно данных для создания учетной записи")); // проверка отображения сообщения об ошибке
    }

}