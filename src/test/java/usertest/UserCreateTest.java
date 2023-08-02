package usertest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import user.User;
import user.UserRandomData;
import user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreateTest {
    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String accessToken;

    @Test
    @DisplayName("Проверка создания нового пользователя с рандомными валидными данными")
    @Description("Проверяем, что токен не пустой")
    public void createUserTest() {
        user = UserRandomData.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response
                .then().body("accessToken", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверка невозможности создания нового пользователя с уже указанными ранее данными")
    @Description("Проверяем код ответа 403 и текст ошибки о существовании такого пользователя")
    public void createSameUserTest() {
        user = UserRandomData.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .statusCode(200);
        response = userSteps.userCreate(user);
        response.then()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Проверка создания нового пользователя со всеми пустыми полями данных")
    @Description("Проверяем код ответа 403 и текст ошибки об обязательности всех полей")
    public void createNoDataUserTest() {
        user = UserRandomData.createRandomNoDataUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Проверка создания нового пользователя с пустым полем имени")
    @Description("Проверяем код ответа 403 и текст ошибки об обязательности всех полей")
    public void createNoNameUserTest() {
        user = UserRandomData.createRandomNoNameUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Проверка создания нового пользователя с пустым полем почты")
    @Description("Проверяем код ответа 403 и текст ошибки об обязательности всех полей")
    public void createNoEmailUserTest() {
        user = UserRandomData.createRandomNoEmailUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Проверка создания нового пользователя с пустым полем пароля")
    @Description("Проверяем код ответа 403 и текст ошибки об обязательности всех полей")
    public void createNoPasswordUserTest() {
        user = UserRandomData.createRandomNoPasswordUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }
}