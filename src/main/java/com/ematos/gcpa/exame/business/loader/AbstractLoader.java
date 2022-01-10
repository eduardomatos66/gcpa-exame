package com.ematos.gcpa.exame.business.loader;

import com.ematos.gcpa.exame.model.Question;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractLoader {

    protected final ClassLoader classLoader;

    public List<Question> questions = new ArrayList<>();

    public AbstractLoader() {
        this.classLoader = AbstractLoader.class.getClassLoader();
        this.loadPathConstants();
        this.loadQuestions();
    }

    protected abstract void loadPathConstants();

    protected abstract void loadQuestions();

    protected abstract Question questionBuilder(File file);

    protected void buildQuestion(File resourceFolder) {
        for (File f : Objects.requireNonNull(resourceFolder.listFiles())) {
            Question q = this.questionBuilder(f);
            if (q != null) {
                this.questions.add(q);
            }
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }

    protected void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

}
