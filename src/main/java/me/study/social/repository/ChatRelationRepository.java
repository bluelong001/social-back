package me.study.social.repository;

import me.study.social.bean.ChatContent;
import me.study.social.bean.ChatRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRelationRepository extends JpaRepository<ChatRelation,Integer> {
    ChatRelation findFirstByUserAIdAndUserBId(Integer a,Integer b);
    ChatRelation findFirstByUserBIdAndUserAId(Integer a,Integer b);
    List<ChatRelation> findAllByUserAIdOrUserBId(Integer a,Integer b);
}
