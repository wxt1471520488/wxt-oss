package com.wangxt.oss.core.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.wangxt.oss.core.config.IOSSConfig;
import com.wangxt.oss.core.pojo.*;
import com.wangxt.oss.core.pojo.CopyObjectResult;
import com.wangxt.oss.core.pojo.OSSObject;
import com.wangxt.oss.core.pojo.OSSObjectSummary;
import com.wangxt.oss.core.pojo.ObjectMetadata;
import com.wangxt.oss.core.pojo.PutObjectResult;
import com.wangxt.oss.core.service.IOSS;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author wangxt
 * @description 阿里云的OSS
 * @date 2022/3/2 10:01
 **/
public class AliOss implements IOSS {

    private OSSClient ossClient;

    private final IOSSConfig ossConfig;

    /**
     * 获取配置参数类
     */
    @Override
    public IOSSConfig getOssConfig() {
        return this.ossConfig;
    }

    public AliOss(IOSSConfig config){
        this.ossConfig = config;
        if(Objects.nonNull(config)){
            ossClient = new OSSClient(config.getEndpoint().getHost(), config.getAccessId(), config.getAccessKey());
        }
    }

    /**
     * 上传文件方法
     *
     * @param finalKey oss文件路径
     * @param is       文件流
     * @return PutObjectResult
     */
    @Override
    public PutObjectResult putFile(String finalKey, InputStream is) {
        PutObjectRequest request = new PutObjectRequest(ossConfig.getBucketName(), finalKey, is, null);

        try {
            com.aliyun.oss.model.PutObjectResult putObjectResult = ossClient.putObject(request);
            return new PutObjectResult(putObjectResult.getETag());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传文件方法
     *
     * @param finalKey    oss文件路径
     * @param is          文件流
     * @param contentType 文件类型  eg:image/png
     * @return PutObjectResult
     */
    @Override
    public PutObjectResult putFile(String finalKey, InputStream is, String contentType) {
        com.aliyun.oss.model.ObjectMetadata objectMetadata = new com.aliyun.oss.model.ObjectMetadata();
        objectMetadata.setContentType(contentType);
        PutObjectRequest request = new PutObjectRequest(ossConfig.getBucketName(), finalKey, is, objectMetadata);

        try {
            com.aliyun.oss.model.PutObjectResult putObjectResult = ossClient.putObject(request);
            return new PutObjectResult(putObjectResult.getETag());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传文件方法
     *
     * @param finalKey oss文件路径
     * @param byts     文件字节数组
     * @return PutObjectResult
     */
    @Override
    public PutObjectResult putFile(String finalKey, byte[] byts) {
        return putFile(finalKey, byts, StringUtils.EMPTY);
    }

    /**
     * 上传文件方法
     *
     * @param finalKey    oss文件路径
     * @param byts        文件字节数组
     * @param contentType 文件类型  eg:image/png
     * @return PutObjectResult
     */
    @Override
    public PutObjectResult putFile(String finalKey, byte[] byts, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        return putFile(finalKey, new ByteArrayInputStream(byts), metadata);
    }

    /**
     * 上传文件方法
     *
     * @param finalKey oss文件路径
     * @param is       文件流
     * @param meta     存储对象元数据
     * @return PutObjectResult
     */
    @Override
    public PutObjectResult putFile(String finalKey, InputStream is, ObjectMetadata meta) {
        return putFile(finalKey, is, meta);
    }

    /**
     * 上传文件方法
     *
     * @param finalKey oss文件路径
     * @param bytes     文件字节数组
     * @param meta     存储对象元数据
     * @return PutObjectResult
     */
    @Override
    public PutObjectResult putFile(String finalKey, byte[] bytes, ObjectMetadata meta) {
        return putFile(finalKey, new ByteArrayInputStream(bytes), meta);
    }

    /**
     * 获取文件
     * @param finalKey oss文件路径
     * @return 文件对象
     */
    @Override
    public OSSObject getFile(String finalKey) {
        return getFile(finalKey, 0 ,0);
    }

    /**
     * 获取文件
     * @param finalKey oss文件路径
     * @param rangeFrom 文件流起始位置
     * @param rangeTo 文件流终止位置
     * @return 文件对象
     */
    @Override
    public OSSObject getFile(String finalKey, long rangeFrom, long rangeTo) {
        GetObjectRequest request = new GetObjectRequest(ossConfig.getBucketName(), finalKey);
        if(rangeFrom > 0 && rangeTo > 0){
            request.setRange(rangeFrom, rangeTo);
        }

        com.aliyun.oss.model.OSSObject object = ossClient.getObject(request);
        if(Objects.nonNull(object)){
            return new OSSObject(object.getObjectContent());
        }

        return null;
    }

    /**
     * 获取文件
     * @param finalKey oss文件路径
     * @param style 文件格式
     * @return 文件对象
     */
    @Override
    public OSSObject getFile(String finalKey, String style) {
        GetObjectRequest request = new GetObjectRequest(ossConfig.getBucketName(), finalKey);
        if(StringUtils.isNotBlank(style)){
            request.setProcess(style);
        }

        com.aliyun.oss.model.OSSObject object = ossClient.getObject(request);
        if(Objects.nonNull(object)){
            return new OSSObject(object.getObjectContent());
        }
        return null;
    }

    /**
     * 获取文件元数据
     * @param finalKey oss文件路径
     * @return 文件对象元数据
     */
    @Override
    public ObjectMetadata getObjectMetadata(String finalKey) {
        com.aliyun.oss.model.ObjectMetadata objectMetadata = ossClient.getObjectMetadata(ossConfig.getBucketName(), finalKey);
        return new ObjectMetadata(objectMetadata.getETag(), objectMetadata.getContentLength(), LocalDateTime.now(), objectMetadata.getContentType(), objectMetadata.getUserMetadata(), new HashMap<>());
    }

    /**
     * 复制文件
     * @param fromKey 源文件路径
     * @param toKey 目标文件路径
     * @return 复制操作结果
     */
    @Override
    public CopyObjectResult copyFile(String fromKey, String toKey) {
        return copyFile(fromKey, toKey, new ObjectMetadata());
    }

    /**
     * 复制文件
     * @param fromKey 源文件路径
     * @param toKey 目标文件路径
     * @return 复制操作结果
     */
    @Override
    public CopyObjectResult copyFileNoCatch(String fromKey, String toKey) throws Exception {
        return copyFile(fromKey, toKey);
    }

    /**
     * 复制文件到其它桶
     * @param fromKey 源文件路径
     * @param targetBucket 目标桶名称
     * @param toKey 目标文件路径
     * @return 复制操作结果
     */
    @Override
    public CopyObjectResult copyFile2Bucket(String fromKey, IOSS targetBucket, String toKey) {
        return copyFile2Bucket(fromKey, targetBucket, toKey);
    }

    /**
     * 复制文件并指定目标文件元数据信息
     * @param fromKey 源文件路径
     * @param toKey 目标文件路径
     * @param targetFileObjectMetadata 目标文件元数据信息
     * @return 复制操作结果
     */
    @Override
    public CopyObjectResult copyFile(String fromKey, String toKey, ObjectMetadata targetFileObjectMetadata) {
        CopyObjectRequest request = new CopyObjectRequest(ossConfig.getBucketName(), fromKey, ossConfig.getBucketName(), toKey);

        com.aliyun.oss.model.ObjectMetadata objectMetadata = new com.aliyun.oss.model.ObjectMetadata();
        objectMetadata.setContentType(targetFileObjectMetadata.getContentType());
        objectMetadata.setContentLength(targetFileObjectMetadata.getSize());
        objectMetadata.setContentMD5(targetFileObjectMetadata.getETag());
        request.setNewObjectMetadata(objectMetadata);

        com.aliyun.oss.model.CopyObjectResult copyObjectResult = ossClient.copyObject(request);
        return new CopyObjectResult(copyObjectResult.getETag());
    }

    /**
     * 复制文件到其它桶，并指定目标文件元数据信息
     * @param fromKey 源文件路径
     * @param targetBucket 目标桶名称
     * @param toKey 目标文件路径
     * @param targetFileObjectMetadata 目标文件元数据信息
     * @return 复制操作结果
     */
    @Override
    public CopyObjectResult copyFile2Bucket(String fromKey, IOSS targetBucket, String toKey, ObjectMetadata targetFileObjectMetadata) {
        CopyObjectRequest request = new CopyObjectRequest(ossConfig.getBucketName(), fromKey, targetBucket.getOssConfig().getBucketName(), toKey);

        com.aliyun.oss.model.ObjectMetadata objectMetadata = new com.aliyun.oss.model.ObjectMetadata();
        objectMetadata.setContentType(targetFileObjectMetadata.getContentType());
        objectMetadata.setContentLength(targetFileObjectMetadata.getSize());
        objectMetadata.setContentMD5(targetFileObjectMetadata.getETag());
        request.setNewObjectMetadata(objectMetadata);

        com.aliyun.oss.model.CopyObjectResult copyObjectResult = ossClient.copyObject(request);
        return new CopyObjectResult(copyObjectResult.getETag());
    }

    /**
     * 删除文件对象
     * @param finalKey oss路径
     * @return true 删除成功
     */
    @Override
    public boolean delFile(String finalKey) {
        VoidResult voidResult = ossClient.deleteObject(ossConfig.getBucketName(), finalKey);
        return true;
    }

    /**
     * 获取文件下载地址
     * @param finalKey 文件oss路径
     * @param expiration 过期时间(单位：分钟)
     * @return 下载地址
     */
    @Override
    public String getDownloadExpUrl(String finalKey, int expiration) {
        return getDownloadExpUrl(finalKey, new Date(System.currentTimeMillis() + expiration * 60 * 1000L));
    }

    /**
     * 获取文件下载地址
     * @param finalKey 文件oss路径
     * @param expiration 到期时间
     * @return 下载地址
     */
    @Override
    public String getDownloadExpUrl(String finalKey, Date expiration) {
        URL url = ossClient.generatePresignedUrl(ossConfig.getBucketName(), finalKey, expiration);
        return url.getPath();
    }

    /**
     * 获取文件展示地址
     * @param finalKey 文件路径
     * @return 展示地址
     */
    @Override
    public String getPathUrl(String finalKey) {
        return String.format("//%s/%s", this.getOssConfig().getCdnEndpoint().getHost(), finalKey);
    }

    /**
     * 获取指定前缀的文件列表
     * @param bTypeName 桶路径
     * @param prefix 前缀
     * @return 文件列表
     */
    @Override
    public List<OSSObjectSummary> listObjects(String bTypeName, String prefix) {
        String endPrefix = bTypeName + "/" + prefix;
        String nextMarker = null;
        ObjectListing objectListing;
        ListObjectsRequest lsObjReq = new ListObjectsRequest(this.ossConfig.getBucketName());
        lsObjReq.setPrefix(endPrefix);
        List<OSSObjectSummary> lsSums = new ArrayList<>();

        do {
            objectListing = this.ossClient.listObjects(lsObjReq.withMarker(nextMarker).withMaxKeys(30));
            nextMarker = objectListing.getNextMarker();
            List<com.aliyun.oss.model.OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            for (com.aliyun.oss.model.OSSObjectSummary ossObjectSummary : objectSummaries) {
                OSSObjectSummary summary = new OSSObjectSummary();
                summary.setETag(ossObjectSummary.getETag());
                summary.setSize(ossObjectSummary.getSize());
                summary.setLastModified(LocalDateTime.now());
                summary.setFileKey(ossObjectSummary.getKey());
                lsSums.add(summary);
            }
        } while (objectListing.isTruncated());

        return lsSums;
    }
}
