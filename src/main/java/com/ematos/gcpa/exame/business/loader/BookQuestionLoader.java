package com.ematos.gcpa.exame.business.loader;

import com.ematos.gcpa.exame.exception.NotEnoughAlternativesException;
import com.ematos.gcpa.exame.exception.QuestionNotExistentException;
import com.ematos.gcpa.exame.model.Question;
import com.ematos.gcpa.exame.model.QuestionOption;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class BookQuestionLoader extends AbstractLoader {

    private static final String QUESTION_TITLE_REGEX = "^\\d+\\..*";
    private static final String ALTERNATIVES_REGEX = "^[ABCDE]\\..*";
    private static final String ANSWER_REGEX = "^(\\d+\\.) ([ABCDE]\\.)(.*)";
    private static final String QUESTION_FILE_PATTERN = "(.*)_questions";

    protected Scanner myReader;

    public BookQuestionLoader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    @Override
    protected void loadPathConstants() {
        try {
            this.questionsResource = ResourcePatternUtils
                    .getResourcePatternResolver(this.resourceLoader)
                    .getResources("classpath:bk*/*");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadQuestions() {
        LOG.warning("Questions Path: " + this.questionsResource);
        for (Resource resource : this.questionsResource) {
            LOG.warning("Creating question for resource: " + resource.toString());
            this.buildQuestionsFromResource(resource);
        }
    }

    protected void buildQuestionsFromResource(Resource resource) {
        String fileName = resource.getFilename();
        Pattern p = Pattern.compile(QUESTION_FILE_PATTERN);
        Matcher m = p.matcher(fileName);

        if (m.find()) {
            this.questionBuilder(resource);
            Resource answerResourceFile = null;
            try {
                answerResourceFile = this.getAnswerResourceFile(new File(((ClassPathResource) resource).getPath()).getParentFile().getName(), resource.getFilename());
            } catch (IOException e) {
                throw new RuntimeException("Error while loading answer file: " + resource.getFilename(), e);
            }
            this.readAnswersFile(answerResourceFile);
        }
    }

    private Resource getAnswerResourceFile(String source, String filename) throws IOException {
        String desiredFileName = filename.replace("questions", "answers");

        for (Resource resource : this.questionsResource) {
            if (Objects.requireNonNull(resource.getFilename()).equals(desiredFileName)
            && new File(((ClassPathResource) resource).getPath()).getPath().contains(source)) {
                return resource;
            }
        }
        LOG.warning("NO ANSWER FILE has tem found");
        return null;
    }

    private void readAnswersFile(Resource resource) {
        try {
            this.myReader = new Scanner(resource.getInputStream());
            StringBuilder answer = new StringBuilder();
            boolean canUpdate = false;

            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();

                if ((canUpdate && line.matches(ANSWER_REGEX)) || !myReader.hasNextLine()) {
                    this.updateQuestion(
                            new File(((ClassPathResource) resource).getPath()).getParentFile().getName(),
                            this.getQuestionToken(resource.getFilename()),
                            answer.toString());

                    answer = new StringBuilder(line);
                    canUpdate = false;
                }

                if (!myReader.hasNextLine()) {
                    this.updateQuestion(
                            new File(((ClassPathResource) resource).getPath()).getParentFile().getName(),
                            this.getQuestionToken(resource.getFilename()),
                            answer.toString());
                } else if (!canUpdate && line.matches(ANSWER_REGEX)) {
                    answer = new StringBuilder(line.trim());
                    canUpdate = true;
                } else {
                    answer.append(" ").append(line.trim());
                }
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred trying to open file.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getQuestionToken(String name) {
        String fileName = name.replace("answers", "questions");
        Pattern p = Pattern.compile(QUESTION_FILE_PATTERN);
        Matcher m = p.matcher(fileName);
        String result = "";

        if (m.find()) {
            result = m.group(1);
        }

        return result.trim();
    }

    private void updateQuestion(String source, String questionToken, String answer) {
        Question question = this.searchForQuestion(source, this.extractQuestionNumberFromAnswer(answer), questionToken);
        List<String> correctAlternatives = this.extractAlternativesFromAnswer(answer);

        if (question == null) {
            throw new QuestionNotExistentException(String.format("Question not found: %s", answer));
        }

        for (String correctAlternative : correctAlternatives) {
            for (QuestionOption questionOption: question.getQuestionOptionList()) {
                if (questionOption.getOptionDescription().startsWith(correctAlternative)) {
                    questionOption.setCorrect(true);
                    break;
                }
            }
        }

        question.setExplanation(this.extractExplanationFromAnswer(answer));
        question.setTitle(String.format("%s %s", questionToken, question.getTitle()));
    }

    private String extractQuestionNumberFromAnswer(String answer) {
        Pattern p = Pattern.compile(ANSWER_REGEX);
        Matcher m = p.matcher(answer);
        String result = "";
        if (m.find()) {
            result = m.group(1);
        }
        return result.trim();
    }

    private List<String> extractAlternativesFromAnswer(String answer) {
        Pattern p = Pattern.compile(ANSWER_REGEX);
        Matcher m = p.matcher(answer);
        List<String> result = new ArrayList<>();

        if (m.find()) {
            String temp = m.group(2).trim();
            if (temp.contains(" ")) {
                result.addAll(Arrays.stream(temp.split(" ")).collect(Collectors.toList()));
            } else {
                result.add(temp);
            }
        }
        return result;
    }

    private String extractExplanationFromAnswer(String answer) {
        Pattern p = Pattern.compile(ANSWER_REGEX);
        Matcher m = p.matcher(answer);
        String result = "";
        if (m.find()) {
            result = m.group(3);
        }
        return result.trim();
    }

    private Question searchForQuestion(String source, String answer, String questionToken) {
        Question result = null;
        int counter = 0;
        String questionLabel = String.format("%s_%s", questionToken, "questions");
        List<Question> chapterQuestions = this.questions.stream()
                .filter(question -> question.getLabels().contains(questionLabel))
                .filter(question -> question.getSource().contains(source))
                .collect(Collectors.toList());

        while (result == null && counter < chapterQuestions.size()) {
            Question currentQuestion = chapterQuestions.get(counter);

            if (currentQuestion.getTitle().startsWith(answer)) {
                result = currentQuestion;
            }
            counter++;
        }
        return result;
    }

    @Override
    protected void questionBuilder(Resource resource) {
        try {
            this.myReader = new Scanner(resource.getInputStream());
            LineTypeEnum lineTypeEnum = LineTypeEnum.TITLE;
            StringBuilder title = new StringBuilder();
            List<QuestionOption> alternatives = new ArrayList<>();

            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if (line.matches(ALTERNATIVES_REGEX)) {
                    lineTypeEnum = LineTypeEnum.ALTERNATIVES;
                    alternatives.add(new QuestionOption(line, false));
                }

                if ((line.matches(QUESTION_TITLE_REGEX) && lineTypeEnum == LineTypeEnum.ALTERNATIVES)
                        || !myReader.hasNextLine()) {

                    this.createQuestion(
                            new File(((ClassPathResource) resource).getPath()).getParentFile().getName(),
                            resource.getFilename(),
                            title,
                            alternatives);

                    // Reset entries
                    title = new StringBuilder(line.trim());
                    alternatives = new ArrayList<>();
                    lineTypeEnum = LineTypeEnum.TITLE;

                } else if (lineTypeEnum == LineTypeEnum.ALTERNATIVES) {
                    int lastPos = alternatives.size() - 1;
                    if (!alternatives.get(lastPos).getOptionDescription().contains(line.trim())) {
                        alternatives.get(lastPos).setOptionDescription(
                                String.format("%s %s", alternatives.get(lastPos).getOptionDescription(), line.trim()));
                    }

                } else if (line.matches(QUESTION_TITLE_REGEX) || lineTypeEnum == LineTypeEnum.TITLE) {
                    title.append(" ").append(line.trim());
                }
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred trying to open file.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createQuestion(String folder, String fileName, StringBuilder title, List<QuestionOption> alternatives) {
        if (alternatives.size() < 4) {
            throw new NotEnoughAlternativesException(
                    String.format("Question does not have enough alternatives: %s", title));
        }

        Question question = new Question();
        question.setTitle(title.toString().trim());
        question.setQuestionOptionList(alternatives);
        question.addLabel(fileName);
        question.setSource(folder);

        this.questions.add(question);
    }

    private String getName(String fileName) {
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }
}
