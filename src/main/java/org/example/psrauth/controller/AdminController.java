package org.example.psrauth.controller;

import lombok.RequiredArgsConstructor;
import org.example.psrauth.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/secure")
    public String securedEndpoint() {
        return "You have access to this secured endpoint!";
    }

    @PostMapping("/changeUserRole")
    public String changeUserRole(@RequestParam String username, @RequestParam String newRole) {
        try {
            userService.changeRole(username, newRole);
            return "User role updated successfully!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}