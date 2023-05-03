package com.hqk.recruitment.common.service;

import com.hqk.recruitment.common.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 组织架构表 服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-16
 */
public interface DictService extends IService<Dict> {

     void download(HttpServletResponse httpResponse) throws IOException;

     void upload(MultipartFile file);
}
