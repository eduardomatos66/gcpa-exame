package com.ematos.gcpa.exame.config;

import com.ematos.gcpa.exame.business.loader.BookQuestionLoader;
import com.ematos.gcpa.exame.business.loader.JsonQuestionLoader;
import com.ematos.gcpa.exame.business.loader.SiteArchQuestionLoader;
import com.ematos.gcpa.exame.model.Question;
import com.ematos.gcpa.exame.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.*;
import java.util.logging.Logger;


@Configuration
@EnableTransactionManagement
public class QuestionConfiguration {
    protected Logger LOG = Logger.getLogger(QuestionConfiguration.class.getName());

    @Autowired
    private ResourceLoader resourceLoader;

    private final QuestionRepository questionRepository;

    public QuestionConfiguration(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Bean
    void loadQuestions() {
        List<Question> questions = new ArrayList<>();

        JsonQuestionLoader jsonQuestionLoader = new JsonQuestionLoader(resourceLoader);
        BookQuestionLoader bookQuestionLoader = new BookQuestionLoader(resourceLoader);
        SiteArchQuestionLoader siteArchQuestionLoader = new SiteArchQuestionLoader(resourceLoader);

        questions.addAll(jsonQuestionLoader.getQuestions());
        questions.addAll(bookQuestionLoader.getQuestions());
        questions.addAll(siteArchQuestionLoader.getQuestions());

        this.questionRepository.saveAll(questions);
        LOG.warning("QUESTIONS CREATED: " + questions.size());
    }
}
