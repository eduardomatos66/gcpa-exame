package com.ematos.gcpa.exame.model;

import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    private String source;

    @ElementCollection
    private List<QuestionOption> questionOptionList;

    @ElementCollection
    private Set<String> labels;

    private void setLabels(String label) {}

    public void addLabel(String label) {
        if (this.labels == null) {
            this.labels = new HashSet<>();
        }
        this.labels.add(label);
    }

    public void removeLabel(String label) {
        this.labels.remove(label);
    }

}
