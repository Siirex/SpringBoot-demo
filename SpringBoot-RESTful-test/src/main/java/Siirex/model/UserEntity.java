package Siirex.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "USER_REST_API")
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;
}
