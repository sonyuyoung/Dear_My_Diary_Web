package com.diary.dear_my_diary.entity;


import com.diary.dear_my_diary.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.lang.reflect.Member;

@Entity
@Setter
@Getter
@Table(name = "member_table")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String memberEmail;
    @Column
    private String memeberPassword;
    @Column
    private String memeberName;

    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        //클래스 메서드
        //디티오에 담긴걸 엔티티로 넘기는작업
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemeberName(memberDTO.getMemberName());
        memberEntity.setMemeberPassword(memberDTO.getMemberPassword());
        return memberEntity;

    }
    public static MemberEntity toUpdateMemberEntity(MemberDTO memberDTO) {
        //클래스 메서드
        //디티오에 담긴걸 엔티티로 넘기는작업
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemeberName(memberDTO.getMemberName());
        memberEntity.setMemeberPassword(memberDTO.getMemberPassword());
        return memberEntity;

    }
}
