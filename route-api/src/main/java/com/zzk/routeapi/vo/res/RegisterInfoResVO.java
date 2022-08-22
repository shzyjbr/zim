package com.zzk.routeapi.vo.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInfoResVO implements Serializable{

    private String userName ;
    private String password ;

}
