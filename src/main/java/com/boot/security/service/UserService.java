package com.boot.security.service;

import com.boot.security.model.Account;
import com.boot.security.model.ERole;
import com.boot.security.model.Role;
import com.boot.security.repository.AccountRepository;
import com.boot.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Account getUserByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Account getUserByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account setUser(Account user) throws Exception {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setIsActive(true);

        Role userRole = null;
        if (user.getUsername().equals("admin")) {
            userRole = roleRepository.findByRole(ERole.ADMIN.getValue());
        } else if (user.getUsername().equals("user")) {
            userRole = roleRepository.findByRole(ERole.MANAGER.getValue());
        } else {
            userRole = roleRepository.findByRole(ERole.GUEST.getValue());
        }
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        return accountRepository.save(user);
    }

}
