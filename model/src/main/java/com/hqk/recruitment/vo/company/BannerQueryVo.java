package com.hqk.recruitment.vo.company;

import com.hqk.recruitment.vo.base.PageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BannerQueryVo extends PageVo  {
    private static final long serialVersionUID=1L;

    private String id;

    private String creator;

    private String govUrl;

    @ApiModelProperty(value = "上线状态")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private List<String> createTime;

    @ApiModelProperty(value = "更新时间")
    private List<String> updateTime;
}
