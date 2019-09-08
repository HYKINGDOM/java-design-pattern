package org.fkjava.storage.service;

import org.fkjava.commons.domain.Result;
import org.fkjava.storage.domain.FileItem;
import org.springframework.data.domain.Page;

import java.io.InputStream;

public interface StorageService {
    Page<FileItem> search(String keyword, String orderBy, String direction, int pageNumber);

    Result save(String name, String type, long length, InputStream in);
}
