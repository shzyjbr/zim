package com.zzk.forwardroute.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzk
 * @date 2022/8/14 19:35
 * @desctiption
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQuery {
    private Integer pageNum;
    private Integer pageSize;
    private String uid;

}
