package pl.kostka.restaurant.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.repository.UserRepository;

import java.util.List;

@RestController("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/hello")
    public String hello()
    {
        return "hello";
    }

    @GetMapping
    public List<User> getUser() {
        return userRepository.findAll();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }



}
