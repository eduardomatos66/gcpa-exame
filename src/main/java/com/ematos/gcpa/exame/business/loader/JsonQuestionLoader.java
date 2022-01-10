package com.ematos.gcpa.exame.business.loader;

import com.ematos.gcpa.exame.model.Question;
import com.ematos.gcpa.exame.model.QuestionOption;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class JsonQuestionLoader extends AbstractLoader {

    protected String questionsPath;

    @Override
    protected void loadPathConstants() {
        this.questionsPath = String.format("%s/%s",
                        Objects.requireNonNull(classLoader.getResource(".")).getFile(),
                        "questions");
    }

    @Override
    protected void loadQuestions() {
        this.buildQuestion(
                new File(this.questionsPath)
        );
    }

    protected Question questionBuilder(File jsonFile) {
        Question question = new Question();
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(jsonFile));

            JSONObject jsonObject = (JSONObject) obj;

            String key = (String) jsonObject.get("key");
            String title = (String) jsonObject.get("title");
            question.setTitle(String.format("[%s] %s", key, title));

            String subject = (String) jsonObject.get("subject");
            question.setSubject(subject);

            List<String> answers = loadAnswers((String) jsonObject.get("answer"));
            JSONArray companyList = (JSONArray) jsonObject.get("alternatives");
            question.setQuestionOptionList(buildQuestionOptions(answers, companyList));

            if (validateQuestionBeforeSave(question)) {
                return question;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static boolean validateQuestionBeforeSave(Question question) {
        AtomicBoolean hasAlternative = new AtomicBoolean(false);

        // At least one correct option
        question.getQuestionOptionList().forEach(questionOption -> {
            hasAlternative.set(hasAlternative.get() || questionOption.isCorrect());
        });

        return !question.getTitle().isEmpty() && hasAlternative.get();
    }

    private static List<QuestionOption> buildQuestionOptions(List<String> answers, JSONArray list) {
        List<QuestionOption> questionOptionList = new ArrayList<>();

        list.forEach(o -> {
            String title = o.toString();
            boolean isCorrect = answers.contains(title.substring(0, 1));
            questionOptionList.add(new QuestionOption(title.substring(2), isCorrect));
        });

        return questionOptionList;
    }

    private static List<String> loadAnswers(String answer) {
        return Arrays.stream(answer.split(" "))
                .filter(s -> ((String) s).matches("[ABCDE]"))
                .collect(Collectors.toList());
    }
}
