package me.study.social.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRelation {
    @Id
    @GeneratedValue
    private Integer _id;
    private transient UserInfo userA;
    private transient UserInfo userB;
    private transient List<ChatContent> chatContent;
    private Integer userAId;
    private Integer userBId;
    private Integer chatContentIdArr;
}
