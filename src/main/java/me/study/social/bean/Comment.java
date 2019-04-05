package me.study.social.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue
    private Integer _id;
    @Builder.Default
    private LocalDateTime addTime = LocalDateTime.now();
    private String content;
    private String writer ;
    @Column(name = "_from")
    private String from;
    @Column(name = "_to")
    private String to;
    private Integer pyqId;
    private String replyArr;
    @Builder.Default
    private Boolean isn=true;
    @Builder.Default
    private Boolean toisn=true;
    @Transient
    private transient Pyq pyq;
    @Transient
    private transient List reply;
}
