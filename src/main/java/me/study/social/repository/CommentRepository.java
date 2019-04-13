package me.study.social.repository;

import me.study.social.bean.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findAllByPyqId(Integer id);
    List<Comment> findAllByFromAndTo(String usernameA,String usernameB);
    List<Comment> findAllByTo(String username);
    List<Comment> findAllByToAndFrom(String usernameA,String usernameB);
}
