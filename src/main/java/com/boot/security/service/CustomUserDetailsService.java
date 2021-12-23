package com.boot.security.service;

import com.boot.security.model.Account;
import com.boot.security.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("===> ['loadUserByUsername 호출' ");
        Account user = null;

        try {
            user = userService.getUserByUsername(username);
        } catch (Exception e) {
            log.error("===> error : {}", e.getMessage());
            throw new UsernameNotFoundException(e.getMessage());
        }

        if (user == null) {
            throw new UsernameNotFoundException("");
        }

        return buildUserForAuthentication(user, getUserAuthority(user.getRoles()));
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {

        return userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole()))
                .distinct()
                .collect(Collectors.toList());
    }

    private UserDetails buildUserForAuthentication(Account user, List<GrantedAuthority> userAuthorities) {
        return new User(user.getUsername(), user.getPassword(), user.getIsActive(),
                true, true, true, userAuthorities);
    }
}
