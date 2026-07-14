package controller;

import annotation.Controllerako;
import annotation.GetMapping;
import annotation.PostMapping;

@Controllerako
public class TestController {
    // @GetMapping("/elie")
    // public void testGet() {

    // }

    @GetMapping("/elie")
    public String testDoublonGet() {
        return "teste kely raha miverina";
    }

    @PostMapping("/elie")
    public void testPost() {
        
    }
}