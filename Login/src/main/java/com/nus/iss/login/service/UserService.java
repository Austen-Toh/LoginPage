package com.example.login.service;

import com.example.login.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean validateUser(User user) {
        logger.info("Validating user: {}", user.getUsername());
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";

        try {
            Integer count = jdbcTemplate.queryForObject(
                    query,
                    new Object[]{user.getUsername(), user.getPassword()},
                    Integer.class
            );

            boolean isValidUser = count != null && count > 0;
            if (isValidUser) {
                logger.info("User '{}' successfully validated.", user.getUsername());
            } else {
                logger.warn("Failed validation for user '{}'. Incorrect username or password.", user.getUsername());
            }
            return isValidUser;
        } catch (Exception e) {
            logger.error("Error occurred while validating user '{}': {}", user.getUsername(), e.getMessage(), e);
            return false;
        }
    }
}
