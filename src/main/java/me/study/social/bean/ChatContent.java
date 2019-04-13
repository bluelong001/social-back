package me.study.social.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SortComparator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatContent implements Comparable<ChatContent>{
    @Id
    @GeneratedValue
    private Integer _id;
    private Integer userId;
    private transient UserInfo user_id;
    private transient UserInfo chatWith;
    private Integer chatWithId;
    @Builder.Default
    private LocalDateTime addTime=LocalDateTime.now();
    @Column(columnDefinition="LONGTEXT")
    private String content ;
    @Builder.Default
    private Boolean unread=true;
    @Override
    public int compareTo(ChatContent chatContent) {
        if (this.addTime.isBefore(chatContent.getAddTime()))
            return 1;
        return -1;
    }
}
