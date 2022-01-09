package com.wangxt.oss.core.service;

import com.wangxt.oss.core.config.IOSSConfig;
import com.wangxt.oss.core.pojo.CopyObjectResult;
import com.wangxt.oss.core.pojo.OSSObject;
import com.wangxt.oss.core.pojo.ObjectMetadata;
import com.wangxt.oss.core.pojo.PutObjectResult;

import java.io.InputStream;

/**
 * oss 实现方法抽象类
 */
public interface IOSS {

    /**
     * 获取配置参数类
     */
    IOSSConfig getOssConfig();

    /**
     * 上传文件方法
     *
     * @param finalKey oss文件路径
     * @param is       文件流
     * @return PutObjectResult
     */
    PutObjectResult putFile(String finalKey, InputStream is);

    /**
     * 上传文件方法
     *
     * @param finalKey    oss文件路径
     * @param is          文件流
     * @param contentType 文件类型  eg:image/png
     * @return PutObjectResult
     */
    PutObjectResult putFile(String finalKey, InputStream is, String contentType);

    /**
     * 上传文件方法
     *
     * @param finalKey oss文件路径
     * @param byts     文件字节数组
     * @return PutObjectResult
     */
    PutObjectResult putFile(String finalKey, byte[] byts);

    /**
     * 上传文件方法
     *
     * @param finalKey    oss文件路径
     * @param byts        文件字节数组
     * @param contentType 文件类型  eg:image/png
     * @return PutObjectResult
     */
    PutObjectResult putFile(String finalKey, byte[] byts, String contentType);

    /**
     * 上传文件方法
     *
     * @param finalKey oss文件路径
     * @param is       文件流
     * @param meta     存储对象元数据
     * @return PutObjectResult
     */
    PutObjectResult putFile(String finalKey, InputStream is, ObjectMetadata meta);

    /**
     * 上传文件方法
     *
     * @param finalKey oss文件路径
     * @param byts     文件字节数组
     * @param meta     存储对象元数据
     * @return PutObjectResult
     */
    PutObjectResult putFile(String finalKey, byte[] byts, ObjectMetadata meta);

    /**
     * 获取文件
     * @param finalKey oss文件路径
     * @return 文件对象
     */
    OSSObject getFile(String finalKey);

    /**
     * 获取文件
     * @param finalKey oss文件路径
     * @param rangeFrom 文件流起始位置
     * @param rangeTo 文件流终止位置
     * @return 文件对象
     */
    OSSObject getFile(String finalKey, long rangeFrom, long rangeTo);

    /**
     * 获取文件
     * @param finalKey oss文件路径
     * @param style 文件格式
     * @return 文件对象
     */
    OSSObject getFile(String finalKey, String style);

    /**
     * 获取文件元数据
     * @param finalKey oss文件路径
     * @return 文件对象元数据
     */
    ObjectMetadata getObjectMetadata(String finalKey);

    /**
     * 复制文件
     * @param fromKey 源文件路径
     * @param toKey 目标文件路径
     * @return 复制操作结果
     */
    CopyObjectResult copyFile(String fromKey, String toKey);

    /**
     * 复制文件
     * @param fromKey 源文件路径
     * @param toKey 目标文件路径
     * @return 复制操作结果
     */
    CopyObjectResult copyFileNoCatch(String fromKey, String toKey) throws Exception;

    /**
     * 复制文件到其它桶
     * @param fromKey 源文件路径
     * @param targetBucket 目标桶名称
     * @param toKey 目标文件路径
     * @return 复制操作结果
     */
    CopyObjectResult copyFile2Bucket(String fromKey, IOSS targetBucket, String toKey);

    /**
     * 复制文件并指定目标文件元数据信息
     * @param fromKey 源文件路径
     * @param toKey 目标文件路径
     * @param targetFileObjectMetadata 目标文件元数据信息
     * @return 复制操作结果
     */
    CopyObjectResult copyFile(String fromKey, String toKey, ObjectMetadata targetFileObjectMetadata);

    /**
     * 复制文件到其它桶，并指定目标文件元数据信息
     * @param fromKey 源文件路径
     * @param targetBucket 目标桶名称
     * @param toKey 目标文件路径
     * @param targetFileObjectMetadata 目标文件元数据信息
     * @return 复制操作结果
     */
    CopyObjectResult copyFile2Bucket(String fromKey, IOSS targetBucket, String toKey, ObjectMetadata targetFileObjectMetadata);
}
