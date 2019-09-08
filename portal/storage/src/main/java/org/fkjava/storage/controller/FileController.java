package org.fkjava.storage.controller;

import org.fkjava.commons.domain.Result;
import org.fkjava.storage.domain.FileItem;
import org.fkjava.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private StorageService storageService;

    @GetMapping
    public Page<FileItem> list(
            @RequestParam(value = "kw", required = false) String keyword,
            @RequestParam(value = "pn", defaultValue = "0") int pageNumber,
            @RequestParam(value = "ob", defaultValue = "name") String orderBy,
            @RequestParam(value = "d", defaultValue = "ASC") String direction
    ) {
        return this.storageService.search(keyword, orderBy, direction, pageNumber);
    }

    @PostMapping
    public Result upload(
            @RequestPart() MultipartFile file,
            @AuthenticationPrincipal Principal principal)
            throws IOException {

        System.out.println(principal);
        String name = file.getOriginalFilename();
        String type = file.getContentType();
        long length = file.getSize();
        try (InputStream in = file.getInputStream()) {
            return this.storageService.save(name, type, length, in);
        }
    }
}
