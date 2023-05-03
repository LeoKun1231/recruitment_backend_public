package com.hqk.recruitment.oss.service;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import com.hqk.recruitment.oss.properties.OssProperties;
import com.hqk.recruitment.result.R;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OssService {

    @Autowired
    private OssProperties ossProperties;

    public R upload(MultipartFile file) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossProperties.getBucketname();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            String originFileName=filename;
            //生成随机唯一值，使用uuid，添加到文件名称里面
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            filename = uuid+filename;
            //按照当前日期，创建文件夹，上传到创建文件夹里面
            //  2021/02/02/01.jpg
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            filename = timeUrl+"/"+filename;
            //调用方法实现上传
            ossClient.putObject(bucketName, filename, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //上传之后文件路径
            String url = "https://"+bucketName+"."+endpoint+"/"+filename;
            //返回
            Map map = new HashMap();
            map.put("url",url);
            map.put("fileName",originFileName);
            return R.ok().message("上传成功").data(map);
        } catch (Exception ce) {
            System.out.println("Error Message:" + ce.getMessage());
            // 关闭OSSClient。
            ossClient.shutdown();
            return null;
        }
    }


    public String uploadReturnUrl(MultipartFile file) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossProperties.getBucketname();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            String originFileName=filename;
            //生成随机唯一值，使用uuid，添加到文件名称里面
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            filename = uuid+filename;

            //按照当前日期，创建文件夹，上传到创建文件夹里面
            //  2021/02/02/01.jpg
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            filename = timeUrl+"/"+filename;
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
//            metadata.setObjectAcl(CannedAccessControlList.PublicRead);
//            //缓存，可以预览
//            metadata.setCacheControl("no-cache");
//            metadata.setHeader("Pragma", "no-cache");
            //设置为公共读（可以自己设置权限，官方推荐私有，不过我只是个人测试）
//            metadata.setObjectAcl(CannedAccessControlList.Private);
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
//            metadata.setContentDisposition("inline;filename=" + filename);
            //调用方法实现上传
            ossClient.putObject(bucketName, filename, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //上传之后文件路径
            String url = "https://"+bucketName+"."+endpoint+"/"+filename;
            //返回
            return url;
        } catch (Exception ce) {
            System.out.println("Error Message:" + ce.getMessage());
            // 关闭OSSClient。
            ossClient.shutdown();
            return null;
        }
    }


}
