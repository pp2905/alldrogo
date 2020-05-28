package com.application.alledrogo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auction")
public class Auction {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Category category;

    @OneToOne
    private SubCategory subCategory;

    private int ownerId;
    private String title;
    private String description;
    private double price;
    private int baseQuantity;
    private int quantity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
