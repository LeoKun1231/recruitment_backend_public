package com.hqk.recruitment.vo.common;

import com.hqk.recruitment.vo.base.PageVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ReportVo extends PageVo implements Serializable {
    private Integer type;
    private Long reportCount;
    private Long id;
    private Date updateTime;
}
