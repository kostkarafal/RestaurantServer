package pl.kostka.restaurant.unit.controller;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kostka.restaurant.controller.OrderController;
import pl.kostka.restaurant.controller.ProductController;
import pl.kostka.restaurant.model.Product;
import pl.kostka.restaurant.model.enums.ProductType;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProductController.class, secure = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductController productController;


    @Test
    public void testGetProduct() throws Exception{

        Mockito.when(
                productController.getProduct()).thenReturn(Collections.emptyList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[]";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testAddProduct() throws Exception{

        Mockito.when(
                productController.addProduct(any(Product.class))).thenReturn(new Product("test","test",ProductType.KEBAB, 44f,1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products").content("{}").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"id\":null,\"name\":\"test\",\"description\":\"test\",\"type\":\"KEBAB\",\"price\":44.0,\"imageId\":1}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testUpdateProduct() throws Exception{

        Mockito.when(
                productController.updateProduct(anyLong(),any(Product.class))).thenReturn(new Product("test","test",ProductType.KEBAB, 44f,1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/1").content("{}").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"id\":null,\"name\":\"test\",\"description\":\"test\",\"type\":\"KEBAB\",\"price\":44.0,\"imageId\":1}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testUpdateProductImage() throws Exception{

        Mockito.when(
                productController.updateProductImage(anyLong(),anyLong())).thenReturn(new Product("test","test",ProductType.KEBAB, 44f,1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/1/image/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"id\":null,\"name\":\"test\",\"description\":\"test\",\"type\":\"KEBAB\",\"price\":44.0,\"imageId\":1}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testDeleteProduct() throws Exception{
        Mockito.when(
                productController.deleteProduct(anyLong())).thenReturn(ResponseEntity.ok().build());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/products/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }
}
