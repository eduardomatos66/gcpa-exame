package com.ematos.gcpa.exame.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QuestionOption {

    @Column(length = 2000)
    private String optionDescription;
    private boolean isCorrect;
}
