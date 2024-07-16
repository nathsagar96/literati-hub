package dev.sagar.literatihub;

import dev.sagar.literatihub.role.Role;
import dev.sagar.literatihub.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class LiteratiHubApplication {

  public static void main(String[] args) {
    SpringApplication.run(LiteratiHubApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
    return args -> {
      if (roleRepository.findByName("USER").isEmpty()) {
        roleRepository.save(Role.builder().name("USER").build());
      }
    };
  }
}
