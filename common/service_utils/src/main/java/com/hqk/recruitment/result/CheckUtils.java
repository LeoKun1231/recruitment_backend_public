package com.hqk.recruitment.result;

public class CheckUtils {


    public static R isAdd(boolean b,String message){
        if(b){
            return R.ok().message("添加"+message+"成功！");
        }else{
            return R.error().code(20000).message("添加"+message+"失败！");
        }
    }

    public static R isDelete(boolean b,String message){
        if(b){
            return R.ok().message("删除"+message+"成功！");
        }else{
            return R.error().code(20000).message("删除"+message+"失败！");
        }
    }

    public static R isUpdate(boolean b,String message){
        if(b){
            return R.ok().message("修改"+message+"成功！");
        }else{
            return R.error().code(20000).message("修改"+message+"失败！");
        }
    }
}
