package com.application.alledrogo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "sub_category")
public class SubCategory {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Category category;

    private String name;
    private String description;
}
