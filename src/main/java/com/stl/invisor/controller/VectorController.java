package com.stl.invisor.controller;


import org.springframework.web.bind.annotation.RestController;

@RestController
public class VectorController {


    public String reloadVectorStore(){
        return "ok";
    }
}
