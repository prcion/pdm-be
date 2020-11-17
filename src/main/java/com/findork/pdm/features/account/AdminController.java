package com.findork.pdm.features.account;

import com.findork.pdm.security.payload.UserRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createPoll(@Valid @RequestBody UserRequest userRequest) {
       return "Congratulation Admin you can access this api";
    }

}
