package ru.ivmiit.web.forms;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ForgotForm {
    String login;
}
