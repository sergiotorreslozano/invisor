package com.stl.invisor.repository;

import com.stl.invisor.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {


    // Custom query to find questions by User UUID
    @Query("SELECT q FROM Question q WHERE q.user.uuid = :userUuid")
    List<Question> findQuestionsByUserUuid(@Param("userUuid") UUID userUuid);


}
