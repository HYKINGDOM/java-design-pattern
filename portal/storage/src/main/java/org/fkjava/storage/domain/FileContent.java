package org.fkjava.storage.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Getter
@Setter
@Table(indexes = {@Index(columnList = "path")})
public class FileContent {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(length = 36)
    private String id;
    @Column(length = 36)
    private String path;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Blob content;
}
