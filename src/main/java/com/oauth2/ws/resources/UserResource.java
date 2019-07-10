package com.oauth2.ws.resources;

import com.oauth2.ws.models.Role;
import com.oauth2.ws.models.User;
import com.oauth2.ws.dto.UserDTO;
import com.oauth2.ws.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserResource {

    @Autowired
    private UserService userService;

    private TokenStore tokenStore = new InMemoryTokenStore();

    @Autowired
    private DefaultTokenServices tokenService = new DefaultTokenServices();

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<User> users = userService.findAll();
        List<UserDTO> usersDTO = users.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(usersDTO);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable String id) {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(new UserDTO(user));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        User user = userService.fromDTO(userDTO);
        return ResponseEntity.ok().body(new UserDTO(userService.create(user)));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable String id, @RequestBody UserDTO userDTO) {
        User user = userService.fromDTO(userDTO);
        user.setId(id);
        return ResponseEntity.ok().body(new UserDTO(userService.update(user)));
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("users/{id}/roles")
    public ResponseEntity<List<Role>> getRoles(@PathVariable String id) {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(user.getRoles());
    }

    @GetMapping("/users/main")
    public ResponseEntity<UserDTO> getUserMain(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        UserDTO userDTO = new UserDTO(user);
        userDTO.setPassword("");
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenService.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
            tokenService.revokeToken(String.valueOf(accessToken));
        }

        return ResponseEntity.noContent().build();
    }
}
