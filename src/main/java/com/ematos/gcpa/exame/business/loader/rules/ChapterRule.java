package com.ematos.gcpa.exame.business.loader.rules;

import com.ematos.gcpa.exame.model.Question;
import com.google.common.collect.Sets;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Rule(name="Chapter", description = "Check which chapter the question is related to.")
public class ChapterRule {
    private static final Pattern pattern = Pattern.compile("^ch(\\d+)_.*");

    @Condition
    public boolean checkChapterRule(@Fact("question") Question question) {

        if (question.getLabels() != null) {
            return question.getLabels()
                    .stream().anyMatch(e -> pattern.matcher(e).matches());
        }
        return false;
    }

    @Action
    public void updateQuestionLabel(@Fact("question") Question question) {
        String[] labels = question.getLabels().toArray(new String[0]);
        for (int i = 0; i < labels.length; i++) {
            Matcher m = pattern.matcher(labels[i]);
            if (m.find()) {
                labels[i] = String.format("Chapter%s", m.group(1));
            }
        }
        question.setLabels(Sets.newHashSet(labels));
    }
}
