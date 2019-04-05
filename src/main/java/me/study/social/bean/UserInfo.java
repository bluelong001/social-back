package me.study.social.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class UserInfo {

    @Id
    @GeneratedValue
    private Integer _id;
    private String username;
    @Builder.Default
    private Boolean isAdmin=false;
    @Builder.Default
    private String avater="default.jpg";
    @Builder.Default
    private String signature = "这人很懒，这是系统帮他写的";

}
