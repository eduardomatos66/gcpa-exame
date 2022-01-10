package com.ematos.gcpa.exame.business.loader;

import com.ematos.gcpa.exame.model.Question;
import com.ematos.gcpa.exame.model.QuestionOption;
import com.google.common.io.Files;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class BookQuestionLoader extends AbstractLoader {

    public static final String QUESTION_TITLE_REGEX = "^\\d+\\..*";
    public static final String ALTERNATIVES_REGEX = "^[ABCD]\\..*";
    public static final String ANSWER_REGEX = "^\\d+\\. [ABCD]\\..*";

    protected String questionsPath;
    protected String answersPath;

    @Override
    protected void loadPathConstants() {
        this.questionsPath = String.format("%s/%s",
                Objects.requireNonNull(classLoader.getResource(".")).getFile(),
                "book/questions");

        this.answersPath = String.format("%s/%s",
                Objects.requireNonNull(classLoader.getResource(".")).getFile(),
                "book/answers");
    }

    @Override
    protected void loadQuestions() {
        this.buildQuestion(
                new File(this.questionsPath));
        this.buildAnswer(
                new File(this.answersPath));
    }

    private void buildAnswer(File file) {
        try {
            Scanner myReader = new Scanner(file);
            StringBuilder answer = new StringBuilder();
            boolean canUpdate = false;

            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();

                if (canUpdate && line.matches(ANSWER_REGEX)) {
                    this.updateQuestion(answer.toString());
                    answer = new StringBuilder();
                    canUpdate = false;
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

    private void updateQuestion(String answer) {
        Question question = this.seachForQuestion(answer);

    }

    private Question seachForQuestion(String answer) {
        return null;
    }

    @Override
    protected Question questionBuilder(File file) {
        try {
            Scanner myReader = new Scanner(file);
            LineTypeEnum lineTypeEnum = LineTypeEnum.TITLE;
            StringBuilder title = new StringBuilder();
            List<QuestionOption> alternatives = new ArrayList<>();

            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if (line.matches(ALTERNATIVES_REGEX)) {
                    lineTypeEnum = LineTypeEnum.ALTERNATIVES;
                    alternatives.add(new QuestionOption(line, false));

                } else if (line.matches(QUESTION_TITLE_REGEX) && lineTypeEnum == LineTypeEnum.ALTERNATIVES) {
                    this.getQuestion(title, alternatives);

                    // Reset entries
                    title = new StringBuilder(this.getName(file.getName()) + " - " + line.trim());
                    alternatives = new ArrayList<>();
                    lineTypeEnum = LineTypeEnum.TITLE;

                } else if (lineTypeEnum == LineTypeEnum.ALTERNATIVES) {
                    int lastPos = alternatives.size() - 1;
                    alternatives.get(lastPos).setOptionDescription(
                            String.format("%s %s", alternatives.get(lastPos).getOptionDescription(), line.trim()));

                } else if (line.matches(QUESTION_TITLE_REGEX) || lineTypeEnum == LineTypeEnum.TITLE) {
                    title.append(" ").append(line.trim());
                }
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred trying to open file.");
            e.printStackTrace();
        }
        return null;
    }

    private Question getQuestion(StringBuilder title, List<QuestionOption> alternatives) {
        Question question = new Question();
        question.setTitle(title.toString().trim());
        question.setQuestionOptionList(alternatives);
        this.questions.add(question);
        return question;
    }

    private String getName(String fileName) {
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }
}
