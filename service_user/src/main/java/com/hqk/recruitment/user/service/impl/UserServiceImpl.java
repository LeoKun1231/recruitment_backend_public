package com.hqk.recruitment.user.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hqk.recruitment.client.CompanyClient;
import com.hqk.recruitment.client.OssClient;
import com.hqk.recruitment.constants.RoleCode;
import com.hqk.recruitment.model.common.Major;
import com.hqk.recruitment.model.user.Role;
import com.hqk.recruitment.model.user.UserMajor;
import com.hqk.recruitment.model.user.UserRole;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.user.listener.MyExcelListener;
import com.hqk.recruitment.user.mapper.UserMapper;
import com.hqk.recruitment.user.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.exception.MyCustomException;
import com.hqk.recruitment.model.user.User;
import com.hqk.recruitment.utils.JwtHelper;
import com.hqk.recruitment.vo.user.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserMajorService userMajorService;

    @Autowired
    private MajorService majorService;

    @Resource
    private CompanyClient companyClient;

    @Resource
    private OssClient ossClient;


    @Override
    public User getByAccount(String account) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("account",account);
        return baseMapper.selectOne(userQueryWrapper);
    }

    /**
     * 通过手机登录
     * @param loginPhnoeVo
     * @return
     */
    @Override
    public Map<String, Object> loginByPhone(LoginPhnoeVo loginPhnoeVo) {
//      1.获取用户手机号和验证码
        String telephone = loginPhnoeVo.getTelephone();
        String code = loginPhnoeVo.getCode();
//      2.进行非空判断
        if(StringUtils.isEmpty(telephone)){
            throw new MyCustomException(20000,"手机号不能为空");
        }
        if(StringUtils.isEmpty(code)){
            throw new MyCustomException(20000,"验证码不能为空");
        }
//      3.验证验证码是否正确
        String auth_code = redisTemplate.opsForValue().get(telephone);
        if(StringUtils.isEmpty(auth_code)|| !auth_code.equals(code)){
            throw new MyCustomException(20000,"验证码错误");
        }
//      4.查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("telephone", telephone);
        User user = baseMapper.selectOne(queryWrapper);
        if(Objects.isNull(user)){
            throw new MyCustomException(20000,"手机号或验证码错误");
        }
//      4.判断用户是否是首次登录
        return this.createUseInfo(user);
    }


    /**
     * 通过帐号登录
     * @param loginAccountVo
     * @return
     */
    @Override
    public Map<String, Object> loginByAccount(LoginAccountVo loginAccountVo) {
        String account = loginAccountVo.getAccount();
        String password = loginAccountVo.getPassword();
        if(StringUtils.isEmpty(account)){
            throw new MyCustomException(20000,"帐号不能为空");
        }
        if(StringUtils.isEmpty(password)){
            throw new MyCustomException(20000,"密码不能为空");
        }
        //验证密码
        //先从数据库中查找对应的密码
        User user = this.getOne(new QueryWrapper<User>().eq("account", account));
        if(Objects.isNull(user)){
            throw new MyCustomException(20000,"帐号或密码错误");
        }
        //比较密码
        boolean matches = bCryptPasswordEncoder.matches(password,user.getPassword());
        if(!matches){
            throw new MyCustomException(20000,"密码错误");
        }
        return this.createUseInfo(user);
    }

    @Override
    public UserVo getUserInfoById(Long id) {
        User user = this.getById(id);

        if(Objects.isNull(user)){
            return null;
        }

        UserVo userVo = new UserVo();

        UserMajor one = userMajorService.getOne(Wrappers.<UserMajor>lambdaQuery().eq(UserMajor::getUserId, id));
        BeanUtils.copyProperties(user,userVo);

        if(!Objects.isNull(one)){
            userVo.setMajorId(one.getMajorId());
        }

        return userVo;
    }


    /**
     * 判断手机号码是否存在
     * @param telephone
     * @return
     */
    @Override
    public Boolean checkPhone(String telephone) {
        if(StringUtils.isEmpty(telephone)){
            throw  new MyCustomException(20000,"手机号码不能为空");
        }
        User user = this.getOne(new QueryWrapper<User>().eq("telephone", telephone));
        return !Objects.isNull(user);
    }

    /**
     * 重置密码登录
     * @param loginPasswordReset
     * @return
     */
    @Override
    public Map<String, Object> resetPassword(LoginPasswordReset loginPasswordReset) {
        String telephone = loginPasswordReset.getTelephone();
        String code = loginPasswordReset.getCode();
        String password = loginPasswordReset.getPassword();

        if(StringUtils.isEmpty(telephone)){
            throw new MyCustomException(20000,"手机号不能为空");
        }
        if(StringUtils.isEmpty(code)){
            throw new MyCustomException(20000,"验证码不能为空");
        }
        if(StringUtils.isEmpty(password)){
            throw  new MyCustomException(20000,"密码不能为空");
        }

        String redisCode = redisTemplate.opsForValue().get(telephone);
        if(StringUtils.isEmpty(redisCode)|| !redisCode.equals(code)){
            throw new MyCustomException(20000,"验证码错误");
        }

        Boolean checkPhone = this.checkPhone(telephone);
        if(!checkPhone){
            throw new MyCustomException(20000,"该手机号还未绑定");
        }

        User user = this.getOne(new QueryWrapper<User>().eq("telephone", telephone));
        user.setPassword(bCryptPasswordEncoder.encode(password));
        boolean update = this.update(user, new QueryWrapper<User>().eq("telephone", telephone));
        if(!update){
            throw new MyCustomException(20000,"重置失败，请尝试无效后联系客服");
        }

        Map<String, Object> useInfo = this.createUseInfo(user);
        return useInfo;
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @Override
    public boolean deleteUser(Long userId) {
        if(Objects.isNull(userId)){
            throw new MyCustomException(20000,"用户Id不能为空");
        }
        Role role = roleService.getRoleByUserId(userId);
        if(RoleCode.BOSS==role.getId()){
            companyClient.deleteCompanyByUserId(userId);
        }
        return this.removeById(userId);
    }

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    @Override
    public boolean deleteUserByIds(List<Long> ids) {
        if(ids.size()==0){
            throw new MyCustomException(20000,"删除用户数组不能为空");
        }
        return  this.removeByIds(ids);
    }


    /**
     * 搜索管理者用户列表
     * @param userQueryVo
     * @return
     */
    @Override
    public R getAdminAndTeacherList(AdminQueryVo userQueryVo) {
        Integer currentPage = userQueryVo.getCurrentPage();
        Integer pageSize = userQueryVo.getPageSize();
        List<Long> majorId = userQueryVo.getMajorId();

        //如果没有传则传设置默认currentPage=1 pageSize=10
        if(currentPage==null || currentPage<=0){
            currentPage=1;
        }
        if(pageSize==null || pageSize==0){
            pageSize=10;
        }
        Page<User> page=new Page<>(currentPage,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if((!Objects.isNull(majorId) && majorId.size()>0)){
            //admin的负责的专业是全部 也就是id==1
            majorId.add(1L);
            String join = org.apache.commons.lang.StringUtils.join(majorId, ",");
            queryWrapper.inSql("id","SELECT DISTINCT user_id FROM sys_user_major WHERE major_id in ("+join+")");
        }

        if(!StringUtils.isEmpty(userQueryVo.getUserName())){
            queryWrapper.like("user_name",userQueryVo.getUserName());
        }
        if(!StringUtils.isEmpty(userQueryVo.getAccount())){
            queryWrapper.eq("account",userQueryVo.getAccount());
        }
        if(!StringUtils.isEmpty(userQueryVo.getNickName())){
            queryWrapper.like("nick_name",userQueryVo.getNickName());
        }
        if(!StringUtils.isEmpty(userQueryVo.getEmail())){
            queryWrapper.like("email",userQueryVo.getEmail());
        }
        if(!StringUtils.isEmpty(userQueryVo.getTelephone())){
            queryWrapper.like("telephone",userQueryVo.getTelephone());
        }
        if(!Objects.isNull(userQueryVo.getCreateTime())){
            List<String> createTime = userQueryVo.getCreateTime();
            if(!Objects.isNull(createTime.get(0))&&!Objects.isNull(createTime.get(1))){
                String date = createTime.get(0);
                String date1 = createTime.get(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startTime  = simpleDateFormat.parse(date);
                    Date endTime = simpleDateFormat.parse(date1);
                    queryWrapper.between("create_time",startTime,endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!Objects.isNull(userQueryVo.getUpdateTime())){
            List<String> updateTime = userQueryVo.getUpdateTime();
            if(!Objects.isNull(updateTime.get(0))&&!Objects.isNull(updateTime.get(1))){
                String date = updateTime.get(0);
                String date1 = updateTime.get(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startTime =  simpleDateFormat.parse(date);
                    Date endTime = simpleDateFormat.parse(date1);
                    queryWrapper.between("update_time",startTime,endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        //根据RoleId进行查询
        if(!Objects.isNull(userQueryVo.getRoleId())){
            queryWrapper.inSql("id","SELECT user_id from sys_user_role WHERE role_id="+String.valueOf(userQueryVo.getRoleId()));
        }else{
            queryWrapper.inSql("id","SELECT user_id from sys_user_role WHERE role_id="+1+" or role_id="+2);
        }

        queryWrapper.orderByDesc("id");
        List<User> records = this.page(page, queryWrapper).getRecords();
        List<AdminVo> list = new ArrayList<>(records.size());
        for (User record : records) {
            AdminVo adminVo = new AdminVo();
            Role role = roleService.getRoleByUserId(record.getId());
            Map<String, List> majorNamesByUserId = userMajorService.getMajorIdsAndNamesByUserId(record.getId());
            BeanUtils.copyProperties(record,adminVo);
            adminVo.setRoleId(role.getId());
            adminVo.setRoleName(role.getDesc());
            adminVo.setMajorIds((List<Long>) majorNamesByUserId.get("ids"));
            adminVo.setMajorNames((List<String>)majorNamesByUserId.get("names"));
            list.add(adminVo);
        }
        return R.ok().data("records", list).data("totalCount",page.getTotal()).message(null);
    }

    @Override
    public R addAdmin(AdminUpdateVo adminVo) {
        Long roleId = adminVo.getRoleId();
        String account = adminVo.getAccount();
        String password = adminVo.getPassword();
        List<Long> majorIds = adminVo.getMajorIds();
        if(!Objects.isNull(account)){
            User user1 = this.getOne(new QueryWrapper<User>().eq("account", account));
            if(!Objects.isNull(user1)){
                return R.error().message("该帐号已存在，请重新输入");
            }
        }
        if(Objects.isNull(password)){
            adminVo.setPassword(bCryptPasswordEncoder.encode("123456"));
        }else{
            adminVo.setPassword(bCryptPasswordEncoder.encode(password));
        }
        User user = new User();
        BeanUtils.copyProperties(adminVo,user);
        boolean save = this.save(user);
        if(save && !Objects.isNull(roleId)){
            Long userId = user.getId();
            boolean b = userRoleService.AddUserRole(userId, roleId);
            if(roleId==RoleCode.ADMIN){
                List<Long> list = new ArrayList<>();
                list.add(1L);
                userMajorService.addUserMajors(userId,list);
            }
            if(b){
                if(!Objects.isNull(majorIds)){
                   userMajorService.addUserMajors(userId, majorIds);
                }
                return R.ok().message("添加成功");
            }else{
                return R.error().message("添加失败");
            }
        }
        return R.error().message("添加失败");
    }

    @Override
    public R updateUser(AdminUpdateVo adminUpdateVo) {
        List<Long> majorIds = adminUpdateVo.getMajorIds();
        String account = adminUpdateVo.getAccount();
        Long roleId = adminUpdateVo.getRoleId();

        //根据account查询
        User user1 = this.getOne(new QueryWrapper<User>().eq("account", account));
        if(!Objects.isNull(user1)){
            //根据修改的id查询原本的accout
            User user2 = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, adminUpdateVo.getId()));
            if(!account.equals(user2.getAccount())){
                return R.error().message("该帐号已存在，请重新输入");
            }
        }

        User user = new User();
        BeanUtils.copyProperties(adminUpdateVo,user);
        boolean b = this.updateById(user);
        if(!Objects.isNull(roleId)){
            //为角色更新roleId
            userRoleService.AddUserRole(user.getId(), roleId);
            //如果是admin 则添加所有专业
            if(roleId== RoleCode.ADMIN){
                List<Long> list = new ArrayList<>();
                list.add(1L);
                userMajorService.addUserMajors(user.getId(),list);
            }
        }

        if(!Objects.isNull(majorIds)){
            userMajorService.addUserMajors(adminUpdateVo.getId(), majorIds);
            return R.ok().message("修改用户成功");
        }
        if(b){
            return R.ok().message("修改用户成功");
        }else{
            return R.error().message("修改用户失败");
        }
    }

    @Override
    public R getStudentList(StudentQueryVo studentQueryVo) {
        Integer currentPage = studentQueryVo.getCurrentPage();
        Integer pageSize = studentQueryVo.getPageSize();

        log.warn("studentQ"+studentQueryVo);
        List<Long> majorId = studentQueryVo.getMajorId();
        Long userId = studentQueryVo.getUserId();

        //如果没有传则传设置默认currentPage=1 pageSize=10
        if(currentPage==null || currentPage<=0){
            currentPage=1;
        }
        if(pageSize==null || pageSize==0){
            pageSize=10;
        }

        Page<User> page=new Page<>(currentPage,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        List<Long> ids=new ArrayList<>();
        //根据userId判断是admin还是teacher
        if(!Objects.isNull(userId)){
            Role role = roleService.getRoleByUserId(userId);
            //如果是老师只能查询自己相关的学生
            if(role.getId()==RoleCode.TEACHER){
                Map<String, List> map = userMajorService.getMajorIdsAndNamesByUserId(userId);
                ids.addAll(map.get("ids"));
            }
        }

        //判断是否是admin还是teacher 如果是admin ids则为空
        //ids为空进入，或者有搜索条件也进入
        if(ids.size()!=0 || (!Objects.isNull(majorId) && majorId.size()>0)){
            if(!Objects.isNull(majorId) && majorId.size()>0){
                //如果ids之前有值，则清空
                ids.clear();
                ids.addAll(majorId);
            }
            String join = org.apache.commons.lang.StringUtils.join(ids, ",");
            queryWrapper.inSql("id","SELECT DISTINCT user_id FROM sys_user_major WHERE major_id in ("+join+")");
        }

        if(!StringUtils.isEmpty(studentQueryVo.getUserName())){
            queryWrapper.like("user_name",studentQueryVo.getUserName());
        }
        if(!StringUtils.isEmpty(studentQueryVo.getAccount())){
            queryWrapper.eq("account",studentQueryVo.getAccount());
        }
        if(!StringUtils.isEmpty(studentQueryVo.getNickName())){
            queryWrapper.like("nick_name",studentQueryVo.getNickName());
        }
        if(!StringUtils.isEmpty(studentQueryVo.getEmail())){
            queryWrapper.like("email",studentQueryVo.getEmail());
        }
        if(!StringUtils.isEmpty(studentQueryVo.getTelephone())){
            queryWrapper.like("telephone",studentQueryVo.getTelephone());
        }
        if(!Objects.isNull(studentQueryVo.getCreateTime())){
            List<String> createTime = studentQueryVo.getCreateTime();
            if(!Objects.isNull(createTime.get(0))&&!Objects.isNull(createTime.get(1))){
                String date = createTime.get(0);
                String date1 = createTime.get(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startTime = simpleDateFormat.parse(date);
                    Date endTime = simpleDateFormat.parse(date1);
                    queryWrapper.between("create_time",startTime,endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!Objects.isNull(studentQueryVo.getUpdateTime())){
            List<String> updateTime = studentQueryVo.getUpdateTime();
            if(!Objects.isNull(updateTime.get(0))&&!Objects.isNull(updateTime.get(1))){
                String date = updateTime.get(0);
                String date1 = updateTime.get(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startTime = simpleDateFormat.parse(date);
                    Date endTime = simpleDateFormat.parse(date1);
                    queryWrapper.between("update_time",startTime,endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        queryWrapper.orderByDesc("id");
        queryWrapper.inSql("id","SELECT user_id from sys_user_role WHERE role_id="+3);
        List<User> records = this.page(page, queryWrapper).getRecords();
        List<StudentVo> list = new ArrayList<>(records.size());
        for (User record : records) {
            StudentVo studentVo = new StudentVo();
            Major major = userMajorService.getMajorByUserId(record.getId());
            BeanUtils.copyProperties(record,studentVo);
            studentVo.setMajorId(major.getId());
            studentVo.setMajorName(major.getMajorName());
            list.add(studentVo);
        }
        return R.ok().data("records", list).data("totalCount",page.getTotal()).message(null);
    }

    @Override
    public R addStudent(StudentUpdateVo studentUpdateVo) {
        String account = studentUpdateVo.getAccount();
        String password = studentUpdateVo.getPassword();
        Long majorId = studentUpdateVo.getMajorId();
        if(!Objects.isNull(account)){
            User user1 = this.getOne(new QueryWrapper<User>().eq("account", account));
            if(!Objects.isNull(user1)){
                return R.error().message("该帐号已存在，请重新输入");
            }
        }
        if(Objects.isNull(password)){
            studentUpdateVo.setPassword(bCryptPasswordEncoder.encode("123456"));
        }else{
            studentUpdateVo.setPassword(bCryptPasswordEncoder.encode(password));
        }
        User user = new User();
        BeanUtils.copyProperties(studentUpdateVo,user);
        boolean save = this.save(user);
        if(save){
            Long userId = user.getId();
            userMajorService.addUserMajor(userId, majorId);
            boolean b = userRoleService.AddUserRole(userId, RoleCode.STUDENT);
            if(b){
                return R.ok().message("添加学生成功");
            }else{
                return R.error().message("添加学生失败");
            }
        }
        return R.error().message("添加学生失败");
    }

    @Override
    public R updateStudent(StudentUpdateVo studentUpdateVo) {
        Long majorId = studentUpdateVo.getMajorId();
        String account = studentUpdateVo.getAccount();
        //根据account查询
        User user1 = this.getOne(new QueryWrapper<User>().eq("account", account));
        if(!Objects.isNull(user1)){
            //根据修改的id查询原本的accout
            User user2 = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, studentUpdateVo.getId()));
            if(!account.equals(user2.getAccount())){
                return R.error().message("该帐号已存在，请重新输入");
            }
        }
        User user = new User();
        BeanUtils.copyProperties(studentUpdateVo,user);
        boolean b = this.updateById(user);
        if(!Objects.isNull(majorId)){
            userMajorService.addUserMajor(studentUpdateVo.getId(), majorId);
            return R.ok().message("修改学生成功");
        }
        if(b){
            return R.ok().message("修改学生成功");
        }else{
            return R.error().message("修改学生失败");
        }
    }

    @Override
    public boolean MySaveBacth(List<User> list,Long majorId) {
        String encode = bCryptPasswordEncoder.encode("123456");
        List<User> users = list.stream().map(item ->{
            item.setPassword(encode);
            item.setNickName("用户_"+UUID.randomUUID().toString().replaceAll("-", ""));
            return item;
        }).collect(Collectors.toList());
        boolean isSave = this.saveBatch(users);
        if(isSave){
            List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
            List<UserRole> roles = userIds.stream().map(item -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(item);
                userRole.setRoleId(RoleCode.STUDENT);
                return userRole;
            }).collect(Collectors.toList());
            List<UserMajor> majors = userIds.stream().map(item -> {
                UserMajor userMajor = new UserMajor();
                userMajor.setUserId(item);
                userMajor.setMajorId(majorId);
                return userMajor;
            }).collect(Collectors.toList());
            userRoleService.saveBatch(roles);
            userMajorService.saveBatch(majors);
        }
        return isSave;
    }

    @Override
    public R batchAdd(MultipartFile file, Long id) {
        try {
            EasyExcel.read(file.getInputStream(),User.class,new MyExcelListener(this,id)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            return R.error().message("添加学生失败");
        }
        return R.ok().message("添加学生成功");
    }

    @Override
    public R addBoss(BossUpdateVo bossVo) {
        String account = bossVo.getAccount();
        String password = bossVo.getPassword();
        if(!Objects.isNull(account)){
            User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, account));
            if(!Objects.isNull(user)){
                return R.error().message("该帐号已存在，请重新输入");
            }
        }
        if(Objects.isNull(password)){
            bossVo.setPassword(bCryptPasswordEncoder.encode("123456"));
        }else{
            bossVo.setPassword(bCryptPasswordEncoder.encode(password));
        }
        User user = new User();
        BeanUtils.copyProperties(bossVo,user);
        user.setStatus(0L);
        boolean save = this.save(user);
        if(save){
            Long userId = user.getId();
            companyClient.initCompany(userId);
            boolean b = userRoleService.AddUserRole(userId, RoleCode.BOSS);
            if(b){
                return R.ok().message("添加公司成功");
            }else{
                return R.error().message("添加公司成功");
            }
        }
        return R.error().message("添加公司失败");
    }

    @Override
    public R getBossList(BossQueryVo bossQueryVo) {
        Integer currentPage = bossQueryVo.getCurrentPage();
        Integer pageSize = bossQueryVo.getPageSize();

        //如果没有传则传设置默认currentPage=1 pageSize=10
        if(currentPage==null || currentPage<=0){
            currentPage=1;
        }
        if(pageSize==null || pageSize==0){
            pageSize=10;
        }

        Page<User> page=new Page<>(currentPage,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        Long status = bossQueryVo.getStatus();
        if(!Objects.isNull(status)){
            queryWrapper.eq("status",status);
        }

        if(!StringUtils.isEmpty(bossQueryVo.getCompanyName())){
            queryWrapper.like("company_name",bossQueryVo.getCompanyName());
        }

        if(!StringUtils.isEmpty(bossQueryVo.getUserName())){
            queryWrapper.like("user_name",bossQueryVo.getUserName());
        }
        if(!StringUtils.isEmpty(bossQueryVo.getAccount())){
            queryWrapper.eq("account",bossQueryVo.getAccount());
        }
        if(!StringUtils.isEmpty(bossQueryVo.getNickName())){
            queryWrapper.like("nick_name",bossQueryVo.getNickName());
        }
        if(!StringUtils.isEmpty(bossQueryVo.getEmail())){
            queryWrapper.like("email",bossQueryVo.getEmail());
        }
        if(!StringUtils.isEmpty(bossQueryVo.getTelephone())){
            queryWrapper.like("telephone",bossQueryVo.getTelephone());
        }
        if(!Objects.isNull(bossQueryVo.getCreateTime())){
            List<String> createTime = bossQueryVo.getCreateTime();
            if(!Objects.isNull(createTime.get(0))&&!Objects.isNull(createTime.get(1))){
                String date = createTime.get(0);
                String date1 = createTime.get(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startTime = simpleDateFormat.parse(date);
                    Date endTime = simpleDateFormat.parse(date1);
                    queryWrapper.between("create_time",startTime,endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!Objects.isNull(bossQueryVo.getUpdateTime())){
            List<String> updateTime = bossQueryVo.getUpdateTime();
            if(!Objects.isNull(updateTime.get(0))&&!Objects.isNull(updateTime.get(1))){
                String date = updateTime.get(0);
                String date1 = updateTime.get(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startTime = simpleDateFormat.parse(date);
                    Date endTime = simpleDateFormat.parse(date1);
                    queryWrapper.between("update_time",startTime,endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        queryWrapper.orderByDesc("id");
        queryWrapper.inSql("id","SELECT user_id from sys_user_role WHERE role_id="+4);
        List<User> records = this.page(page, queryWrapper).getRecords();
        List<BossVo> list = new ArrayList<>(records.size());
        for (User record : records) {
            BossVo bossVo = new BossVo();
            BeanUtils.copyProperties(record,bossVo);
            list.add(bossVo);
        }
        return R.ok().data("records", list).data("totalCount",page.getTotal()).message(null);
    }

    @Override
    public R updateBoss(BossUpdateVo bossVo) {
        String account = bossVo.getAccount();
        //根据account查询
        User user1 = this.getOne(new QueryWrapper<User>().eq("account", account));
        if(!Objects.isNull(user1)){
            //根据修改的id查询原本的accout
            User user2 = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, bossVo.getId()));
            if(!account.equals(user2.getAccount())){
                return R.error().message("该帐号已存在，请重新输入");
            }
        }
        User user = new User();
        BeanUtils.copyProperties(bossVo,user);
        Long id = bossVo.getId();
        User user2 = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, id).select(User::getCompanyName, User::getCompanyId));
        String companyName = user2.getCompanyName();
        String companyId = user2.getCompanyId();
        //判断是否更新公司名 如果有则更新所有公司相同的人的公司名
        if (!companyName.equals(bossVo.getCompanyName())){
            this.update(Wrappers.<User>lambdaUpdate().set(User::getCompanyName,bossVo.getCompanyName()).eq(User::getCompanyId,companyId));
        }
        boolean b = this.updateById(user);
        if(b){
            return R.ok().message("修改公司成功");
        }else{
            return R.error().message("修改公司失败");
        }
    }

    @Override
    public R changeActive(Long id) {
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, id));
        Integer active = user.getActive();
        log.warn("active"+active);
        user.setActive(active==0?1:0);
        boolean b = this.updateById(user);
        if(b){
            return R.ok().message(null);
        }else{
            return R.ok().message("修改状态失败");
        }
    }

    @Override
    public boolean changeCompanyStatus(Long id) {
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, id));
        user.setStatus(1L);
        return this.updateById(user);
    }

    @Override
    public R getStatus(Long id) {
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, id));
        return R.ok().message(null).data("status",user.getStatus()).data("reason",user.getReason());
    }

    @Override
    public R checkCompany(Map map) {
        Integer id = (Integer) map.get("id");
        String reason = (String) map.get("reason");
        boolean isSuccess = (boolean) map.get("isSuccess");
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, id));

        if(isSuccess){
            user.setStatus(2L);
            user.setActive(1);
        }else{
            user.setStatus(3L);
            user.setReason(reason);
        }
        boolean b = this.updateById(user);
        if(b){
            return R.ok().message("操作成功");
        }else{
            return R.error().message("操作失败");
        }
    }

    @Override
    public R addHR(HrUpdateVo hrUpdateVo,String authorization) {
        String account = hrUpdateVo.getAccount();
        String password = hrUpdateVo.getPassword();
        if(!Objects.isNull(account)){
            User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, account));
            if(!Objects.isNull(user)){
                return R.error().message("该帐号已存在，请重新输入");
            }
        }
        if(Objects.isNull(password)){
            hrUpdateVo.setPassword(bCryptPasswordEncoder.encode("123456"));
        }else{
            hrUpdateVo.setPassword(bCryptPasswordEncoder.encode(password));
        }
        String token = this.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        User user1 = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, userId).select(User::getCompanyName,User::getCompanyId));
        User user = new User();
        BeanUtils.copyProperties(hrUpdateVo,user);
        log.warn("userId"+user1.getCompanyId());
        user.setCompanyId(user1.getCompanyId());
        user.setCompanyName(user1.getCompanyName());
        boolean save = this.save(user);
        if(save){
            boolean b = userRoleService.AddUserRole(user.getId(), RoleCode.HR);
            if(b){
                return R.ok().message("添加HR成功");
            }else{
                return R.error().message("添加HR成功");
            }
        }
        return R.error().message("添加HR失败");
    }

    @Override
    public R updateHR(HrUpdateVo hrUpdateVo,String authorization) {
        String account = hrUpdateVo.getAccount();
        //根据account查询
        User user1 = this.getOne(new QueryWrapper<User>().eq("account", account));
        if(!Objects.isNull(user1)){
            //根据修改的id查询原本的accout
            User user2 = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, hrUpdateVo.getId()));
            if(!account.equals(user2.getAccount())){
                return R.error().message("该帐号已存在，请重新输入");
            }
        }
        User user = new User();
        BeanUtils.copyProperties(hrUpdateVo,user);
        boolean b = this.updateById(user);
        if(b){
            return R.ok().message("修改HR成功");
        }else{
            return R.error().message("修改HR失败");
        }
    }

    @Override
    public R getHRList(HrQueryVo hrQueryVo,String authorization) {
        String token = this.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        String companyName = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, userId).select(User::getCompanyName)).getCompanyName();
        Integer currentPage = hrQueryVo.getCurrentPage();
        Integer pageSize = hrQueryVo.getPageSize();

        //如果没有传则传设置默认currentPage=1 pageSize=10
        if(currentPage==null || currentPage<=0){
            currentPage=1;
        }
        if(pageSize==null || pageSize==0){
            pageSize=10;
        }

        Page<User> page=new Page<>(currentPage,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_name",companyName);
        queryWrapper.ne("id",userId);

        if(!StringUtils.isEmpty(hrQueryVo.getUserName())){
            queryWrapper.like("user_name",hrQueryVo.getUserName());
        }
        if(!StringUtils.isEmpty(hrQueryVo.getAccount())){
            queryWrapper.eq("account",hrQueryVo.getAccount());
        }
        if(!StringUtils.isEmpty(hrQueryVo.getNickName())){
            queryWrapper.like("nick_name",hrQueryVo.getNickName());
        }
        if(!StringUtils.isEmpty(hrQueryVo.getEmail())){
            queryWrapper.like("email",hrQueryVo.getEmail());
        }
        if(!StringUtils.isEmpty(hrQueryVo.getTelephone())){
            queryWrapper.like("telephone",hrQueryVo.getTelephone());
        }
        if(!Objects.isNull(hrQueryVo.getCreateTime())){
            List<String> createTime = hrQueryVo.getCreateTime();
            if(!Objects.isNull(createTime.get(0))&&!Objects.isNull(createTime.get(1))){
                String date = createTime.get(0);
                String date1 = createTime.get(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startTime = simpleDateFormat.parse(date);
                    Date endTime = simpleDateFormat.parse(date1);
                    queryWrapper.between("create_time",startTime,endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!Objects.isNull(hrQueryVo.getUpdateTime())){
            List<String> updateTime = hrQueryVo.getUpdateTime();
            if(!Objects.isNull(updateTime.get(0))&&!Objects.isNull(updateTime.get(1))){
                String date = updateTime.get(0);
                String date1 = updateTime.get(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startTime = simpleDateFormat.parse(date);
                    Date endTime = simpleDateFormat.parse(date1);
                    queryWrapper.between("update_time",startTime,endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        queryWrapper.orderByDesc("id");
        queryWrapper.inSql("id","SELECT user_id from sys_user_role WHERE role_id="+RoleCode.HR);
        List<User> records = this.page(page, queryWrapper).getRecords();
        List<BossVo> list = new ArrayList<>(records.size());
        for (User record : records) {
            BossVo bossVo = new BossVo();
            BeanUtils.copyProperties(record,bossVo);
            list.add(bossVo);
        }
        return R.ok().data("records", list).data("totalCount",page.getTotal()).message(null);
    }

    @Override
    public List<Long> getActiveCompany() {
        return this.list(Wrappers.<User>lambdaQuery()
                        .eq(User::getActive, 1)
                        .select(User::getId))
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList());
    }

    @Override
    public R resetPasswordByPassword(PasswordResetByPassword password, String authorization) {
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);

        String oldPassword = password.getOldPassword();
        String newPassword = password.getNewPassword();

        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, userId));
        boolean matches = bCryptPasswordEncoder.matches(oldPassword,user.getPassword());
        if(!matches){
            return R.error().message("原来的密码有误");
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        boolean b = this.updateById(user);
        return R.ok().message("修改密码成功！");
    }

    @Override
    public R resetPasswordByTelephone(LoginPasswordReset loginPasswordReset, String authorization) {
        String telephone = loginPasswordReset.getTelephone();
        String code = loginPasswordReset.getCode();
        String password = loginPasswordReset.getPassword();

        if(StringUtils.isEmpty(telephone)){
            throw new MyCustomException(20000,"手机号不能为空");
        }
        if(StringUtils.isEmpty(code)){
            throw new MyCustomException(20000,"验证码不能为空");
        }
        if(StringUtils.isEmpty(password)){
            throw  new MyCustomException(20000,"密码不能为空");
        }

        String redisCode = redisTemplate.opsForValue().get(telephone);
        if(StringUtils.isEmpty(redisCode)|| !redisCode.equals(code)){
            throw new MyCustomException(20000,"验证码错误");
        }

        Boolean checkPhone = this.checkPhone(telephone);
        if(!checkPhone){
            throw new MyCustomException(20000,"该手机号还未绑定");
        }

        User user = this.getOne(new QueryWrapper<User>().eq("telephone", telephone));
        user.setPassword(bCryptPasswordEncoder.encode(password));
        boolean update = this.update(user, new QueryWrapper<User>().eq("telephone", telephone));
        if(!update){
            return R.error().message("重置失败，请尝试无效后联系客服");
        }

        return R.ok().message("修改密码成功！");

    }

    @Override
    public R editUser(EditUserVo editUserVo,String authorization) {

        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        User user = new User();
        BeanUtils.copyProperties(editUserVo,user);
        user.setId(userId);
        boolean b = this.updateById(user);
        if(b){
            return R.ok().message("修改成功").data("data",createUseInfo(user));
        }else{
            return R.error().message("修改失败");
        }
    }

    @Override
    public R updateAvatar(MultipartFile file, String authorization) {
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, userId));

        String url = ossClient.uploadUrl(file);
        user.setAvatar(url);
        boolean b = this.updateById(user);
        if(b){
            return R.ok().message(null).data("url",url);
        }else{
            return R.error().message("修改失败");
        }
    }

    @Override
    public R updateTempAvatar(MultipartFile file, String authorization) {
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        String url = ossClient.uploadUrl(file);
        return R.ok().message(null).data("url",url);
    }

    @Override
    public boolean closeCompany(Long id) {
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, id));
        Integer active = user.getActive();
        user.setActive(0);
        return this.updateById(user);
    }


    String getToken(String authorization){
        if(authorization.length()<=7){
            throw new MyCustomException(20000,"token不存在");
        }
        String token= authorization.substring(7, authorization.length());
        boolean isNotExpire = JwtHelper.checkToken(token);
        if(!isNotExpire){
            throw new MyCustomException(20000,"token过期");
        }
        return token;
    }

    /**
     * 返回给前端的登录用户信息
     * @param user
     * @return
     */
    private Map<String,Object> createUseInfo(User user){
        Long userId = user.getId();
        String account= user.getAccount();
        String nickName = user.getNickName();
        String userName = user.getUserName();
        String avatar = user.getAvatar();
        String token = JwtHelper.createToken(userId, account);
        //将token放到redis中
        redisTemplate.opsForValue().set(String.valueOf(userId),token);
        Role role = roleService.getRoleByUserId(userId);
        Long id = role.getId();

//       5.将数据放回给前端
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",userId);
        map.put("nickName",nickName);
        map.put("userName",userName);
        map.put("avatar",avatar);
        map.put("email",user.getEmail());
        map.put("telephone",user.getTelephone());

        if(role.getId()==3){
            UserMajor userMajor = userMajorService.getOne(Wrappers.<UserMajor>lambdaQuery().eq(UserMajor::getUserId, userId));
            if(!Objects.isNull(userMajor)){
                Major major = majorService.getOne(Wrappers.<Major>lambdaQuery().eq(Major::getId, userMajor.getMajorId()));
                map.put("majorId",userMajor.getMajorId());
                map.put("marjorName",major.getMajorName());
            }
        }
        map.put("roleId",id);
        if(RoleCode.HR==id || RoleCode.BOSS==id){
            String companyId = user.getCompanyId();
            map.put("companyId",companyId);
        }
        map.put("token",token);
        return map;
    }
}
