package org.fkjava.storage.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.fkjava.commons.domain.Result;
import org.fkjava.storage.domain.FileItem;
import org.fkjava.storage.service.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    private final StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public Page<FileItem> list(
            @AuthenticationPrincipal Principal principal,
            @RequestParam(value = "kw", required = false) String keyword,
            @RequestParam(value = "pn", defaultValue = "0") int pageNumber,
            @RequestParam(value = "ps", defaultValue = "10") int pageSize,
            @RequestParam(value = "ob", defaultValue = "name") String orderBy,
            @RequestParam(value = "d", defaultValue = "ASC") String direction
    ) {
        String userId = principal.getName();
        return this.storageService.search(userId, keyword, orderBy, direction, pageNumber, pageSize);
    }

    @PostMapping
    public Result upload(
            @RequestPart() MultipartFile file,
            @AuthenticationPrincipal Principal principal)
            throws IOException {
        String userId = principal.getName();
        String name = file.getOriginalFilename();
        String type = file.getContentType();
        long length = file.getSize();
        try (InputStream in = file.getInputStream()) {
            // 返回的结果中，attachment属性是FileItem对象的id，通过此id可以下载文件
            return this.storageService.save(userId, name, type, length, in);
        }
    }

    @DeleteMapping("{id}")
    public Result delete(@PathVariable String id) {
        return this.storageService.delete(id);
    }


    @GetMapping("{id}")
    public FileItem get(@PathVariable String id) {
        return this.storageService.getFileItem(id);
    }

    @GetMapping("download/{id}")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable String id) {
        FileItem fileItem = this.storageService.getFileItem(id);

        if (fileItem == null) {
            return ResponseEntity.notFound().build();
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentLength(fileItem.getLength());
        builder.contentType(MediaType.valueOf(fileItem.getContentType()));
        String name = fileItem.getName();
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        builder.header("Content-Disposition", "attachment;filename*=UTF-8''" + encodedName);

        return builder.body(outputStream -> {
            InputStream fileContent = this.storageService.getFileContent(fileItem);
            if (fileContent == null) {
                throw new IOException("无法得到文件内容，请跟踪详细异常日志！");
            }
            IOUtils.copy(fileContent, outputStream);
        });
    }
}
