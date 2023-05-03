package com.hqk.recruitment.vo.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeOrDisLikeVo  implements Serializable {

    private Long commentId;
    private Long userId;
    private Long articleId;
}
