package com.oauth2.ws.config;

import com.oauth2.ws.models.Role;
import com.oauth2.ws.models.User;
import com.oauth2.ws.repositories.RoleRepository;
import com.oauth2.ws.repositories.UserRepository;
import com.oauth2.ws.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

@Configuration
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        userRepository.deleteAll();
        roleRepository.deleteAll();
        verificationTokenRepository.deleteAll();

        User joao = new User("joao", "das neves", "joaodasneves@uol.com.br");
        User beto = new User("beto", "jamaica", "bj@ig.com.br");

        Role roleAdmin = createRoleIfNotFound("ROLE_ADMIN");
        Role roleUser = createRoleIfNotFound("ROLE_USER");

        joao.setRoles(Arrays.asList(roleAdmin));
        joao.setPassword(passwordEncoder.encode("1234"));
        joao.setEnabled(true);

        beto.setRoles(Arrays.asList(roleUser));
        beto.setPassword(passwordEncoder.encode("1234"));
        beto.setEnabled(true);

        createUserIfNotFound(joao);
        createUserIfNotFound(beto);
    }

    private User createUserIfNotFound(final User user) {
        Optional<User> obj = userRepository.findByEmail(user.getEmail());
        if(obj.isPresent()) {
            return obj.get();
        }
        return userRepository.save(user);
    }

    private Role createRoleIfNotFound(final String name) {
        Optional<Role> obj = roleRepository.findByName(name);
        if(obj.isPresent()) {
            return obj.get();
        }
        return roleRepository.save(new Role(name));
    }
}
