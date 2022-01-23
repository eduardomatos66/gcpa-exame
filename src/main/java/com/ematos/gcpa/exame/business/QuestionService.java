package com.ematos.gcpa.exame.business;

import com.ematos.gcpa.exame.exception.NotEnoughQuestionsException;
import com.ematos.gcpa.exame.exception.QuestionNotExistentException;
import com.ematos.gcpa.exame.model.Question;
import com.ematos.gcpa.exame.model.QuestionOption;
import com.ematos.gcpa.exame.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    @GetMapping
    public List<Question> getQuestions() {
        return this.questionRepository.findAll();
    }

    public Question getQuestionById(Long questionId) {
        Optional<Question> optionalQuestion = this.questionRepository.findById(questionId);

        if (optionalQuestion.isEmpty()) {
            throw new QuestionNotExistentException(
                    String.format("There is no question for id: %s", questionId));
        }

        return optionalQuestion.get();
    }


    public void addNewQuestion(Question question) {
        if (this.questionRepository.findQuestionById(question.getId()) != null) {
            throw new IllegalStateException("The question's key is already registered!!");
        }
        this.questionRepository.save(question);
    }

    public void deleteQuestion(Long questionId) {
        boolean exists = this.questionRepository.existsById(questionId);
        if (!exists) {
            throw new QuestionNotExistentException("The question with id " + questionId + " does not exists");
        }
        this.questionRepository.deleteById(questionId);
    }

    @Transactional
    public void updateQuestion(Long questionId, String title, String subject, List<QuestionOption> options, Set<String> labels) {
        Question question = this.getQuestionById(questionId);

        if (title != null &&
                title.length() > 0 &&
                !Objects.equals(question.getTitle(), title)) {
            question.setTitle(title);
        }

        if (subject != null &&
                subject.length() > 0 &&
                !Objects.equals(question.getSubject(), subject)) {

            question.setSubject(subject);
        }

        question.setQuestionOptionList(options);
        question.setLabels(labels);
    }

    public List<Question> getAmountQuestions(Integer questionsNumber) {
        List<Question> allQuestions = this.questionRepository.findAll();
        if (allQuestions.size() < questionsNumber) {
            throw new NotEnoughQuestionsException("There is not enough answers as requested: " + questionsNumber);
        }

        Collections.shuffle(allQuestions);
        return allQuestions.subList(0, questionsNumber);
    }

    public Set<String> getQuestionsLabel() {
        Set<String> result = new HashSet<>();
        this.questionRepository.findAll().forEach(question -> result.addAll(question.getLabels()));
        return result;
    }

    public List<Question> getAllQuestionsWithLabel(String label) {
        return this.questionRepository.findAll().stream()
                .filter(question -> question.getLabels().contains(label))
                .collect(Collectors.toList());
    }

    public List<Question> getQuestionsWithMultipleResponse() {

        return this.questionRepository.findAll().stream()
                .filter(question -> question.getQuestionOptionList()
                        .stream().filter(QuestionOption::isCorrect).count() > 1)
                .collect(Collectors.toList());
    }

    public List<Question> getQuestionAmountWithLabel(String label, Integer questionsNumber) {
        List<Question> questionList = this.getAllQuestionsWithLabel(label);
        Collections.shuffle(questionList);

        if (questionList.size() > questionsNumber) {
            return questionList.subList(0, questionsNumber);
        }
        return questionList;
    }
}
