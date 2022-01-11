package com.ematos.gcpa.exame.controller;

import com.ematos.gcpa.exame.business.QuestionService;
import com.ematos.gcpa.exame.model.Question;
import com.ematos.gcpa.exame.model.QuestionOption;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="question")
@AllArgsConstructor
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
        return this.questionService.getQuestions().size();
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
                               @RequestParam(required = false) List<QuestionOption> options) {

        this.questionService.updateQuestion(questionId, title, description, options);
    }

    @GetMapping(path = "exame/{questionsNumber}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Question> getAmountQuestions(@PathVariable("questionsNumber") Integer questionsNumber) {
        return this.questionService.getAmountQuestions(questionsNumber);
    }
}
