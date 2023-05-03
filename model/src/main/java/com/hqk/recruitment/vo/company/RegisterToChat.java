package com.hqk.recruitment.vo.company;

import lombok.Data;

@Data
public class RegisterToChat {

    private static final long serialVersionUID=1L;

    private String id;

    private Long userId;
    private Long toId;

    private String jobId;
}
