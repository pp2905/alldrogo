package com.application.alledrogo.Model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private Long phoneNumber;
    private String street;
    private int houseNumber;
    private int flatNumber;
    private String postCode;
    private String postOffice;

}
