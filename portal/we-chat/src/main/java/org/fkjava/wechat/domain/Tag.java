package org.fkjava.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(name = "search_by_account_and_id", columnList = "account, weChatTagId")
})
public class Tag {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(length = 36)
    private String tagId;

    // 微信公众号的账号
    private String account;

    // 微信公众号平台上面的id
    // 新建一个标签的时候，需要把标题提交到公众号平台，才能返回标签的id
    // 修改公众号平台上面的标签，需要携带此id
    @JsonProperty("id")
    private Integer weChatTagId;
    // 标签的名称
    private String name;
    // 是否临时标签，临时标签由程序启动的时候自动处理
    private boolean temporary;

    @JsonIgnore
    @ManyToMany
    private List<UserInfo> userInfos;
    // 此属性不要保存到数据库，只是内存中用于转换，用于传递给页面的
    @Transient
    private List<String> users = new LinkedList<>();
}
