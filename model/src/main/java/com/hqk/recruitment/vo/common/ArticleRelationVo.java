package com.hqk.recruitment.vo.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleRelationVo implements Serializable {

    private String title;
    private  Long id;
    private Long watchCount;
    private Long commentCount;
}
