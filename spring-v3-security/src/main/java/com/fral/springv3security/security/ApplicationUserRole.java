package com.fral.springv3security.security;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.fral.springv3security.security.ApplicationUserPermission.COURSE_READ;
import static com.fral.springv3security.security.ApplicationUserPermission.COURSE_WRITE;
import static com.fral.springv3security.security.ApplicationUserPermission.STUDENT_READ;
import static com.fral.springv3security.security.ApplicationUserPermission.STUDENT_WRITE;

public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }
}
