package com.hqk.recruitment.vo.company;

import com.hqk.recruitment.model.company.Company;
import lombok.Data;

@Data
public class CompanyDetailVo extends Company {

    private Long jobCount;
    private Long hrCount;
}
