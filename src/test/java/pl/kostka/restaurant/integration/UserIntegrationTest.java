package pl.kostka.restaurant.integration;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.kostka.restaurant.controller.UserController;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.exception.ResourceSaveException;
import pl.kostka.restaurant.exception.UserAlreadyExistsException;
import pl.kostka.restaurant.model.Role;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.repository.RoleRepository;
import pl.kostka.restaurant.repository.UserRepository;

import java.security.Principal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserIntegrationTest {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RoleRepository roleRepository;


    private UserController userController;

    private User testUser;

    private Principal principal;

    @Before
    public void init() {
        userController = new UserController(userRepository, roleRepository);
        testUser = new User("test99", "superTestPass", "Test", "Test", "Test", "Test");
        principal = new Principal() {
            @Override
            public String getName() {
                return "test99";
            }
        };
        roleRepository.save(new Role("USER","description"));
    }

    @Test
    public void testAddUser() {

        User resultUser = userController.addUser(new User(testUser));

        Assert.assertNotNull(resultUser);
        Assert.assertNotNull(resultUser.getRoles());
        Assert.assertNotEquals(resultUser, testUser);
        Assert.assertNotEquals(resultUser.getPassword(), testUser.getPassword());
        Assert.assertEquals(resultUser.getUsername(),testUser.getUsername());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testAddUser_userAlreadyExists() {
        userController.addUser(new User(testUser));
        userController.addUser(new User(testUser));
    }

    @Test(expected = NullPointerException.class)
    public void testAddUser_emptyUser() {
        userController.addUser(new User());
    }

    @Test
    public void checkUser_false() {
        boolean result = userController.checkUser(testUser);
        Assert.assertNotNull(result);
        Assert.assertEquals(false, result);
    }

    @Test
    public void checkUser_true() {
        userController.addUser(new User(testUser));
        boolean result = userController.checkUser(testUser);
        Assert.assertNotNull(result);
        Assert.assertEquals(true, result);
    }

    @Test
    public void checkUsernameIsFree_true(){
        Boolean result = userController.checkIfUsernameIsFree("test");
        Assert.assertEquals(true, result);
    }

    @Test
    public void checkUsernameIsFree_false(){
        userController.addUser(new User(testUser));
        Boolean result = userController.checkIfUsernameIsFree(testUser.getUsername());
        Assert.assertEquals(false, result);
    }

    @Test
    public void checkEmailIsFree_true(){
        Boolean result = userController.checkIfEmailIsFree("test");
        Assert.assertEquals(true, result);
    }

    @Test
    public void checkEmailIsFree_false(){
        userController.addUser(new User(testUser));
        Boolean result = userController.checkIfEmailIsFree(testUser.getEmail());
        Assert.assertEquals(false, result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateUser_resourceNotFound(){
        userController.updateUser(1L, principal, testUser);
    }

    @Test
    public void updateUser(){
        User updateUser = new User("new","new","new","new","new","new");

        User createdUser = userController.addUser(new User(testUser));
        User resultUser = userController.updateUser(createdUser.getId(), principal, updateUser);

        Assert.assertNotNull(resultUser);
        assertNotEquals(resultUser, updateUser);
        assertNotEquals(resultUser, testUser);
        assertEquals(resultUser.getUsername(), testUser.getUsername());
        assertEquals(resultUser.getPassword(), createdUser.getPassword());
        assertEquals(resultUser.getEmail(), updateUser.getEmail());
        assertEquals(resultUser.getName(), updateUser.getName());
        assertEquals(resultUser.getPhoneNumber(), updateUser.getPhoneNumber());
        assertEquals(resultUser.getSurname(), updateUser.getSurname());
    }
}