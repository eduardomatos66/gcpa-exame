package com.ematos.gcpa.exame.repository;

import com.ematos.gcpa.exame.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository
        extends JpaRepository<Question, Long> {

    @Query("SELECT s FROM Question s WHERE s.id = ?1")
    Question findQuestionById(Long id);

}