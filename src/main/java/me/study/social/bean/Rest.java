package me.study.social.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

public class Rest extends HashMap<String ,Object> {
    public static Rest builder(){
        return new Rest();
    }
    public Rest code (int code){
        this.put("code",code);
        return this;
    }
    public Rest msg (String msg){
        this.put("msg",msg);
        return this;
    }
    public Rest set(String key,Object object) {
        this.put(key,object);
        return this;
    }
}
