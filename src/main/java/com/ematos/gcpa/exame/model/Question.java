package com.ematos.gcpa.exame.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question")
public class Question {
    @Id
    @SequenceGenerator(
            name="question_sequence",
            sequenceName="question_sequence",
            allocationSize=1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "question_sequence"
    )
    private Long id;
    @Column(length = 3000)
    private String title;
    private String subject;
    @Column(length = 3000)
    private String explanation;

    @ElementCollection
    private List<QuestionOption> questionOptionList;
}
