package com.oauth2.ws.resources;

import com.oauth2.ws.dto.UserDTO;
import com.oauth2.ws.models.User;
import com.oauth2.ws.resources.utils.GenericResponse;
import com.oauth2.ws.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class RegistrationResource {

    @Autowired
    private UserService userService;

    @PostMapping("/registration/users")
    public ResponseEntity<Void> registerUser(@RequestBody UserDTO userDTO) {
        User user = userService.fromDTO(userDTO);
        userService.registerUser(user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/registrationConfirm/users")
    public ResponseEntity<GenericResponse> confirmRegistrationUser(@RequestParam("token") String token) {
        final Object result = userService.validateVerificationToken(token);
        if(result == null) {
            return ResponseEntity.ok().body(new GenericResponse("Success"));
        }
        return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result.toString()));
    }

    @GetMapping("/resendRegistrationToken/users")
    public ResponseEntity<Void> resendRegistrationToken(@RequestParam("email") String email) {
        userService.generateNewVerificationToken(email);
        return ResponseEntity.noContent().build();
    }
}
