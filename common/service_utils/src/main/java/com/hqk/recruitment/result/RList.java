package com.hqk.recruitment.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RList {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private List data = new ArrayList();

    private RList(){}

    public static RList ok(){
        RList r = new RList();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }

    public static RList error(){
        RList r = new RList();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }

    public RList success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public RList message(String message){
        this.setMessage(message);
        return this;
    }

    public RList code(Integer code){
        this.setCode(code);
        return this;
    }

    public RList data(List list){
        this.setData(list);
        return this;
    }
}
