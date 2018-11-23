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
import pl.kostka.restaurant.controller.AddressController;
import pl.kostka.restaurant.model.Address;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AddressController.class, secure = false)
public class AddressControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressController addressController;


    @Test
    public void testGetAllAddresses() throws Exception{

        Mockito.when(
                addressController.getAdressesByUserId(any(Principal.class))).thenReturn(Collections.emptyList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/addresses")
                .principal(mock(Principal.class))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[]";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }

    @Test
    public void testCreateAdress() throws Exception{

        Mockito.when(
                addressController.createAddress(any(Principal.class), any(Address.class)))
                .thenReturn(new Address("test","test","test","test","test",1d,1d, null));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/addresses").content("{}").contentType(MediaType.APPLICATION_JSON)
                .principal(mock(Principal.class))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"id\":null,\"city\":\"test\",\"street\":\"test\",\"buildingNumber\":\"test\",\"apartmentNumber\":\"test\",\"title\":\"test\"}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testUpdateAdress() throws Exception{

        Mockito.when(
                addressController.updateAddres(any(Principal.class),anyLong(), any(Address.class)))
                .thenReturn(new Address("test","test","test","test","test",1d,1d, null));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/addresses/1").content("{}").contentType(MediaType.APPLICATION_JSON)
                .principal(mock(Principal.class))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"id\":null,\"city\":\"test\",\"street\":\"test\",\"buildingNumber\":\"test\",\"apartmentNumber\":\"test\",\"title\":\"test\"}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testDeleteAdress() throws Exception{

        Mockito.when(
                addressController.deleteAddress(any(Principal.class),anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/addresses/1")
                .principal(mock(Principal.class))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }


}
