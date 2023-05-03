package com.hqk.recruitment.model.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLike  implements Serializable {
    private Long commentId;
    private Long userId;
    private Integer status;
    private Date updateTime;
}
