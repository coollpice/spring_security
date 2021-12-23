package com.boot.security;

import com.boot.security.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class SampleQuery {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init1();
        initService.init2();
        initService.init3();
    }

    @Service
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;

        public void init1() {
            Role role = new Role();
            role.setRole("ROLE_ADMIN");
            em.persist(role);
        }

        public void init2() {
            Role role = new Role();
            role.setRole("ROLE_MANAGER");
            em.persist(role);
        }

        public void init3() {
            Role role = new Role();
            role.setRole("ROLE_GUEST");
            em.persist(role);
        }
    }
}
