package com.study.soju.repository;

import com.study.soju.entity.RecruitMenteeComment;
import com.study.soju.entity.RecruitMentorComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitMenteeCommentRepository extends JpaRepository<RecruitMenteeComment, Object> {
    //멘토프로필 idx 로 멘토프로필 검색
    RecruitMenteeComment findByIdx(long idx);
    //멘토프로필 idx 로 리스트 검색
    List<RecruitMenteeComment> findByCommentIdx(long commentIdx);
}
