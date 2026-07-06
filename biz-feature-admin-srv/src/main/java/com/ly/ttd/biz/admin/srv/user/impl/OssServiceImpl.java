package com.ly.ttd.biz.admin.srv.user.impl;

import com.ly.ttd.biz.admin.srv.user.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * OSS 文件上传服务实现
 *
 * @author yong.li
 * @since 2026/5/8 14:49
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Value("${oss.file.path}")
    private String ossFilePath;

    // 日志文件名
    private static final String LOG_FILENAME = "ossIndex.log";

    // 日期格式化
    private static final DateTimeFormatter DATE_FOLDER_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String upload(String uid, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        try {
            // 1. 生成文件ID：文件名+时间戳的MD5值
            String originalFilename = file.getOriginalFilename();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            String fileId = generateFileId(originalFilename, timestamp);

            // 2. 按年月生成文件夹
            String yearMonth = LocalDate.now().format(DATE_FOLDER_FORMATTER);
            String fileDir = ossFilePath + File.separator + yearMonth;

            // 3. 确保目录存在
            File dir = new File(fileDir);
            if (!dir.exists()) {
                dir.mkdirs();
                log.info("创建目录: {}", fileDir);
            }

            // 4. 获取文件扩展名
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 5. 生成文件路径
            String filePath = fileDir + File.separator + fileId + extension;

            // 6. 保存文件
            File destFile = new File(filePath);
            file.transferTo(destFile);
            log.info("文件上传成功: {} -> {}", originalFilename, filePath);

            // 7. 记录日志
            String fileType = file.getContentType();
            String uploadDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
            appendToIndexLog(uid, fileId, originalFilename, fileType, uploadDate, filePath);

            // 8. 返回文件ID
            return fileId;

        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean delete(String fileId) {
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new IllegalArgumentException("文件ID不能为空");
        }

        try {
            // 1. 从 ossIndex.log 中查找文件路径
            String filePath = findFilePathFromLog(fileId);

            if (filePath == null || filePath.isEmpty()) {
                log.warn("在日志中未找到文件记录: {}", fileId);
                return false;
            }

            // 2. 删除文件
            File file = new File(filePath);
            if (!file.exists()) {
                log.warn("文件不存在: {}", filePath);
                return false;
            }

            if (file.delete()) {
                log.info("文件删除成功: {}, path: {}", fileId, filePath);

                // 3. 从日志中删除对应记录
                removeLogEntry(fileId);

                return true;
            } else {
                log.warn("文件删除失败: {}", filePath);
                return false;
            }

        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件删除失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFilePath(String fileId) {
        return findFilePathFromLog(fileId);
    }

    @Override
    public java.util.List<String> getFileIdsByUid(String uid) {
        java.util.List<String> fileIds = new java.util.ArrayList<>();
        String logPath = ossFilePath + File.separator + LOG_FILENAME;
        File logFile = new File(logPath);

        if (!logFile.exists()) {
            log.warn("日志文件不存在: {}", logPath);
            return fileIds;
        }

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 格式：uid|fileId|原文件名|文件类型|上传日期|文件路径
                String[] parts = line.split("\\|");
                if (parts.length >= 6 && parts[0].equals(uid)) {
                    fileIds.add(parts[1]);  // 添加 fileId
                }
            }
        } catch (IOException e) {
            log.error("读取日志文件失败: {}", e.getMessage(), e);
        }

        log.info("查询 uid={} 的文件列表: {}", uid, fileIds);
        return fileIds;
    }

    /**
     * 生成文件ID：文件名+时间戳的MD5值
     */
    private String generateFileId(String filename, String timestamp) {
        try {
            String source = (filename != null ? filename : "unknown") + timestamp;
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(source.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("生成文件ID失败: {}", e.getMessage(), e);
            // 如果MD5失败，使用时间戳作为备用方案
            return System.currentTimeMillis() + "";
        }
    }

    /**
     * 从 ossIndex.log 中查找文件路径
     *
     * @param fileId 文件ID
     * @return 文件路径，未找到返回 null
     */
    private String findFilePathFromLog(String fileId) {
        String logPath = ossFilePath + File.separator + LOG_FILENAME;
        File logFile = new File(logPath);

        if (!logFile.exists()) {
            log.warn("日志文件不存在: {}", logPath);
            return null;
        }

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 格式：uid|fileId|原文件名|文件类型|上传日期|文件路径
                String[] parts = line.split("\\|");
                if (parts.length >= 6 && parts[1].equals(fileId)) {
                    return parts[5];  // 返回文件路径（第6个字段）
                }
            }
        } catch (IOException e) {
            log.error("读取日志文件失败: {}", e.getMessage(), e);
        }

        return null;
    }

    /**
     * 从日志中删除指定文件ID的记录
     *
     * @param fileId 文件ID
     */
    private void removeLogEntry(String fileId) {
        String logPath = ossFilePath + File.separator + LOG_FILENAME;
        File logFile = new File(logPath);

        if (!logFile.exists()) {
            return;
        }

        try {
            // 1. 读取所有行
            java.util.List<String> lines = new java.util.ArrayList<>();
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(logFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            // 2. 过滤掉要删除的记录
            java.util.List<String> filteredLines = new java.util.ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split("\\|");
                // 格式：uid|fileId|原文件名|文件类型|上传日期|文件路径
                if (parts.length < 2 || !parts[1].equals(fileId)) {
                    filteredLines.add(line);
                }
            }

            // 3. 写回文件
            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(logFile))) {
                for (String line : filteredLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            log.info("日志记录已删除: fileId={}", fileId);

        } catch (IOException e) {
            log.error("更新日志文件失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 追加记录到 ossIndex.log
     * 格式：fileId|原文件名|文件类型|上传日期|文件路径
     */
    private void appendToIndexLog(String uid, String fileId, String originalFilename, String fileType,
                                  String uploadDate, String filePath) {
        String logPath = ossFilePath + File.separator + LOG_FILENAME;

        // 确保日志目录存在
        File logFile = new File(logPath);
        File parentDir = logFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // 追加写入日志
        try (FileWriter writer = new FileWriter(logFile, true)) {
            String logEntry = String.format("%s|%s|%s|%s|%s|%s%n",
                    uid,
                    fileId,
                    originalFilename != null ? originalFilename : "unknown",
                    fileType != null ? fileType : "unknown",
                    uploadDate,
                    filePath);
            writer.write(logEntry);
            log.info("日志记录成功: {}", logEntry.trim());
        } catch (IOException e) {
            log.error("写入日志失败: {}", e.getMessage(), e);
            // 日志写入失败不影响文件上传
        }
    }
}
