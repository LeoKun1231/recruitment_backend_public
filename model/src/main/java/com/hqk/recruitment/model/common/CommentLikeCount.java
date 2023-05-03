package com.hqk.recruitment.model.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikeCount implements Serializable {

    private Long commentId;
    private Long likeCount;
}
