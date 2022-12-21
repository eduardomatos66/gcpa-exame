package com.ematos.gcpa.exame.business.loader;

import com.ematos.gcpa.exame.model.Question;
import com.ematos.gcpa.exame.model.QuestionOption;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SiteArchQuestionLoader extends AbstractLoader {

    private static final String TITLE_REGEX = "^<p>\\d+\\.(.*)";
    private static final String ANSWER_REGEX = "^Answers?: ([ABCDEF ]+)(.*)";
    private static final String ALTERNATIVES_REGEX = "^[(ABCDEF)]\\.(.*)";

    public SiteArchQuestionLoader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    private static void registerCorrectAnswer(Question question, String s) {
        String[] corrects = s.trim().split(" ");
        Arrays.stream(corrects).forEach(correct -> {
            int optionNum = Alternative.valueOf(correct).ordinal();
            question.getQuestionOptionList().get(optionNum).setCorrect(true);
        });
    }

    @Override
    protected void loadPathConstants() {
        try {
            this.questionsResource = ResourcePatternUtils
                    .getResourcePatternResolver(this.resourceLoader)
                    .getResources("classpath:questions/arch/questions*");
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

    @Override
    protected void questionBuilder(Resource resource) {
        Pattern titlePattern = Pattern.compile(TITLE_REGEX);
        Pattern answerPattern = Pattern.compile(ANSWER_REGEX);
        Pattern alternativesPattern = Pattern.compile(ALTERNATIVES_REGEX);

        List<QuestionOption> options = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            TextType currentType = TextType.NONE;
            Question question = new Question();
            String line;

            while ((line = br.readLine()) != null) {
                Matcher titleMatcher = titlePattern.matcher(line);
                Matcher answerMatcher = answerPattern.matcher(line);
                Matcher alternativesMatcher = alternativesPattern.matcher(line);

                if (answerMatcher.find()) {
                    if (currentType == TextType.ALTERNATIVES) {
                        question.setQuestionOptionList(options);
                    }

                    currentType = TextType.ANSWER;
                    registerCorrectAnswer(question, answerMatcher.group(1));
                    if (answerMatcher.groupCount() > 1) {
                        question.setExplanation(answerMatcher.group(2));
                    }

                } else if (alternativesMatcher.find()) {
                    currentType = TextType.ALTERNATIVES;
                    options.add(new QuestionOption(alternativesMatcher.group(1), false));

                } else if (currentType == TextType.ALTERNATIVES) {
                    int index = options.size() -1;
                    String value = options.get(options.size() -1).getOptionDescription();
                    value += " " + line;
                    options.remove(index);
                    options.add(new QuestionOption(value, false));

                } else if (currentType == TextType.TITLE) {
                    question.setTitle(question.getTitle() + "\n" + line);

                } else if (titleMatcher.find()) {
                    if (question.getTitle() != null) {
                        questions.add(question);
                    }

                    question = new Question();
                    question.addLabel("Architect");
                    question.addLabel("site");
                    question.setSource("https://gcp-examquestions.com/gcp-professional-cloud-architect-practice-exam-part-1/");
                    options = new ArrayList<>();

                    question.setTitle(titleMatcher.group(1));
                    currentType = TextType.TITLE;
                }
            }
            System.out.println(questions.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

enum TextType {
    ANSWER, ALTERNATIVES, TITLE, NONE
}

enum Alternative {
    A, B, C, D, E, F;
}