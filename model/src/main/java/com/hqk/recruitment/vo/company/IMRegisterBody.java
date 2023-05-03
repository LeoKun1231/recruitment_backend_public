package com.hqk.recruitment.vo.company;

import lombok.Data;

import java.util.List;

@Data
public class IMRegisterBody {
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;
    private List<ResultItem> ResultItem;
}
