package com.stl.invisor;

import com.stl.invisor.entities.User;
import com.stl.invisor.entities.UserRole;
import com.stl.invisor.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class Loader {

    Logger logger = LoggerFactory.getLogger(Loader.class);

    private UserRepository userRepository;

    public Loader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void main (){
        UUID admin = createAdminUser();
        populateUser("Batman", admin, UserRole.PREMIUM);
    }

    private UUID createAdminUser() {
        User admin = userRepository.findByName("Admin");
        // just to make life easier when development
        UUID creator = UUID.fromString("2a81b88a-8465-4c8a-b4e0-09dd098c0d7b");
        if (admin == null){
            admin = User.builder()
                    .withName("Admin")
                    .withIsActive(false)
                    .withUserRole(UserRole.ADMIN)
                    .withCreatedBy(creator)
                    .withUuid(creator)
                    .withCreatedTime(Instant.now())
                    .build();
            userRepository.save(admin);
        }

        return  admin.getUuid();
    }

    private User populateUser(String name, UUID admin, UserRole role) {
        User user = userRepository.findByName(name);
        if (user ==null){
            User character = User.builder()
                    .withName(name)
                    .withUserRole(role)
                    .withCreatedTime(Instant.now())
                    .withCreatedBy(admin)
                    .withIsActive(true)
                    // To make life easy when developing
                    .withUuid(UUID.fromString("cd0543c0-c619-4aa4-8eb6-e1e721a4cc6a"))
                    .build();
            userRepository.save(character);
        }
        return user;
    }

}
