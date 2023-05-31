package com.berezovskiy.diploma.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "datasets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dataset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean isTrainedOn;
    private byte [] accidentSeverityPredictionClassifier;
    private byte [] casualtiesNumberPredictionClassifier;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL)
    private List<Prediction> predictions;

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL)
    private List<Accident> accidents;

    public Dataset(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return title;
    }
}