package com.ly.ttd.biz.feature.dem.sweb.service.user;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author yong.li
 * @since 2026/5/8 14:47
 */
public interface OssService {

    // 上传文件
    String upload(String uid, MultipartFile file);

    // 删除文件
    Boolean delete(String fileId);

    // 根据 fileId 查询文件路径
    String getFilePath(String fileId);

    // 根据 uid 查询文件ID列表
    java.util.List<String> getFileIdsByUid(String uid);

}
