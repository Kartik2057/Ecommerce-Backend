package com.kartik.Ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kartik.Ecommerce.model.Address;
import com.kartik.Ecommerce.model.PaymentInformation;
import com.kartik.Ecommerce.model.Rating;
import com.kartik.Ecommerce.model.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String role;
    private String mobile;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "payment_information",joinColumns = @JoinColumn(name="user_id"))
    private List<PaymentInformation> paymentInformation = new ArrayList<>();


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();
    private LocalDateTime createdAt;
}
