package com.ch.ch16.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@SuppressWarnings("all")
@Entity
@Table(name = "orderbasetable")
public class OrdersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer busertableId;
    private Double amount;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orderdate;
    @Transient
    private Integer currentPage;
    @Transient
    private String startDate;
    @Transient
    private String endDate;
    @Transient
    private Double totalamount;
    @Transient
    private String months;
    @Transient
    private List<Integer> bshoppingnum;
    @Transient
    private List<Integer> bgid;
    @Transient
    private Integer pageSize;
}
