package org.fkjava.storage.repository;

import org.fkjava.storage.domain.FileContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileContentRepository extends JpaRepository<FileContent, String> {
    void deleteByPath(String path);

    FileContent findByPath(String path);
}
