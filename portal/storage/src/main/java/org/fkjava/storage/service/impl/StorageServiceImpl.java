package org.fkjava.storage.service.impl;

import org.fkjava.commons.domain.Result;
import org.fkjava.storage.domain.FileItem;
import org.fkjava.storage.repository.FileItemRepository;
import org.fkjava.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.io.InputStream;
import java.util.Date;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private FileItemRepository fileItemRepository;

    @Override
    public Page<FileItem> search(String keyword, String orderBy, String direction, int pageNumber) {

        // 排序条件
        Sort sort;
        if ("DESC".equals(direction)) {
            sort = Sort.by(Sort.Order.desc(orderBy));
        } else {
            sort = Sort.by(Sort.Order.asc(orderBy));
        }

        // 分页条件
        Pageable pageable = PageRequest.of(pageNumber, 10, sort);

        // 动态条件查询
        Page<FileItem> page = this.fileItemRepository.findAll((root, query, criteriaBuilder) -> {
            String kw = "%" + keyword + "%";
            Predicate predicate = criteriaBuilder.like(root.get("name"), kw);
            return predicate;
        }, pageable);

        return page;
    }

    @Override
    public Result save(String name, String type, long length, InputStream in) {
        FileItem item = new FileItem();
        item.setContentType(type);
        item.setLength(length);
        item.setName(name);
//        item.setPath(path);
        item.setUploadTime(new Date());
//        item.setUserId(userId);
        return null;
    }
}
