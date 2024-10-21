package com.ematos.gcpa.exame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
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
    private String level;

    @ElementCollection
    private List<QuestionOption> questionOptionList;

    @ElementCollection
    private Set<String> labels;

    public void addLabel(String label) {
        if (this.labels == null) {
            this.labels = new HashSet<>();
        }
        this.labels.add(label);
    }

    public void removeLabel(String label) {
        this.labels.remove(label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Question question = (Question) o;
        return id != null && Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
