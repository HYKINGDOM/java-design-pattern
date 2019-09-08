package org.fkjava.storage.repository;

import org.fkjava.storage.domain.FileItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileItemRepository extends JpaRepository<FileItem, String>, JpaSpecificationExecutor<FileItem> {
}
