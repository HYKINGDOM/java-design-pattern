package org.fkjava.storage.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fkjava.commons.config.CommonConfigProperties;
import org.fkjava.commons.domain.Result;
import org.fkjava.storage.domain.FileContent;
import org.fkjava.storage.domain.FileItem;
import org.fkjava.storage.repository.FileContentRepository;
import org.fkjava.storage.repository.FileItemRepository;
import org.fkjava.storage.service.StorageService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final FileItemRepository fileItemRepository;
    private final CommonConfigProperties commonsConfigProperties;
    private final FileContentRepository fileContentRepository;

    public StorageServiceImpl(
            FileItemRepository fileItemRepository,
            CommonConfigProperties commonsConfigProperties,
            FileContentRepository fileContentRepository
    ) {
        this.fileItemRepository = fileItemRepository;
        this.commonsConfigProperties = commonsConfigProperties;
        this.fileContentRepository = fileContentRepository;
    }

    @Override
    public Page<FileItem> search(String userId, String keyword, String orderBy, String direction, int pageNumber, int pageSize) {

        // 排序条件
        Sort sort;
        if ("DESC".equals(direction)) {
            sort = Sort.by(Sort.Order.desc(orderBy));
        } else {
            sort = Sort.by(Sort.Order.asc(orderBy));
        }

        // 分页条件
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // 动态条件查询

        return this.fileItemRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate userIdEqual = criteriaBuilder.equal(root.get("userId"), userId);
            Predicate predicate = userIdEqual;
            if (!StringUtils.isEmpty(keyword)) {
                String kw = "%" + keyword + "%";
                Predicate keyworkLike = criteriaBuilder.like(root.get("name"), kw);
                predicate = criteriaBuilder.and(userIdEqual, keyworkLike);
            }
            return predicate;
        }, pageable);
    }

    @Override
    @Transactional
    public Result save(String userId, String name, String type, long length, InputStream in) {
        FileItem item = new FileItem();
        item.setUserId(userId);
        item.setContentType(type);
        item.setLength(length);
        item.setName(name);
        item.setUploadTime(new Date());

        // 生成随机存储路径
        String path = UUID.randomUUID().toString();
        item.setPath(path);


        // 保存文件内容
        if (commonsConfigProperties.getStorage().getType() == CommonConfigProperties.Storage.Type.fs) {
            // 存储到文件
            String dir = commonsConfigProperties.getStorage().getDir();

            // 确保目录存储
            File file = new File(dir);
            if (!file.exists()) {
                boolean b = file.mkdirs();
                if (b) {
                    log.trace("上传文件时使用的文件保存目录创建成功，保存位置: {}", file.getAbsolutePath());
                }
            }
            Path filePath = Path.of(dir, path);
            try {
                // 如果目标文件存在，则直接覆盖文件
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                log.trace("文件上传失败: " + e.getLocalizedMessage(), e);
                return Result.error("文件上传失败: " + e.getLocalizedMessage());
            }
        } else {
            // 存储到数据库
            FileContent fc = new FileContent();
            Blob blob = BlobProxy.generateProxy(in, length);
            fc.setContent(blob);
            fc.setPath(path);
            this.fileContentRepository.save(fc);
        }

        // 保存文件信息到数据库
        this.fileItemRepository.save(item);

        return Result.ok("文件上传成功");
    }

    @Override
    @Transactional
    public Result delete(String id) {
        this.fileItemRepository.findById(id)
                .ifPresent(fileItem -> {
                    if (commonsConfigProperties.getStorage().getType() == CommonConfigProperties.Storage.Type.fs) {
                        String dir = commonsConfigProperties.getStorage().getDir();
                        File file = new File(dir, fileItem.getPath());
                        boolean b = file.delete();
                        log.trace("删除文件，文件路径: {}，删除结果: {}", file.getAbsolutePath(), b);
                    } else {
                        this.fileContentRepository.deleteByPath(fileItem.getPath());
                    }
                    this.fileItemRepository.delete(fileItem);
                });
        return Result.ok("文件删除成功");
    }

    @Override
    public FileItem getFileItem(String id) {
        return this.fileItemRepository.findById(id).orElse(null);
    }

    @Override
    public InputStream getFileContent(FileItem fileItem) {
        if (commonsConfigProperties.getStorage().getType() == CommonConfigProperties.Storage.Type.fs) {
            String dir = commonsConfigProperties.getStorage().getDir();
            File file = new File(dir, fileItem.getPath());
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                log.trace("文件 {} 未找到", file.getAbsolutePath());
                return null;
            }
        } else {
            FileContent fc = this.fileContentRepository.findByPath(fileItem.getPath());
            if (fc == null) {
                log.trace("数据存储的文件内容未找到，文件路径： {}", fileItem.getPath());
                return null;
            }
            try {
                return fc.getContent().getBinaryStream();
            } catch (SQLException e) {
                log.trace("无法读取数据库存储的文件内容，文件路径: {}，异常原因: {}", fileItem.getPath(), e.getLocalizedMessage());
                log.trace("无法读取数据库存储的文件内容的异常跟踪", e);
                return null;
            }
        }
    }
}
