package com.ematos.gcpa.exame.business.loader;

import com.ematos.gcpa.exame.model.Question;
import com.ematos.gcpa.exame.model.QuestionOption;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class JsonQuestionLoader extends AbstractLoader {

    public JsonQuestionLoader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    @Override
    protected void loadPathConstants() {
        try {
            this.questionsResource = ResourcePatternUtils
                    .getResourcePatternResolver(this.resourceLoader)
                    .getResources("classpath:questions/*.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void loadQuestions() {
        LOG.warning("Questions Path: " + this.questionsResource);
        for (Resource resource : this.questionsResource) {
            LOG.warning("Creating question for resource: " + resource.toString());
            this.questionBuilder(resource);
        }
    }

    protected void questionBuilder(Resource resource) {
        Question question = new Question();
        JSONParser parser = new JSONParser();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readValue(resource.getInputStream(), JsonNode.class);

            String key = jsonNode.get("key").asText();
            String title = jsonNode.get("title").asText();
            question.setTitle(String.format("[%s] %s", key, title));

            JsonNode subject = jsonNode.get("subject");
            if (subject != null) {
                question.setSubject(subject.asText());
                question.addLabel(subject.asText());
            }
            question.addLabel("site");

            List<String> answers = loadAnswers(jsonNode.get("answer").asText());
            ArrayNode companyList = (ArrayNode) jsonNode.get("alternatives");
            question.setQuestionOptionList(buildQuestionOptions(answers, companyList));

            if (validateQuestionBeforeSave(question)) {
                this.questions.add(question);
            } else {
                LOG.warning("QUESTION NOT CREATED: " + question.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean validateQuestionBeforeSave(Question question) {
        AtomicBoolean hasAlternative = new AtomicBoolean(false);

        // At least one correct option
        question.getQuestionOptionList().forEach(questionOption -> {
            hasAlternative.set(hasAlternative.get() || questionOption.isCorrect());
        });

        return !question.getTitle().isEmpty() && hasAlternative.get();
    }

    private static List<QuestionOption> buildQuestionOptions(List<String> answers, JsonNode list) {
        List<QuestionOption> questionOptionList = new ArrayList<>();

        list.forEach(o -> {
            String title = o.asText();
            boolean isCorrect = answers.contains(title.substring(0, 1));
            questionOptionList.add(new QuestionOption(title, isCorrect));
        });

        return questionOptionList;
    }

    private static List<String> loadAnswers(String answer) {
        return Arrays.stream(answer.split(" "))
                .filter(s -> ((String) s).matches("[ABCDE]"))
                .collect(Collectors.toList());
    }
}
