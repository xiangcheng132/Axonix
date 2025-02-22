package com.Axonix.controller;

import com.Axonix.demo.model.User;
import com.Axonix.demo.model.UserExample;
import com.Axonix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/count")
    public long countUsers(UserExample example) {
        return userService.countUsers(example);
    }

    @DeleteMapping("/deleteByExample")
    public int deleteUsers(@RequestBody UserExample example) {
        return userService.deleteUsers(example);
    }

    @DeleteMapping("/delete/{id}")
    public int deleteUserById(@PathVariable Integer id) {
        return userService.deleteUserById(id);
    }

    @PostMapping("/add")
    public int addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping("/addSelective")
    public int addUserSelective(@RequestBody User user) {
        return userService.addUserSelective(user);
    }

    @PostMapping("/getByExampleWithBLOBs")
    public List<User> getUsersByExampleWithBLOBs(@RequestBody UserExample example) {
        return userService.getUsersByExampleWithBLOBs(example);
    }

    @PostMapping("/getByExample")
    public List<User> getUsersByExample(@RequestBody UserExample example) {
        return userService.getUsersByExample(example);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/updateSelective")
    public int updateUsersSelective(@RequestBody User user, @RequestBody UserExample example) {
        return userService.updateUsersSelective(user, example);
    }

    @PutMapping("/updateWithBLOBs")
    public int updateUsersWithBLOBs(@RequestBody User user, @RequestBody UserExample example) {
        return userService.updateUsersWithBLOBs(user, example);
    }

    @PutMapping("/update")
    public int updateUsers(@RequestBody User user, @RequestBody UserExample example) {
        return userService.updateUsers(user, example);
    }

    @PutMapping("/updateByIdSelective")
    public int updateUserByIdSelective(@RequestBody User user) {
        return userService.updateUserByIdSelective(user);
    }

    @PutMapping("/updateByIdWithBLOBs")
    public int updateUserByIdWithBLOBs(@RequestBody User user) {
        return userService.updateUserByIdWithBLOBs(user);
    }

    @PutMapping("/updateById")
    public int updateUserById(@RequestBody User user) {
        return userService.updateUserById(user);
    }
}
