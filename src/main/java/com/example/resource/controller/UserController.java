package com.example.resource.controller;

import com.example.resource.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    @RequestMapping(value="/user", method = RequestMethod.GET)
    public String listUser(){
        return "get用户";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String create(@RequestBody User user){
        return "post用户";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable(value = "id") Long id){
        return "delete success";
    }

}
