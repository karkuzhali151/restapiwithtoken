package com.example.restapiwithtoken.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {

    private int employeeid;
    private String employeename;
    private String email;
    private String profileimage;


}
