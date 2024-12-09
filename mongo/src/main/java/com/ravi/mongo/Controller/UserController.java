package com.ravi.mongo.Controller;


import com.ravi.mongo.Documents.User;
import com.ravi.mongo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/fetch/{userId}")
    public User getUser(@PathVariable("userId") String userId) {
        return userRepository.findById(userId).get();
    }

    @PostMapping("/save")
    public void setUser(@RequestBody User user) {
        userRepository.save(user);
    }
}
