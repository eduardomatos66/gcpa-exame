package com.ematos.gcpa.exame.business.loader;

import com.ematos.gcpa.exame.business.loader.rules.ChapterRule;
import com.ematos.gcpa.exame.business.loader.rules.CleanLabelRule;
import com.ematos.gcpa.exame.business.loader.rules.SourceRule;
import com.ematos.gcpa.exame.model.Question;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.api.RulesEngineParameters;
import org.jeasy.rules.core.DefaultRulesEngine;

import java.util.List;

public class LabelRulesExecutor {

    public static void executeRulesOnQuestion(List<Question> questionList) {
        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(false);
        RulesEngine rulesEngine = new DefaultRulesEngine(parameters);

        // create rules
        Rules rules = new Rules();
        rules.register(new ChapterRule());
        rules.register(new SourceRule());
        rules.register(new CleanLabelRule());

        for (Question question: questionList) {
            // fire rules
            Facts facts = new Facts();
            facts.put("question", question);

            rulesEngine.fire(rules, facts);
        }
    }

}
