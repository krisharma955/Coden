package com.k955.Coden.controller;

import com.k955.Coden.dtos.SuperAdmin.UpdateUserRole;
import com.k955.Coden.dtos.User.UserProfileResponse;
import com.k955.Coden.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sup-adm")
public class SuperAdminController {

    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserProfileResponse> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PatchMapping("/by-email")
    public ResponseEntity<UserProfileResponse> updateUserRoleByEmail
            (@RequestParam String email, @Valid @RequestBody UpdateUserRole updateUserRole)
    {
        return ResponseEntity.ok(userService.updateUserRoleByEmail(email, updateUserRole));
    }

    @PatchMapping("/{userId}") //userId -> user whose role is being updated
    public ResponseEntity<UserProfileResponse> updateUserRole
            (@PathVariable UUID userId, @Valid @RequestBody UpdateUserRole updateUserRole)
    {
        return ResponseEntity.ok(userService.updateUserRole(userId, updateUserRole));
    }

}
