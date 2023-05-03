package com.hqk.recruitment.user.controller.roleController;

import com.hqk.recruitment.result.R;
import com.hqk.recruitment.user.service.UserService;
import com.hqk.recruitment.vo.user.StudentQueryVo;
import com.hqk.recruitment.vo.user.StudentUpdateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/acl/student")
public class StudentController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('admin','teacher')")
    public R addStudent(@RequestBody StudentUpdateVo studentUpdateVo){
        return userService.addStudent(studentUpdateVo);
    }


    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('admin','teacher')")
    public R updateStudent(@RequestBody StudentUpdateVo studentUpdateVo){
        return userService.updateStudent(studentUpdateVo);
    }

    /**
     * 获取学生列表
     * @param studentQueryVo
     * @return
     */
    @PostMapping("/list")
    public R getStudentList(@RequestBody StudentQueryVo studentQueryVo) throws ParseException {
        return userService.getStudentList(studentQueryVo);
    }

    /**
     * 批量添加学生
     * @param file
     * @return
     */
    @PostMapping("/batchAdd/{id}")
    @PreAuthorize("hasAnyRole('admin','teacher')")
    public R batchAdd(@RequestParam("file")MultipartFile file,@PathVariable Long id) {
        return userService.batchAdd(file,id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('admin','teacher')")
    public R deleteStudentById(@PathVariable Long id){
        boolean b = userService.removeById(id);
        if(b){
            return R.ok().message("删除学生成功");
        }else{
            return R.error().message("删除学生失败");
        }
    }

    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyRole('admin','teacher')")
    public R batchDeleteStudent(@RequestBody List<Long> ids){
        boolean b = userService.deleteUserByIds(ids);
        if(b){
            return R.ok().message("删除学生成功");
        }else{
            return R.error().message("删除学生失败");
        }
    }
}
