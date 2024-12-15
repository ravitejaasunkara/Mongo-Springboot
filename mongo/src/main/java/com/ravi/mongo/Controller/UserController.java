package com.ravi.mongo.Controller;


import com.ravi.mongo.Documents.Address;
import com.ravi.mongo.Documents.User;
import com.ravi.mongo.Dto.OrderDto;
import com.ravi.mongo.Dto.StreetLike;
import com.ravi.mongo.Repository.UserRepository;
import com.ravi.mongo.Service.OrderServiceImpl;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderServiceImpl orderService;

    @GetMapping("/fetch/{userId}")
    public User getUser(@PathVariable("userId") String userId) {
        return userRepository.findById(userId).get();
    }

    @PostMapping("/save")
    public void setUser(@RequestBody User user) {
        userRepository.save(user);
    }

    @GetMapping("/age")
    public List<Document> getUserByAge(@RequestParam("age") int age) {
        return userRepository.getAge(age);
    }

    @GetMapping("/age/get")
    public Document getUserByAgeEquals(@RequestParam("getByAge") int age) {
        return userRepository.getByAge(age);
    }

    @GetMapping("/fetchByCity/{city}")
    public List<Document> fetchByCity(@PathVariable("city") String city) {
        return userRepository.findByAddressCity(city);
    }

    @GetMapping("/fetchByStreet/{street}")
    public List<Document> fetchByStreet(@PathVariable("street") String street) {
        logger.info("street like:{}",userRepository.findByAddressStreetLike(street));
        return userRepository.findByAddressStreet(street);
    }

    @GetMapping("/fetchByStreetLike/{street}")
    public List<StreetLike> fetchByStreetLike(@PathVariable("street") String street) {
        return userRepository.findByAddressStreetLike(street);
    }

    @GetMapping("/findByEmail/{email}")
    public List<Document> fetchByEmail(@PathVariable("email") String email) {
        return userRepository.findByEmail(email);
    }

    @GetMapping("/findByEmailLike/{email}")
    public List<Document> fetchByEmailLike(@PathVariable("email") String email) {
        return userRepository.findByEmailLike(email);
    }

    @GetMapping("/findByEmailLikeProjection/{email}")
    public List<Document> fetchByEmailLikeProjection(@PathVariable("email") String email) {
        return userRepository.findByEmailLikeProjection(email);
    }

    @GetMapping("/findByAgeName/{age}/{name}")
    public List<Document> findByAgeName(@PathVariable("age") int age,@PathVariable("name") String name) {
        return userRepository.findByAgeAndName(age,name);
    }
    @GetMapping("/findByAgeOrName/{age}/{name}")
    public List<Document> findByAgeOrName(@PathVariable("age") int age,@PathVariable("name") String name) {
        return userRepository.findByAgeOrName(age,name);
    }

    @GetMapping("/checkNameExists/{name}")
    public long checkNameExists(@PathVariable("name") String name) {
        return userRepository.checkNameExists(name);
    }

    @DeleteMapping("/delete/{age}")
    public ResponseEntity<String> deleteByAge(@PathVariable("age") int age) {
        try{
            userRepository.deleteByAge(age);
            return ResponseEntity.status(HttpStatus.OK).body("entry deleted successfully");
        } catch (Exception e) {
            logger.error("Error occurred while deleting entry:{}",e.getMessage());
        }
        return null;
    }


    @GetMapping("/address/{city}")
    public Address getAddressByCity(@PathVariable("city") String city) {
        Address addressByCity = userRepository.getAddressByCity(city);
        logger.info("address by city:{}",addressByCity);
        return addressByCity;
    }

    @GetMapping("/orders")
    public List<OrderDto> getOrdersData() {
        return orderService.getOrdersWithDetails();
    }

    @GetMapping("/orders/{name}")
    public List<User> getDataByQuery(@PathVariable("name") String name) {
        return orderService.getListOfUsers(name);
    }

    @GetMapping("/list/{name}/{age}")
    public Page<User> getUsersByNameAndAge(@PathVariable("name") String name, @PathVariable("age") int age, @RequestParam(value = "pageNo",defaultValue = "0") int pageNo, @RequestParam(value = "pageSize",defaultValue = "2") int pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        orderService.groupUsersByAge();
        return orderService.getUsersByNameAndAge(name,age,pageable);
    }




}
