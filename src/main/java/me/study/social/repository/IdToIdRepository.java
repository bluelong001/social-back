package me.study.social.repository;

import me.study.social.bean.Comment;
import me.study.social.bean.IdToId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdToIdRepository extends JpaRepository<IdToId,Integer> {
    IdToId findFirstByUsername(String username);
}
