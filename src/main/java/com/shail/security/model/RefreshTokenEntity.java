package com.shail.security.model;

import lombok.*;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
@GenericGenerator(name = "pkgen", strategy = "increment")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(generator="pkgen")
    private Integer id;
    @Column(columnDefinition = "LONGTEXT")
    private String token;
    private boolean validity;

    public RefreshTokenEntity(String token, boolean validity) {
        this.token = token;
        this.validity = validity;
    }
}
