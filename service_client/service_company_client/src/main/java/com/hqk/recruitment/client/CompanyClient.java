package com.hqk.recruitment.client;


import com.hqk.recruitment.model.company.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "service-company")
@RequestMapping("/company")
public interface CompanyClient {

    @GetMapping("/init/{id}")
    public boolean initCompany(@PathVariable("id") Long id);

    @GetMapping("/companyName/{id}")
    public  String getCompanyNameByUserId(@PathVariable("id") Long id);

    @DeleteMapping("/company/delete/{id}")
    public boolean deleteCompanyByUserId(@PathVariable("id") Long id);
}
