package Siirex;

import Siirex.model.UserEntity;
import Siirex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    UserRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // INSERT user
        UserEntity user = new UserEntity();
        user.setUsername("hoangminh");
        user.setPassword(passwordEncoder.encode("304304"));
        repository.save(user);
        System.out.println(user);
    }
}
