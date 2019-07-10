package com.oauth2.ws.services;

import com.oauth2.ws.models.Role;
import com.oauth2.ws.models.User;
import com.oauth2.ws.repositories.UserRepository;
import com.oauth2.ws.services.exception.ObjectNotEnabledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()) {
            throw new UsernameNotFoundException(String.format("UserNotExist"));
        }else if(!user.get().isEnabled()) {
            throw new ObjectNotEnabledException(String.format("UserNotEnabled"));
        }

        return new UserRepositoryUserDetails(user.get());
    }

    private final List<GrantedAuthority> getGrantedAuthorities(final Collection<Role> roles) {
        final List<GrantedAuthority> authorities =new ArrayList<>();

        for(Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    public final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(roles);
    }

    private final static class UserRepositoryUserDetails extends User implements UserDetails {

        public UserRepositoryUserDetails(User user) {
            super(user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return getRoles();
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
