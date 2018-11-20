package pl.kostka.restaurant.unit.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kostka.restaurant.controller.UserController;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.Role;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.repository.RoleRepository;
import pl.kostka.restaurant.repository.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RoleRepository roleRepository;


    private UserController userController;

    @Before
    public void init(){
        userController = new UserController(userRepository,roleRepository);
    }

    @Test
    public void testAddUser(){

        roleRepository.save(new Role("USER","description"));
       User resultUser = userController.addUser(new User("test99", "superTestPass", "Test", "Test", "Test", "Test"));

       Assert.assertNotNull(resultUser);
    }

    @Test
    public void testFindByUsername() throws Exception{
        User user = userRepository
                .save(new User("test99", "superTestPass", "Test", "Test", "Test", "Test"));
        User foundUserByUsername = userRepository
                .findByUsername(user.getUsername()).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        assertNotNull(foundUserByUsername);
        assertEquals(user.getUsername(), foundUserByUsername.getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testFindByUsername_notFound() throws Exception{
        userRepository.save(new User("test99", "superTestPass", "Test", "Test", "Test", "Test"));
        userRepository.findByEmail("wrongName").orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }


    @Test()
    public void testFindByEmail() throws Exception{
        User user= userRepository
                .save(new User("test99", "superTestPass", "Test", "Test", "Test", "Test"));

        User foundUserByEmail = userRepository.findByEmail(user.getEmail()).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        assertEquals(user.getEmail(), foundUserByEmail.getEmail());
        assertEquals(user.getUsername(), foundUserByEmail.getUsername());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testFindByEmail_notFOund() throws Exception{
       userRepository.save(new User("test99", "superTestPass", "Test", "Test", "Test", "Test"));
       userRepository.findByEmail("wrongEmail").orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }
}