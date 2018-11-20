package pl.kostka.restaurant.unit.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kostka.restaurant.controller.OrderController;
import pl.kostka.restaurant.controller.UserController;
import pl.kostka.restaurant.model.*;
import pl.kostka.restaurant.model.dto.Basket;
import pl.kostka.restaurant.model.enums.OrderStatus;
import pl.kostka.restaurant.model.enums.ProductType;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@WebMvcTest(value = OrderController.class, secure = false)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderController orderController;



    Principal mockPrincipal = new Principal() {
        @Override
        public String getName() {
            return "test";
        }
    };


    @Test
    public void getAllordersByUserId() throws Exception {

        Mockito.when(
                orderController.getAllOrdersByUserId(Mockito.any(Principal.class))).thenReturn(Collections.emptyList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/orders")
                .principal(mockPrincipal)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[]";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }

    @Test
    public void makeOrder() throws Exception {
        Mockito.when(
                orderController.makeOrder(any(Basket.class),any(Principal.class))).thenReturn(new Order());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/orders/make-order").contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .principal(mockPrincipal)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

}
