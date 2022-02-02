package com.ematos.gcpa.exame.business.loader;

import com.ematos.gcpa.exame.model.Question;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractLoader {

    protected ResourceLoader resourceLoader;
    protected Logger LOG = Logger.getLogger(AbstractLoader.class.getName());
    protected final ClassLoader classLoader;
    protected Resource[] questionsResource;
    public List<Question> questions = new ArrayList<>();

    public AbstractLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.classLoader = AbstractLoader.class.getClassLoader();
        this.loadPathConstants();
        this.loadQuestions();
        this.analyzeQuestions();
    }

    protected abstract void loadPathConstants();

    protected abstract void loadQuestions();

    protected abstract void questionBuilder(Resource resource) throws IOException;

    public List<Question> getQuestions() {
        return questions;
    }

    protected void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    private void analyzeQuestions() {
        LabelRulesExecutor.executeRulesOnQuestion(this.questions);
    }
}
