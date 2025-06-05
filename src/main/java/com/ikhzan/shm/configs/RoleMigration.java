package com.ikhzan.shm.configs;

import com.ikhzan.shm.data.MigrationHistory;
import com.ikhzan.shm.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class RoleMigration implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(RoleMigration.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) {
        boolean migrationAlreadyRun = mongoTemplate.exists(
                Query.query(Criteria.where("name").is("role_migration")),
                MigrationHistory.class
        );

        if (!migrationAlreadyRun) {
            userRepository.findAll().forEach(user -> {
                if (user.getRole() == null || user.getRole().isEmpty()) {
                    user.setRole("USER"); // Assign default role
                    userRepository.save(user);
                }

                if (user.getRole() != null && !user.getRole().isEmpty()){
                    if (user.getUsername().equals("admin")){
                        user.setRole("ADMIN");
                        userRepository.save(user);
                    }
                }
            });

            // Log migration as completed
            MigrationHistory migration = new MigrationHistory();
            migration.setName("role_migration");
            migration.setCompleted(true);
            mongoTemplate.save(migration);
            logger.info("Roles migration completed.");
        } else {
            logger.info("Roles migration already completed, skipping.");
        }

    }

}
