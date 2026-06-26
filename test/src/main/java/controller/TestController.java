package controller;

import annotation.Controllerako;
import annotation.GetMapping;

@Controllerako
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        return "Test OK";
    }
}