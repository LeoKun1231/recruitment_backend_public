package com.hqk.recruitment.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.model.user.User;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.user.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
public interface UserService extends IService<User> {

    User getByAccount(String account);

    Map<String, Object> loginByPhone(LoginPhnoeVo loginPhnoeVo);

    Map<String, Object> loginByAccount(LoginAccountVo loginAccountVo);


    UserVo getUserInfoById(Long id);

    Boolean checkPhone(String telephone);


    Map<String, Object>  resetPassword(LoginPasswordReset loginPasswordReset);

    boolean deleteUser(Long userId);

    boolean deleteUserByIds(List<Long> ids);

    R getAdminAndTeacherList(AdminQueryVo adminQueryVo) throws ParseException;

    R addAdmin(AdminUpdateVo adminVo);

    R updateUser(AdminUpdateVo adminUpdateVo);

    R getStudentList(StudentQueryVo studentQueryVo);

    R addStudent(StudentUpdateVo studentUpdateVo);

    R updateStudent(StudentUpdateVo studentUpdateVo);

    boolean MySaveBacth(List<User> list,Long majorId);

    R batchAdd(MultipartFile file, Long id);

    R addBoss(BossUpdateVo bossVo);

    R getBossList(BossQueryVo bossQueryVo);

    R updateBoss(BossUpdateVo bossVo);

    R changeActive(Long id);

    boolean changeCompanyStatus(Long id);

    R getStatus(Long id);

    R checkCompany(Map map);

    R addHR(HrUpdateVo hrUpdateVo,String authorization);

    R updateHR(HrUpdateVo hrUpdateVo,String authorization);

    R getHRList(HrQueryVo hrQueryVo,String authorization);

    List<Long> getActiveCompany();

    R resetPasswordByPassword(PasswordResetByPassword password, String authorization);

    R resetPasswordByTelephone(LoginPasswordReset loginPasswordReset, String authorization);

    R editUser(EditUserVo editUserVo,String authorization);

    R updateAvatar(MultipartFile file, String authorization);

    R updateTempAvatar(MultipartFile file, String authorization);

    boolean closeCompany(Long id);

}
