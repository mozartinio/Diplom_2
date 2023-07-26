package ordertest;

import constants.ApiEndpoints;
import ingredients.Ingredient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserRandomData;
import user.UserSteps;

import static ingredients.IngredientRequest.getIngredientFromArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static user.UserRandomData.faker;


public class OrderReceivingTest {

    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private Response response;
    private String accessToken;
    private Ingredient validIngredient;

    @Before
    public void setUp() {
        RestAssured.baseURI = ApiEndpoints.BASE_URL;
        validIngredient = getIngredientFromArray();
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersWithAuthorizedUserTest() {
        User user = UserRandomData.createNewRandomUser();
        Order order = new Order(validIngredient);
        response = userSteps.userCreate(user);
        accessToken = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithToken(order, accessToken);
        response = orderSteps.getUserOrders(accessToken);
        response.then()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrdersWithUnauthorizedUserTest() {
        Order order = new Order(validIngredient);
        response = orderSteps.createOrderWithToken(order, String.valueOf(faker.random().hashCode()));
        response = orderSteps.getUserOrders(String.valueOf(faker.random().hashCode()));
        response.then()
                .body("message", equalTo("You should be authorised"))
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