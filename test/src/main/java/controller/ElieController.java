package controller;

import annotation.Controllerako;
import annotation.GetMapping;

@Controllerako
public class ElieController {
    @GetMapping("/hello")
    public void hello() {
    }

    @GetMapping("/test")
    public void test() {
    }

    @GetMapping // URL vide
    public void index() {
    }
}
