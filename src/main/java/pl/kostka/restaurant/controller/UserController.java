package pl.kostka.restaurant.controller;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.exception.ResourceSaveException;
import pl.kostka.restaurant.exception.UserAlreadyExistsException;
import pl.kostka.restaurant.model.Role;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.repository.RoleRepository;
import pl.kostka.restaurant.repository.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/users/user")
    public User getUser(Principal principal) {
        return userRepository.findByUsername(principal.getName()).map(user -> {
            user.setPassword(null);
            user.setRoles(null);
            return user;
        }).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        if(user.getUsername() != null && user.getName() != null && user.getSurname() != null && user.getEmail() != null && user.getPhoneNumber() != null) {
            if (!userRepository.findByUsername(user.getUsername()).isPresent()) {

                    String hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                    user.setPassword(hash);

                    Role role = roleRepository.findByRoleName("USER").orElseThrow(()-> new ResourceNotFoundException("User role not found"));
                    List<Role> roles = new ArrayList<>();
                    roles.add(role);
                    user.setRoles(roles);
                    try {
                        return userRepository.save(user);
                    } catch (Exception e) {
                        throw new ResourceSaveException(e.getMessage());
                    }

            } else
                throw new UserAlreadyExistsException("Username: " + user.getUsername() + " is already taken");
        } else {
            throw new NullPointerException("Some of user atributes is null!");
        }
    }

    @PostMapping("users/login")
    public boolean checkUser(@RequestBody User user) {
        return userRepository.findByUsername(user.getUsername()).map(userDb ->
            BCrypt.checkpw(user.getPassword(), userDb.getPassword())
        ).orElse( false);
    }

    @PostMapping("users/check-username")
    public boolean checkIfUsernameIsFree(@RequestBody String username) {
        return !userRepository.findByUsername(username).isPresent();

    }

    @PostMapping("users/check-email")
    public boolean checkIfEmailIsFree(@RequestBody String email) {
        return !userRepository.findByEmail(email).isPresent();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/users/{userId}")
    public User updateUser(@PathVariable("userId") Long userId, Principal principal, @RequestBody User userRequest) {
       return userRepository.findByUsername(principal.getName()).map(user -> {
           if (user.getId().equals(userId)) {
               user.setName(userRequest.getName());
               user.setSurname(userRequest.getSurname());
               user.setPhoneNumber(userRequest.getPhoneNumber());
               user.setEmail(userRequest.getEmail());
               return userRepository.save(user);
           } else {
               throw new ResourceNotFoundException("User: "+ principal.getName() + "not found");
           }
       }).orElseThrow(()-> new  ResourceNotFoundException("User: "+ principal.getName() + "not found"));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        return userRepository.findById(userId).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("UserId "+ userId + " not found"));
    }


}
