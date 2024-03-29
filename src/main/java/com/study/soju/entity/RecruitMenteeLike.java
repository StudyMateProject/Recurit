package com.study.soju.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "RecruitMenteeLike")
public class RecruitMenteeLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private Long likeIdx;

    @Column(nullable = false)
    private Long memberIdx;
}
