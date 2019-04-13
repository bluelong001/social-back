package me.study.social.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatContent {
    @Id
    @GeneratedValue
    private Integer _id;
    private Integer userId;
    private transient UserInfo user_id;
    private transient UserInfo chatWith;
    private Integer chatWithId;
    @Builder.Default
    private LocalDateTime addTime=LocalDateTime.now();
    private String content ;
    @Builder.Default
    private Boolean unread=true;
}
