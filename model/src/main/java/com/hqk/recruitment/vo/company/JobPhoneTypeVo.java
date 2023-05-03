package com.hqk.recruitment.vo.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class JobPhoneTypeVo {

    private String text;
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<JobPhoneTypeVo> children;

}
