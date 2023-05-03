package com.hqk.recruitment.vo.common;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CommentReportVo implements Serializable {

    private Long id;
    private String avatar;
    private String nickName;
    private String comment;
    private List<String> reason;
    private Long reportCount;
    private Date updateTime;

}
