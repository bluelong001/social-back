package me.study.social.repository;

import me.study.social.bean.ChatContent;
import me.study.social.bean.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatContentRepository extends JpaRepository<ChatContent,Integer> {
}
