package com.diary.dear_my_diary.controller;


import com.diary.dear_my_diary.dto.BoardDTO;
import com.diary.dear_my_diary.entity.BoardFileEntity;
import com.diary.dear_my_diary.repository.BoardFileRepository;
import com.diary.dear_my_diary.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    /*
    게시판 주요기능
    글쓰기 (/board/save)
    글목록(/board/)
    글조회(/board/{id})
    글수정(/board/update/{id})
    : 상세화면에서 수정버튼 클릭 , 서버에서 해당 게시글의 정보를가지고
      수정화면출력, 제목 내용 수정 입력받아서 서버로 요청 , 수정처리
      글삭제(/board/delete/{id})
      페이징처리 (/board/paging)
      */
    private final BoardService boardService;

    @GetMapping("/board/save")
    public String saveForm() {

        return "board/save";
    }

    @PostMapping("/board/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("/board")
    public String findAll(Model model) {
        //db에서 전체 게시글 데이터를 가져와서 리스트에보여준다
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "board/list";
    }

    @GetMapping("/board/{id}")
    public String findById(@PathVariable Long id, Model model,@PageableDefault(page=1)Pageable pageable ) {
        //해당 게시글이 조회수를 하나 올리고 게시글데이터를 가져와서 디테일html 로 출력
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "board/detail";
    }

    @GetMapping("/board/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "board/update";
    }

    @PostMapping("/board/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) throws IOException {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "board/detail";
//         return "redirect:/board/"+boardDTO.getId();
        // 이렇게도 할수있는데 이렇게하면 수정후 조회수까지 같이올라가서
        //수정하고 난뒤에 수정된 상세페이지로 이동

    }

//    @PostMapping("/board/update")
//    public String update(@ModelAttribute BoardDTO boardDTO,
//                         @RequestParam("boardFile") List<MultipartFile> boardFiles,
//                         Model model) {
//        try {
//            BoardDTO updatedBoard = boardService.updata(boardDTO, boardFiles);
//            model.addAttribute("board", updatedBoard);
//            return "board/detail";
//        } catch (IOException e) {
//            // 파일 처리 중 예외가 발생한 경우 여기에 대한 예외 처리 로직을 추가할 수 있습니다.
//            e.printStackTrace(); // 예외 처리는 로그로 기록하거나 적절한 방식으로 처리해야 합니다.
//            model.addAttribute("error", "파일 처리 중 오류가 발생했습니다.");
//            return "error-page"; // 파일 처리 중 오류가 발생한 경우 에러 페이지로 리다이렉트
//        }
//    }


    //    @Transactional(readOnly = true)
//    public BoardDTO getBoradDtl(Long boardId){
//        List<BoardFileEntity> BoardImgList= BoardFileRepository.findByBoardIdOrderByIdAsc(boardId);
//        List<BoardDTO> BoardImgDtoList  =new ArrayList<>();
//        for (BoardFileEntity boaradFileEntity : boar){
//            BoardDTO boardDTO = BoardDTO.of(BoardFileEntity);
//            BoardImgList.add(boardImg);
//        }
//
//    }
//@PostMapping("/board/update")
//public String update(@ModelAttribute BoardDTO boardDTO, Model model,
//                     @RequestParam(value = "files", required = false) List<MultipartFile> files) {
//    try {
//        boardDTO.setBoardFile(files); // 이미지를 DTO에 설정
//        BoardDTO updatedBoard = boardService.update(boardDTO); // 이미지를 포함하여 업데이트
//
//        model.addAttribute("board", updatedBoard);
//        return "board/detail";
//    } catch (IOException e) {
//        // 파일 처리 중 예외가 발생한 경우 여기에 대한 예외 처리 로직을 추가할 수 있습니다.
//        e.printStackTrace(); // 예외 처리는 로그로 기록하거나 적절한 방식으로 처리해야 합니다.
//        model.addAttribute("error", "파일 처리 중 오류가 발생했습니다.");
//        return "errorPage"; // 파일 처리 중 오류가 발생한 경우 에러 페이지로 리다이렉트
//    }
//}
    @GetMapping("/board/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/";
    }
//    @GetMapping("board/paging")
//    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
////        pageable.getPageNumber();
//        Page<BoardDTO> boardList = boardService.paging(pageable);
//        int blockLimit = 3;
////        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
//        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() + 1 / blockLimit))) - 1) * blockLimit + 1;
//        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();
//
//        // page 갯수 20개
//        // 현재 사용자가 3페이지
//        // 1 2 3
//        // 현재 사용자가 7페이지
//        // 7 8 9
//        // 보여지는 페이지 갯수 3개
//        // 총 페이지 갯수 8개
//
//        model.addAttribute("boardList", boardList);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
////        return "redirect:/board";
//        return "board/paging";
//    }
    }