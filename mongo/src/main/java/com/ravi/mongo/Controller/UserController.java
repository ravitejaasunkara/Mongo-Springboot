package com.ravi.mongo.Controller;


import com.ravi.mongo.Documents.User;
import com.ravi.mongo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/fetch/{userId}")
    public User getUser(@PathVariable("userId") String userId) {
        return userRepository.findById(userId).get();
    }
}
