package de.msg.training.jwtsimpleexample.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/test")
public class TestController {

    @GetMapping()
    public String userAccess() {
        return "Not protected endpoint";
    }

}
