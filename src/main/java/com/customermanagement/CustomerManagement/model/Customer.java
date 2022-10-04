package com.customermanagement.CustomerManagement.model;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity

@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue
    private Long id;
    @NonNull

    @Getter @Setter
    private String name;
    private Date dateOfBirth;
    private String nicNumber;
    private int mobileNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;

}
