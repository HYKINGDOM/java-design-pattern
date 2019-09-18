package org.fkjava.gift.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="gift_present")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Present {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(length = 36)
    //id
    private String id;
    //名称
    private String name;
    //价格
    private String price;
    //标签
    @Transient
    private List<String> labels;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Tally> tally;
    //描述
    @Column(length = 10240)
    private String gift_describe;
    //类型
    private String gift_type;

}
