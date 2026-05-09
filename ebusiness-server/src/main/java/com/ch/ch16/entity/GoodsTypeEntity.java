package com.ch.ch16.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@SuppressWarnings("all")
@Entity
@Table(name = "goodstype")
public class GoodsTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String typename;
    @Transient
    private Integer currentPage;
    @Transient
    private Integer pageSize;
}
