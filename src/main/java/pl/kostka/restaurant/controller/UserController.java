package pl.kostka.restaurant.controller;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getUser() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        String hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hash);
        return userRepository.save(user);
    }

    @PostMapping("users/login")
    public boolean checkUser(@RequestBody User user) {
        User userDb = userRepository.findByLogin(user.getLogin());
        return BCrypt.checkpw(user.getPassword(), userDb.getPassword());
    }

    @PutMapping("/users/{userId}")
    public User updateUser(@PathVariable Long userId, @Valid @RequestBody User userRequest) {
        return userRepository.findById(userId).map(user -> {
            user.setName(userRequest.getName());
            user.setSurname(userRequest.getSurname());
            user.setPhoneNumber(userRequest.getPhoneNumber());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId "+ userId + " not found"));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return userRepository.findById(userId).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("UserId "+ userId + " not found"));
    }


}
