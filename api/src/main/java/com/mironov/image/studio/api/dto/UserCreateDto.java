package com.mironov.image.studio.api.dto;

import com.mironov.image.studio.enums.Status;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto {

    private Long id;
    @NotBlank(message = "Не должно быть пустым")
    @Size(min = 3, max = 15, message = "Логин должен быть от 3 до 15 символов")
    private String username;
    @NotBlank(message = "Не должно быть пустым")
    @Size(min = 5, max = 100, message = "Имэйл должен быть от 5 до 100 символов")
    private String email;
    @NotBlank(message = "Не должно быть пустым")
    @Size(min = 4, max = 25, message = "Введите пароль от 4 до 25 символов")
    private String password;
    @Pattern(regexp = "\\+3[0-9]{11}", message = "Телефонный номер должен начинаться с +3, затем - 11 цифр")
    private String phone;
    @NotBlank(message = "Не должно быть пустым")
    @Size(min = 2, max = 15, message = "Введите имя от 2 до 15 символов")
    private String firstName;
    @NotBlank(message = "Не должно быть пустым")
    @Size(min = 2, max = 15, message = "Введите имя от 2 до 15 символов")
    private String lastName;
    private List<RoleDto> roles;
    private Status status;

    public List<RoleDto> getRoles() {
        if (roles == null) {
            return new ArrayList<>();
        }
        return roles;
    }
}
