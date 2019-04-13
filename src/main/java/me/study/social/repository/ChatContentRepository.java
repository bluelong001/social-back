package me.study.social.repository;

import me.study.social.bean.ChatContent;
import me.study.social.bean.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatContentRepository extends JpaRepository<ChatContent,Integer> {
    List<ChatContent> findAllByUserIdAndChatWithIdAndUnread(Integer userId,Integer chatWithId,boolean unread);
}
