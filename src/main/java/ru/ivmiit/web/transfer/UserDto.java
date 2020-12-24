package ru.ivmiit.web.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ivmiit.web.model.User;
import ru.ivmiit.web.security.details.Role;
import ru.ivmiit.web.security.details.State;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String name;
    private String login;
    private List<Role> role;
    private State state;
    private String email;

    public static UserDto from(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .login(user.getLogin())
                .role(user.getRoles())
                .state(user.getState())
                .email(user.getEmail())
                .build();
    }
}
