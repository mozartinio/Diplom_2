package usertest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserRandomData;
import user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static user.UserRandomData.faker;

public class UserChangingDataTest {
    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String accessToken;


    @Before
    public void setUp() {
        user = UserRandomData.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Создание пользователя, изменение логина и почты, проверка кода ответа 200")
    public void userDataChangingTestWithAuthorizationTest() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.userProfileChanging(user, accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    @Description("Создание пользователя, изменение пароля, проверка кода ответа 200")
    public void userPasswordChangingTestWithAuthorizationTest() {
        user.setPassword(faker.internet().password());
        response = userSteps.userProfileChanging(user, accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    @Description("Создание пользователя, изменение пароля, проверка кода ответа 401")
    public void userPasswordChangingTestWithoutAuthorizationTest() {
        user.setPassword(faker.internet().password());
        response = userSteps.userProfileChanging(user, "");
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Создание пользователя, изменение логина и почты, проверка кода ответа 401")
    public void userDataChangingTestWithoutAuthorizationTest() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.userProfileChanging(user, "");
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }
}