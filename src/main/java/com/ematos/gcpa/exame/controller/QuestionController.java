package com.ematos.gcpa.exame.controller;

import com.ematos.gcpa.exame.business.QuestionService;
import com.ematos.gcpa.exame.model.Question;
import com.ematos.gcpa.exame.model.QuestionOption;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path="questions")
@AllArgsConstructor
@CrossOrigin
public class QuestionController extends AbstractController {

    private final QuestionService questionService;

    @GetMapping(path="")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Question> getQuestions() {

        return this.questionService.getQuestions();
    }

    @GetMapping(path="/amount")
    @ResponseStatus(value = HttpStatus.OK)
    public int getQuestionsAmount() {
        return this.questionService.getQuestionsAmount();
    }

    @GetMapping(path="/multipleResponseQuestions")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Question> getQuestionsWithMultipleResponse() {
        return this.questionService.getQuestionsWithMultipleResponse();
    }

    @GetMapping(path="/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Question getQuestionById(@PathVariable("id")  Long id) {

        return this.questionService.getQuestionById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void registerNewQuestion(@RequestBody Question question) {
        this.questionService.addNewQuestion(question);
    }

    @DeleteMapping(path = "/{questionId}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteQuestion(@PathVariable("questionId") Long questionId) {
        this.questionService.deleteQuestion(questionId);
    }

    @PutMapping(path = "/{questionId}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void updateQuestion(@PathVariable("questionId") Long questionId,
                               @RequestParam(required = false) String title,
                               @RequestParam(required = false) String description,
                               @RequestParam(required = false) List<QuestionOption> options,
                               @RequestParam(required = false) Set<String> labels) {

        this.questionService.updateQuestion(questionId, title, description, options, labels);
    }

    @GetMapping(path = "labels")
    @ResponseStatus(value = HttpStatus.OK)
    public Set<String> getQuestionsLabel() {
        return this.questionService.getQuestionsLabel();
    }

    @GetMapping(path = "exam")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Question> getQuestionAmountWithLabel(@RequestParam Optional<String> label,
                                                     @RequestParam Optional<List<String>> labels,
                                                     @RequestParam Optional<Integer> questionsNumber) {
        List<Question> questionList;

        if (label.isPresent() && questionsNumber.isPresent()) {
            questionList = this.questionService.getQuestionAmountWithLabel(label.get(), questionsNumber.get());
        } else if (label.isPresent()) {
            questionList = this.questionService.getAllQuestionsWithLabel(label.get());
        } else if (questionsNumber.isPresent()) {
            questionList = this.questionService.getAmountQuestions(questionsNumber.get());
        } else if (labels.isPresent()) {
            questionList = this.questionService.getAllQuestionsWithLabels(labels.get());
        } else {
            // Default number of question on exam.
            questionList = this.questionService.getAmountQuestions(50);
        }

        return questionList;
    }
}
