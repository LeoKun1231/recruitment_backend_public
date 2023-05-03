package com.hqk.recruitment.client;


import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.user.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(value = "service-user")
public interface UserClient {

    @GetMapping("/acl/user/{id}")
    public UserVo getUserInfo(@PathVariable Long id);

    @GetMapping("/acl/getRole/{id}")
    public String getRoleName(Long userId);

    @GetMapping("/acl/boss/changeStatus/{id}")
    public boolean changeCompanyStatus(@PathVariable("id") Long id);

    @GetMapping("/acl/boss/getActiveCompany")
    public List<Long> getActiveCompany();

    @GetMapping("/acl/major/{id}")
    public String getMajorNameById(@PathVariable("id") Long id);


    @PutMapping("/acl/boss/closeCompany/{id}")
    public boolean closeCompany(@PathVariable("id") Long id);
}
