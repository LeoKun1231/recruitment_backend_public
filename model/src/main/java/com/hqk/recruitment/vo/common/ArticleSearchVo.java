package com.hqk.recruitment.vo.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ArticleSearchVo  implements Serializable {

    private String title;
    private Long id;
}
