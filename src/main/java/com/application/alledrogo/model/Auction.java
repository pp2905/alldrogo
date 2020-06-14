package com.application.alledrogo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty
    @OneToOne
    private Category category;

    @NotNull
    @NotBlank
    @NotEmpty
    private int ownerId;

    @NotNull
    @NotBlank
    @NotEmpty
    private String title;

    @NotNull
    @NotBlank
    @NotEmpty
    private String description;

    @NotNull
    @NotBlank
    @NotEmpty
    private BigDecimal price;

    @NotNull
    @NotBlank
    @NotEmpty
    private int baseQuantity;

    @NotNull
    @NotBlank
    @NotEmpty
    private int quantity;

    @NotNull
    @NotBlank
    @NotEmpty
    private LocalDateTime startDate;

    @NotNull
    @NotBlank
    @NotEmpty
    private LocalDateTime endDate;
}
