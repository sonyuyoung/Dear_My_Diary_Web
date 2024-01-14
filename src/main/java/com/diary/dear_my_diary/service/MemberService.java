package com.diary.dear_my_diary.service;


import com.diary.dear_my_diary.dto.MemberDTO;
import com.diary.dear_my_diary.entity.MemberEntity;
import com.diary.dear_my_diary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {
        //컨트롤러에서 디티오 객체를 받아옴.
        //repository의 save메서드 호출( 조건: entity 객체를 넘겨주어야함)

        //1. dto 객체를 -> entity 객체로 변환
        //방식은 다양하나 , ( 엔티티클래스에 변환하는 팩토리메서드를쓰거나, 서비스에 별도의 메서드를 하나두고 바꾸는방법등 )
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
        //2.repository 의 save 메서드 호출 -> jpa 기능. 인서트 .
    }

    public MemberDTO login(MemberDTO memberDTO) {
        // 1. 회원이 입력한 이메일로 디비에서 조회를 함
        // 2. 디비에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단

        // 디티오에 담겨있는 이메일값을 넘겨주고 옵셔널 객체로 리턴받는다.
        // 우리가 실제로 사용할 객체는 엔티티 객체고 옵셔널 안에 엔티티가 한번더 감싸져있다고 생각하면됨
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (byMemberEmail.isPresent()) {
            //조회결과가있다( 해당이메일을 가진회원정보가있다 )
            MemberEntity memberEntity = byMemberEmail.get();
            if (memberEntity.getMemeberPassword().equals(memberDTO.getMemberPassword())) {
                //비밀번호가 일치
                //컨트롤러에는디티오객체를 사용해야함 , 엔티티객체를 디티오로 변환후 리턴해주어야함 .
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                return null;    //비밀번호 불일치
            }
        } else {      //조회결과가없다 ( 해당이메일을 가진회원이없다 )
            return null;
        }


    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        //리스트 객체는 entity로 가지고옴 -> dto로 변환해야함
        List<MemberDTO> memberDTOList = new ArrayList<>();
        // 엔티티객체를 하나하나 꺼내서 디티오 리스트로 다시담는 과정
        for (MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        //하나의 객체를 조회할때 Optional
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());

        }else{
            return null;
        }
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if(optionalMemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }else {
            return null;
        }

    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public String emailCheck(String memberEmail) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if (byMemberEmail.isPresent()) {
//        조회결과가 있다 - > 사용할수없다
            return null;
        } else {
            return "ok";
        }
    }}