package com.hqk.recruitment.vo.company;

import com.hqk.recruitment.vo.base.PageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CompanyListVo  {

    @ApiModelProperty("公司id")
    private String companyId;

    private String avatar;
    private String shortName;
    private String size;
    private String category;
    private String level;

    private List<JobWithCompanyVo> jobList;

}
