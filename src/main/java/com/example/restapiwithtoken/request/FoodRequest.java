package com.example.restapiwithtoken.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {

    String foodname;
    String fooddecription;
    String foodimage;
    String employeeid;
    int foodid;

}
