package com.ematos.gcpa.exame.business.loader.rules;

import com.ematos.gcpa.exame.model.Question;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.stream.Collectors;

@Rule(name="Cleaner", description = "Check which chapter the question is related to.")
public class CleanLabelRule {
    @Condition
    public boolean checkSourceRule(@Fact("question") Question question) {
        return question.getLabels().stream().anyMatch(s -> s.contains("_question"));
    }

    @Action
    public void updateSourceLabel(@Fact("question") Question question) {
        List<String> toBeRemoved = question.getLabels().stream()
                .filter(s -> s.contains("_question")).collect(Collectors.toList());

        toBeRemoved.forEach(question::removeLabel);
    }
}
