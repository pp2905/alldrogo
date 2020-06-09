package com.application.alledrogo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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

    @NotNull
    @NotBlank
    @OneToOne
    private Category category;

    @NotNull
    @NotBlank
    private int ownerId;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @NotBlank
    private BigDecimal price;

    @NotNull
    @NotBlank
    private int baseQuantity;

    @NotNull
    @NotBlank
    private int quantity;

    @NotNull
    @NotBlank
    private LocalDateTime startDate;

    @NotNull
    @NotBlank
    private LocalDateTime endDate;
}
