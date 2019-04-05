package me.study.social.repository;

import me.study.social.bean.ChatContent;
import me.study.social.bean.Pyq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PyqRepository extends JpaRepository<Pyq,Integer> {
    List<Pyq> findAllByWriterId(Integer writerId);
}
