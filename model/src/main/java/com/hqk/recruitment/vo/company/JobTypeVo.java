package com.hqk.recruitment.vo.company;

import com.hqk.recruitment.vo.base.PageVo;
import lombok.Data;

@Data
public class JobTypeVo extends PageVo {

    private String type;
    private String companyId;
}
