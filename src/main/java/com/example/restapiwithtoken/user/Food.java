package com.example.restapiwithtoken.user;


import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue()
    private int foodid;
    private String foodname;
    private String fooddecription;
    private String foodimage;
    private String createdby;
    private Date createdat;
}
