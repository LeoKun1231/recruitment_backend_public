package com.hqk.recruitment.vo.company;

import lombok.Data;

import java.util.List;

@Data
public class JobWithCompanyVo {
    private String jobId;

    private String jobName;
    private Integer startMoney;
    private Integer endMoney;
    private Integer moneyMonth;
    private List<String> city;
    private String id;
    private String jobRequire;
}
