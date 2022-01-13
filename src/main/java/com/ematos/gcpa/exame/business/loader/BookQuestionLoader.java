package com.ematos.gcpa.exame.business.loader;

import com.ematos.gcpa.exame.exception.NotEnoughAlternativesException;
import com.ematos.gcpa.exame.exception.QuestionNotExistentException;
import com.ematos.gcpa.exame.model.Question;
import com.ematos.gcpa.exame.model.QuestionOption;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class BookQuestionLoader extends AbstractLoader {

    private static final String QUESTION_TITLE_REGEX = "^\\d+\\..*";
    private static final String ALTERNATIVES_REGEX = "^[ABCD]\\..*";
    private static final String ANSWER_REGEX = "^(\\d+\\.) ([ABCD]\\.)(.*)";
    private static final String QUESTION_FILE_PATTERN = "(.*)_questions";

    protected Scanner myReader;

    protected String questionsPath;


    @Override
    protected void loadPathConstants() {
        this.questionsPath = String.format("%s/%s",
                Objects.requireNonNull(classLoader.getResource(".")).getFile(),
                "bk");
    }

    @Override
    protected void loadQuestions() {
        this.buildQuestion(
                new File(this.questionsPath));
    }

    protected void buildQuestion(File resourceFolder) {
        for (File f : Objects.requireNonNull(resourceFolder.listFiles())) {

            String fileName = f.getName();
            Pattern p = Pattern.compile(QUESTION_FILE_PATTERN);
            Matcher m = p.matcher(fileName);

            if (m.find()) {
                this.questionBuilder(f);
                this.readAnswersFile(new File(f.getAbsolutePath().replace("questions", "answers")));
            }
        }
    }

    private void readAnswersFile(File file) {
        try {
            this.myReader = new Scanner(file);
            StringBuilder answer = new StringBuilder();
            boolean canUpdate = false;

            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();

                if ((canUpdate && line.matches(ANSWER_REGEX)) || !myReader.hasNextLine()) {
                    this.updateQuestion(this.getQuestionToken(file.getName()), answer.toString());
                    answer = new StringBuilder(line);
                    canUpdate = false;
                }

                if (!myReader.hasNextLine()) {
                    this.updateQuestion(this.getQuestionToken(file.getName()), answer.toString());
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
        }
    }

    private String getQuestionToken(String name) {
        Pattern p = Pattern.compile(QUESTION_FILE_PATTERN);
        Matcher m = p.matcher(name);
        String result = "";

        if (m.find()) {
            result = m.group(1);
        }

        return result.trim();
    }

    private void updateQuestion(String questionToken, String answer) {
        Question question = this.searchForQuestion(this.extractQuestionNumberFromAnswer(answer));
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

    private Question searchForQuestion(String answer) {
        Question result = null;
        int counter = 0;

        while (result == null && counter < this.questions.size()) {
            Question currentQuestion = this.questions.get(counter);

            if (currentQuestion.getTitle().startsWith(answer)) {
                result = this.questions.get(counter);
            }
            counter++;
        }
        return result;
    }

    @Override
    protected void questionBuilder(File file) {
        try {
            this.myReader = new Scanner(file);
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

                    this.createQuestion(title, alternatives);

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
        }
    }

    private void createQuestion(StringBuilder title, List<QuestionOption> alternatives) {
        if (alternatives.size() < 4) {
            throw new NotEnoughAlternativesException(
                    String.format("Question does not have enough alternatives: %s", title));
        }

        Question question = new Question();
        question.setTitle(title.toString().trim());
        question.setQuestionOptionList(alternatives);

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
