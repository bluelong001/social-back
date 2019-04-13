package me.study.social.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pyq implements Comparable<Pyq>{
    @Id
    @GeneratedValue
    private Integer _id;
    @Builder.Default
    private LocalDateTime addTime=LocalDateTime.now();
    @Builder.Default
    private Integer views =0;
    private String content;
    private Integer writerId;
    private transient UserInfo writer;
    private transient List<Comment> comments;
    private transient List pimg;
    @Column(columnDefinition="LONGTEXT")
    private String pimgArr;


    @Override
    public int compareTo(Pyq o) {
            if (this.addTime.isAfter(o.getAddTime()))
                return -1;
            return 1;

    }
}
