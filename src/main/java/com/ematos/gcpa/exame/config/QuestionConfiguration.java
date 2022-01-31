package com.ematos.gcpa.exame.config;

import com.ematos.gcpa.exame.business.loader.BookQuestionLoader;
import com.ematos.gcpa.exame.business.loader.JsonQuestionLoader;
import com.ematos.gcpa.exame.model.Question;
import com.ematos.gcpa.exame.repository.QuestionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.*;


@Configuration
@EnableTransactionManagement
public class QuestionConfiguration {

    private final QuestionRepository questionRepository;

    public QuestionConfiguration(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Bean
    void loadQuestions() {
        List<Question> questions = new ArrayList<>();

        JsonQuestionLoader jsonQuestionLoader = new JsonQuestionLoader();
        BookQuestionLoader bookQuestionLoader = new BookQuestionLoader();

        questions.addAll(jsonQuestionLoader.getQuestions());
        questions.addAll(bookQuestionLoader.getQuestions());

        this.questionRepository.saveAll(questions);
    }
}
