package com.diary.dear_my_diary.repository;

import com.diary.dear_my_diary.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//데이터베이스와 .. 소통하는 클래스
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    //이메일로 회원정보 조회 (select * from member_table where member_email=?)
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
//    Optional<MemberEntity> findById(Long id);
    // optional - > null방지 옵셔널에 감싸서 객체를 넘겨줌
    //레포지토리에서 주고받는 객체는 전부 엔티티객체 .. 엔티티객체로 리턴받는다
}
