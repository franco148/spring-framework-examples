package com.fral.springv3security.auth;

import com.google.common.collect.Lists;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.fral.springv3security.security.ApplicationUserRole.ADMIN;
import static com.fral.springv3security.security.ApplicationUserRole.ADMINTRAINEE;
import static com.fral.springv3security.security.ApplicationUserRole.STUDENT;

@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao {

    private final PasswordEncoder passwordEncoder;

    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> applicationUsers = Lists.newArrayList(
            new ApplicationUser(
                "annasmith",
                passwordEncoder.encode("password"),
                STUDENT.getGrantedAuthorities(),
                true,
                true,
                true,
                true
            ),
            new ApplicationUser(
                "mariajones",
                passwordEncoder.encode("password"),
                ADMIN.getGrantedAuthorities(),
                true,
                true,
                true,
                true
            ),
            new ApplicationUser(
                "tom",
                passwordEncoder.encode("password"),
                ADMINTRAINEE.getGrantedAuthorities(),
                true,
                true,
                true,
                true
            )
        );

        return applicationUsers;
    }
}
