package com.ch.ch16.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@SuppressWarnings("all")
@Entity
@Table(name = "ausertable")
public class AUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String aname;
    private String apwd;
}
