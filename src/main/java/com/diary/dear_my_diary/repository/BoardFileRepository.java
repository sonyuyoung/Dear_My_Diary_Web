package com.diary.dear_my_diary.repository;

import com.diary.dear_my_diary.entity.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Long> {
    // boardId에 해당하는 파일을 삭제하기 위한 사용자 정의 쿼리
    void deleteByBoardEntityId(Long boardId);

    // boardId에 해당하는 파일을 조회하기 위한 사용자 정의 쿼리
    List<BoardFileEntity> findByBoardEntityId(Long boardId);
}