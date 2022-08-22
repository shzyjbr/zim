package com.zzk.forwardroute.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzk
 * @date 2022/8/14 18:58
 * @desctiption
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String uid;
    private String username;
    private String password;
}
