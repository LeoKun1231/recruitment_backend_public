package com.hqk.recruitment.vo.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class JobListVo {


    private String companyId;
    private String jobId;

    private String jobName;
    private Integer startMoney;
    private Integer endMoney;
    private Integer moneyMonth;
    private List<String> city;
    private String jobRequire;
    private List<String> tag;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> weal;

    private String avatar;
    private String companyName;
    private String category;
    private String size;
    private String level;

}
