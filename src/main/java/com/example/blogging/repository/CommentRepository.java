package com.example.blogging.repository;

import com.example.blogging.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
