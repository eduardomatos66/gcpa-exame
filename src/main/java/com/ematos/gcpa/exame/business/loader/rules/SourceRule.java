package com.ematos.gcpa.exame.business.loader.rules;

import com.ematos.gcpa.exame.model.Question;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name="Source", description = "Check which source the question is related to.")
public class SourceRule {

    @Condition
    public boolean checkSourceRule(@Fact("question") Question question) {
        return question.getSource() != null && !question.getSource().isEmpty();
    }

    @Action
    public void updateSourceLabel(@Fact("question") Question question) {
        String source = question.getSource();
        String label = "";

        if (source.contains("bk_arch")) {
            label = "Architect";
        } else if (source.contains("bk_assoc")) {
            label = "Associate";
        } else if (source.contains("assoc")) {
            label = "Associate";
        } else if (source.contains("arch")) {
            label = "Architect";
        }
        question.addLabel(label);
    }
}
