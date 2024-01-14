package com.diary.dear_my_diary.repository;

import com.diary.dear_my_diary.entity.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Long> {
}