package com.hqk.recruitment.vo.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentQueryVo  implements Serializable {

//    private Long commentId;
    private Long articleId;
    private Long userId;
    private Long rootId;
    private Long currentPage;
    private Long pageSize;
}
