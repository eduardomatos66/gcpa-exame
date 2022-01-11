package com.ematos.gcpa.exame.business.loader;

import com.ematos.gcpa.exame.model.Question;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    protected abstract void questionBuilder(File file);

    public List<Question> getQuestions() {
        return questions;
    }

    protected void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
