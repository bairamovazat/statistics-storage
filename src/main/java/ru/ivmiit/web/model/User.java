package ru.ivmiit.web.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.ivmiit.web.security.details.Role;
import ru.ivmiit.web.security.details.State;

import java.util.List;
import java.util.UUID;


@Document(collection = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User {

    @Id
    private String id;

    private String name;

    private UUID uuid;

    @Indexed(unique = true)
    private String login;

    private String hashPassword;

    private List<Role> roles;

    private State state;

    private String email;

    public boolean hasRole(Role role){
        return roles.stream().anyMatch(r -> r.equals(role));
    }

}