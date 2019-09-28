package com.amairovi.keeper.web;

import com.amairovi.keeper.dto.Authentication;
import com.amairovi.keeper.dto.Registration;
import com.amairovi.keeper.dto.UserPlace;
import com.amairovi.keeper.model.User;
import com.amairovi.keeper.service.UserPlaceService;
import com.amairovi.keeper.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserPlaceService placeService;
    private final UserService userService;

    @PutMapping
    public void register(@RequestBody Registration registration) {
        userService.register(registration.getEmail());
    }

    @PostMapping
    public String authentication(@RequestBody Authentication authentication) {
        String email = authentication.getEmail();
        return userService.findByEmail(email)
                .getId();
    }

    @GetMapping("/{userId}/places")
    public List<UserPlace> getPlacesForUser(@PathVariable String userId) {
        User user = userService.findById(userId);

        return placeService.getPlacesHierarchyForUser(user);
    }
}
