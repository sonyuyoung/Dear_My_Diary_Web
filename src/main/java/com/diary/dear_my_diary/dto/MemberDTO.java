package com.diary.dear_my_diary.dto;


import com.diary.dear_my_diary.entity.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString   //디티오 객체가 가지고있는 필드값을 출력할때 tostring 사용
//회원정보에 필요한 내용들을 필드로 정의하고 ,
public class MemberDTO {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;


    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberName(memberEntity.getMemeberName());
        memberDTO.setMemberPassword(memberEntity.getMemeberPassword());
        return memberDTO;
    }


}
