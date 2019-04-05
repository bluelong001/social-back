package me.study.social.repository;

import me.study.social.bean.Password;
import me.study.social.bean.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<Password,Integer> {
}
