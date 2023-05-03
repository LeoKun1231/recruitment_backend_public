package com.hqk.recruitment.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Sort  implements Serializable {

    private Long id;
    private Long count;
}
