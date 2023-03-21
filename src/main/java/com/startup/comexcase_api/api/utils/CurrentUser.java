package com.startup.comexcase_api.api.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {
    public static String getUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
