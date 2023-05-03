package com.hqk.recruitment.vo.company;

import lombok.Data;

import java.util.List;

@Data
public class JobRelationVo {

    private String companyId;
    private String jobId;
    private String companyName;
    private String jobName;
    private String avatar;
    private Integer startMoney;
    private Integer endMoney;
    private  Integer moneyMonth;
    private List<String> city;
}
