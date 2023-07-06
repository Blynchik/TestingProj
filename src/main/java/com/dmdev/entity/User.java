package com.dmdev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Integer id;
    private String name;
    private LocalDate birthday;
    private String email;
    private String password;
    private Role role;
    private Gender gender;

    public User(String name, LocalDate birthday, String email,
                String password, Role role, Gender gender){
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.role = role;
        this.gender = gender;
    }
}
