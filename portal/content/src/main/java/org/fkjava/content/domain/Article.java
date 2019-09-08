package org.fkjava.content.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "content_article")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(length = 36)
    private String id;

    private String title;
    @Column(length = 300)
    private String summary;
    // 数据库的VARCHAR类型不能支持10K的超长字符串，在不同数据库里面有不同的解决方案，MySQL叫做LONGTEXT。
    @Column(length = 10240)
    private String content;
    // 标签名称不要存储到数据库，转换为Tag对象存储到数据库
    @Transient
    private List<String> tagNames;
    // 多对多关系，一个Content可能有多个Tag
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Tag> tags;
    // 什么时候生效，生效以后才能被普通用户查询到
    @JsonFormat(pattern = "yyyy年MM月dd日HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectTime = new Date();
    // 什么时候发布、什么时候写好的，每次修改都会更新此时间
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy年MM月dd日HH:mm:ss")
    private Date publishTime = new Date();
    // 标记：如果为false，表示未删除的，true表示删除的
    private boolean deleted;

    public String getStatus() {
        if (isDeleted()) {
            return "已撤回";
        } else if (System.currentTimeMillis() >= effectTime.getTime()) {
            return "已生效";
        } else {
            return "未生效";
        }
    }
}
