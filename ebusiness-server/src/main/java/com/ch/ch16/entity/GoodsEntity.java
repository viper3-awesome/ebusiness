package com.ch.ch16.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "goodstable")
@SuppressWarnings("all")
public class GoodsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String gname;
    private Double goprice;
    private Double grprice;
    private Integer gstore;
    private String gpicture;
    private Integer isAdvertisement;
    private Integer goodstypeId;
    @Transient
    private String typename;
    @Transient
    private byte[] logoFile;
    @Transient
    private String fileName;
    @Transient
    private String act;
    @Transient
    private Integer currentPage;
    @Transient
    private Integer busertableId;
    @Transient
    private Integer pageSize;
}
