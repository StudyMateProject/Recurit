package com.study.soju.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "RecruitMentor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitMentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 10, nullable = false)
    private String writeDate;

    @Column(length = 20, nullable = false)
    private String writer;

    @Column(length = 20, nullable = false)
    private String studyType;

    @Column(length = 500, nullable = false)
    private String studyIntro;

    @Column(nullable = false)
    private int recruiting;

    @Column(nullable = false)
    private int studyLike;

    @Column(nullable = false)
    private int studyLikeCheck;
}
