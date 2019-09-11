package org.fkjava.storage.service;

import org.fkjava.commons.domain.Result;
import org.fkjava.storage.domain.FileItem;
import org.springframework.data.domain.Page;

import java.io.InputStream;

public interface StorageService {
    Page<FileItem> search(String userId, String keyword, String orderBy, String direction, int pageNumber, int pageSize);

    Result save(String userId, String name, String type, long length, InputStream in);

    Result delete(String id);

    FileItem getFileItem(String id);

    InputStream getFileContent(FileItem fileItem);
}
