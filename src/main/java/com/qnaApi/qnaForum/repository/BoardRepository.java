package com.qnaApi.qnaForum.repository;

import com.qnaApi.qnaForum.entity.QnaForum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<QnaForum,Long> {
    //레포지토리는 저장을 하는테 리스트처럼 어떤 타입을 저장할지 같은기능 <형, 엔티티식별자형>

}
