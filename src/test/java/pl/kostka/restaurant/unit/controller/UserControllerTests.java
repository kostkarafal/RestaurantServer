package pl.kostka.restaurant.unit.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kostka.restaurant.controller.UserController;
import pl.kostka.restaurant.model.Role;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.repository.RoleRepository;
import pl.kostka.restaurant.repository.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserController userController;

    User mockUser = new User("test99", "superTestPass", "Test", "Test", "Test", "Test");
    String mockUserRequest = "{\"username\":\"test99\",\"password\":\"superTestPass\",\"name\":\"Test\",\"surname\":\"Test\",\"phoneNumber\":\"Test\",\"email\":\"Test\"}";
    String wrongUserReguest = "{\"username\":\"test99\"}";

    Role mockRole = new Role("USER", "test");

    Principal mockPrincipal = new Principal() {
        @Override
        public String getName() {
            return "test";
        }
    };


    @Test
    public void addUser() throws Exception {
        Mockito.when(
                userController.addUser(Mockito.any(User.class))).thenReturn(mockUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users")
                .accept(MediaType.APPLICATION_JSON).content(mockUserRequest)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{\"id\":null,\"username\":\"test99\",\"password\":\"superTestPass\",\"name\":\"Test\",\"surname\":\"Test\",\"phoneNumber\":\"Test\",\"email\":\"Test\",\"roles\":null}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }

    @Test
    public void getUser() throws Exception {

        Mockito.when(
                userController.getUser(Mockito.any(Principal.class))).thenReturn(mockUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/user")
                .principal(mockPrincipal)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{\"id\":null,\"username\":\"test99\",\"password\":\"superTestPass\",\"name\":\"Test\",\"surname\":\"Test\",\"phoneNumber\":\"Test\",\"email\":\"Test\",\"roles\":null}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }

    @Test
    public  void checkUser() throws Exception {
        Mockito.when(
                userController.checkUser(Mockito.any(User.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .accept(MediaType.APPLICATION_JSON).content(mockUserRequest)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "true";

        Assert.assertEquals(expected, result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }

    @Test
    public  void checkIfUsernameIsFree() throws Exception {
        Mockito.when(
                userController.checkIfUsernameIsFree(Mockito.any(String.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/check-username")
                .accept(MediaType.APPLICATION_JSON).content(mockUserRequest)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "true";

        Assert.assertEquals(expected, result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }

    @Test
    public  void checkIfEmailIsFree() throws Exception {
        Mockito.when(
                userController.checkIfEmailIsFree(Mockito.any(String.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/check-email")
                .accept(MediaType.APPLICATION_JSON).content(mockUserRequest)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "true";

        Assert.assertEquals(expected, result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void getUsers() throws Exception {

        List<User> mockUsers = Arrays.asList(mockUser);
        Mockito.when(
                userController.getUsers()).thenReturn(mockUsers);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/users").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "[{\"id\":null,\"username\":\"test99\",\"password\":\"superTestPass\",\"name\":\"Test\",\"surname\":\"Test\",\"phoneNumber\":\"Test\",\"email\":\"Test\",\"roles\":null}]";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }

    @Test
    public void updateUser() throws Exception {

        Mockito.when(
                userController.updateUser(Mockito.anyLong() ,Mockito.any(Principal.class),Mockito.any(User.class))
        ).thenReturn(mockUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/users/1")
                .principal(mockPrincipal)
                .accept(MediaType.APPLICATION_JSON).content(mockUserRequest)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{\"id\":null,\"username\":\"test99\",\"password\":\"superTestPass\",\"name\":\"Test\",\"surname\":\"Test\",\"phoneNumber\":\"Test\",\"email\":\"Test\",\"roles\":null}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }

    @Test
    public void deleteUser() throws Exception {

        Mockito.when(
                userController.deleteUser(Mockito.anyLong())
        ).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/users/1")
                .accept(MediaType.APPLICATION_JSON);


        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        int expectedStatusCode = 200;
        Assert.assertEquals(expectedStatusCode,  result.getResponse().getStatus());
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }

}
