package com.shail.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String roles;
}
