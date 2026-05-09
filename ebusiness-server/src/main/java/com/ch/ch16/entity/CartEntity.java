package com.ch.ch16.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "carttable")
@SuppressWarnings("all")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer busertableId;
    private Integer goodstableId;
    private Integer shoppingnum;
    @Transient
    private List<Integer> bshoppingnum;
    @Transient
    private List<Integer> bcid;
}
