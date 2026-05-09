package com.ch.ch16.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orderdetail")
@SuppressWarnings("all")
public class OrderdetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer orderbasetableId;
    private Integer goodstableId;
    private Integer shoppingnum;
}
