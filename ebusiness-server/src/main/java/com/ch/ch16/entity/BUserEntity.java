package com.ch.ch16.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@SuppressWarnings("all")
@Entity
@Table(name = "busertable")
public class BUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String bemail;
    private String bpwd;
    @Transient
    private String code;
}
