package com.diary.dear_my_diary.service;


import com.diary.dear_my_diary.dto.BoardDTO;
import com.diary.dear_my_diary.entity.BoardEntity;
import com.diary.dear_my_diary.entity.BoardFileEntity;
import com.diary.dear_my_diary.repository.BoardFileRepository;
import com.diary.dear_my_diary.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    public final BoardRepository boardRepository;
    public final BoardFileRepository boardFileRepository;
    public void save(BoardDTO boardDTO) throws IOException {
        if (boardDTO.getBoardFile().isEmpty()) {
            // 첨부파일없음
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
            //첨부파일이 없는경우 toSaveEntity 호출 dto를 entity로 변환해서 save처리
        } else {
// 첨부파일있음
/*
 1. DTO에 담긴 파일을 꺼냄
 2. 파일의 이름 가져옴
 3. 서버 저장용 이름을 만듦
 // 내사진.jpg => 839798375892_내사진.jpg
  4. 저장 경로 설정
  5. 해당 경로에 파일 저장
  6. board_table에 해당 데이터 save 처리
  7. board_file_table에 해당 데이터 save 처리
*/
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            // 저장하기. 저장하고나서 getId 아이디 값을 가져오는이유 부모자식간의 관계를 맺어놔서 , 부모의 번호가 필요해서 부모테이블의 pk값이 필요?
            Long saveId =boardRepository.save(boardEntity).getId();
            //부모엔티티 자체가 전달이되어야하고, 디비로부터 가지고옴 .boardEntity 는 디비에 저장하기전이라서 아이디 값이 없음
            BoardEntity board = boardRepository.findById(saveId).get();
            //반복문으로 파일을 가져옴
            for(MultipartFile boardFile:boardDTO.getBoardFile()){
                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                String savePath="/Users/u020/springboot_img/"+storedFileName;
                boardFile.transferTo(new File(savePath));
                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }

            // 사진한개
//            MultipartFile boardFile = boardDTO.getBoardFile();
//            // 사용자가 올린 파일이름
//            String originalFilename = boardFile.getOriginalFilename();
//            //서버저장용이름만들기 (겹치면안됨)
//            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
//            String savePath="/Users/u020/springboot_img/"+storedFileName;
////          말그대로 (new File(savePath) 여기로 넘긴다  throws IOException
//            boardFile.transferTo(new File(savePath));
//            //dto를 entity로 변환해서 보드테이블에 저장 , 그리고 보드파일에 저장
//
//            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
//            // 저장하기. 저장하고나서 getId 아이디 값을 가져오는이유 부모자식간의 관계를 맺어놔서 , 부모의 번호가 필요해서 부모테이블의 pk값이 필요?
//            Long saveId =boardRepository.save(boardEntity).getId();
//            //부모엔티티 자체가 전달이되어야하고, 디비로부터 가지고옴 .boardEntity 는 디비에 저장하기전이라서 아이디 값이 없음
//            BoardEntity board = boardRepository.findById(saveId).get();
//
//
//            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
//            boardFileRepository.save(boardFileEntity);
            //레포는 기본 엔티티로 받고 반환
            //디티오를 엔티티로 변환,엔티티를 디티오로
            //컨트롤러로 줄때는 디티오로 주고 . 레포지토리로 줄때는 엔티티
            // DTO->Entity ( Entity class 에서)
            // Entity->DTO 변환할때는 (DTO class 에서)
        }

    }

    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        //엔티티 객체를 디티오객체로 변환해야함
        //반복문으로 담아서 컨트롤러로
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));

        }
        return boardDTOList;
    }

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);

    }

    public void update(BoardDTO boardDTO) throws IOException {
        if (boardDTO.getBoardFile().isEmpty()) {
            BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
            boardRepository.save(boardEntity);
//        return findById(boardDTO.getId());
        } else {

            List<MultipartFile> boardFile = boardDTO.getBoardFile(); // 1.
            String originalFilename = boardFile.getOriginalFilename(); // 2.
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3.
            String savePath = "C:/springboot_img/" + storedFileName; // 4. C:/springboot_img/9802398403948_내사진.jpg
//            String savePath = "/Users/사용자이름/springboot_img/" + storedFileName; // C:/springboot_img/9802398403948_내사진.jpg
            boardFile.transferTo(new File(savePath)); // 5.
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(savedId).get();

            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
            boardFileRepository.save(boardFileEntity);
        }

    }
//    public BoardDTO updata(BoardDTO boardDTO) {
//        //엔티티로 변환작업
//        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
//        boardRepository.save(boardEntity);
//        return findById(boardDTO.getId());
//        //해당 게시글의 상세조회값으 넘겨줘야하니까, 위에 findById가 있으니까 재사용
//     /* 스프링데이터 jpa는 save 메서드로 insert와 update 둘다가능
//     id값으로 대응함. ( id 값이 있으면 업데이트 )
//      */
//
//
//    }
//public BoardDTO updata(BoardDTO boardDTO, List<MultipartFile> boardFiles) throws IOException {
//    if (boardFiles != null && !boardFiles.isEmpty()) {
//        // 파일이 업로드되었을 경우
//        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
//        Long saveId = boardRepository.save(boardEntity).getId(); // 엔티티를 저장하고 ID를 얻어옴
//
//        // 파일 업로드 및 저장
//        List<String> originalFileNames = new ArrayList<>();
//        List<String> storedFileNames = new ArrayList<>();
//
//        for (MultipartFile boardFile : boardFiles) {
//            String originalFilename = boardFile.getOriginalFilename();
//            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
//            String savePath = "/Users/u020/springboot_img/" + storedFileName;
//            boardFile.transferTo(new File(savePath));
//
//            // 파일 엔티티 생성 및 저장
//            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(
//                    boardRepository.findById(saveId).get(), originalFilename, storedFileName);
//            boardFileEntity = boardFileRepository.save(boardFileEntity);
//
//            originalFileNames.add(originalFilename);
//            storedFileNames.add(storedFileName);
//        }
//
//        boardDTO.setOriginalFileName(originalFileNames);
//        boardDTO.setStoredFileName(storedFileNames);
//    } else {
//        // 파일이 업로드되지 않았을 경우
//        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
//        boardRepository.save(boardEntity);
//    }
//
//    // 엔티티로 변환작업
//    BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
//    boardRepository.save(boardEntity);
//
//    return findById(boardDTO.getId());
//}
//



    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3; // 한 페이지에 보여줄 글 갯수
        // 한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 목록: id, writer, title, hits, createdTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreateTime()));
        return boardDTOS;
    }
}