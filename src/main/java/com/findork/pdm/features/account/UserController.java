package com.findork.pdm.features.account;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String getCurrentUser() {
        return "Congratulation User you can access this api";
    }
}
