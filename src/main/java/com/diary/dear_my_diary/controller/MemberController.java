package com.diary.dear_my_diary.controller;


import com.diary.dear_my_diary.dto.MemberDTO;
import com.diary.dear_my_diary.service.MemberService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    //생성자주입

    //회원가입페이지출력요청
    //링크를 클릭하는것은 Http메서드에서에서 get메서드를 사용한 요청이다
    //겟, 포스트, 딜리트 등 .
//1.회원가입 페이지를 띄워주는 메서드 : 주소는있는데 방식이 다른상황 405오류 . 주소가 없으면 404
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    //받아주는 메서드 : 처리 RequestParam 을 통해 memberEmail 값을 String memberEmail 에 담는다 라고 생각하기
//    @PostMapping("/member/save")
//    public String save(@RequestParam("memberEmail")String memberEmail,@RequestParam("memberPassword")String memberPassword,@RequestParam("memberName")String memberName) {
////      요청가는지확인 soutm  System.out.println("MemberController.save");
//        //soutp
//        System.out.println("memberEmail = " + memberEmail + ", memberPassword = " + memberPassword + ", memberName = " + memberName);
//        return "index";
//    }
    //@ModelAttribute MemberDTO memberDTO 필드값이 담겨서 옴.ModelAttribute 사용안해도되긴함
    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
//    MemberService memberService = new MemberService();
//    memberService.save();
        //이방식을 쓰지않고 , 생성자주입을 사용할것 . private final MemberService memberService;
        //service 디티오객체로 받아와서 서비스클래스로 디티오 객체를넘김
        memberService.save(memberDTO);
//    System.out.println("memberDTO = " + memberDTO);
        return "login";
    }
    // save에서 입력한 값이 컨트롤러로 넘어와서
    // -> 서비스로 넘겼고
    // -> 서비스에서는 디티오객체를 엔티티로 변환해서
    // 엔티티객체를 레포지토리에 세이브메서드로 넘겼다
    // jpa 쓰기위해서는 엔티티 객체를 넘겨주어야한다.
    //서비스에서 엔티티를 디티오로 디티오를 엔티티로 많이 넘겨주게될것임.

    @GetMapping("/member/login")
    public String loginForm() {
        return "login";

    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "index";
        } else {
            return "login";
        }
    }

    @GetMapping("/member/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        // 어떠한 html 로 가져갈 데이터가있다면 모델사용
        model.addAttribute("memberlist", memberDTOList);
        return "list";
    }

    @GetMapping("/member/{id}")
//PathVariable {id} 이렇게 받아옴
    public String findById(@PathVariable Long id, Model model) {
        //한명의 회원
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/member/" + memberDTO.getId();
    }

    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @PostMapping("/member/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail=" + memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
//        if (checkResult != null) {

        }
//            return "ok";
//        } else {
//            return "no";
//        }

}
