package com.diary.dear_my_diary.dto;


import com.diary.dear_my_diary.entity.BoardEntity;
import com.diary.dear_my_diary.entity.BoardFileEntity;
import javassist.expr.NewArray;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//DTO(data transfer object),vo ,Bean
@Getter
@Setter
@ToString
@NoArgsConstructor
//기본생성자
@AllArgsConstructor
//모든필드를 매개변수로 하는 생성자
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;

    //조회수
    private LocalDateTime boardCreatedTime;
    //게시글작성시간
    private LocalDateTime boardUpdatedTime;
    //수정시간

    //파일여러개
    private List<MultipartFile> boardFile; // save.html -> Controller 파일 담는 용도
//    private MultipartFile boardFile; // save.html -> Controller 파일 담는 용도
    //가져가야하는 파일도 여러게

//    private String originalFileName; // 원본 파일 이름
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 서버 저장용 파일 이름
//    private String storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0) : 바이트도 가능, 불타입도 가능 (복잡함)

    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    // 디티오로 변환해서 가져올때
    public static BoardDTO toBoardDTO (BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreateTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdateTime());
        if(boardEntity.getFileAttached()==0){
            boardDTO.setFileAttached(boardEntity.getFileAttached());
        }else{
            // 파일가져올때
            // 파일이 있는경우
            // 다중파일
            // 전달 하기위해 리스트로 ..
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 1
            // 단일파일
//            boardDTO.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());
//            boardDTO.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());
            for (BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()) {
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
            // 파일 이름을 가져가야 함.
            // orginalFileName, storedFileName : board_file_table(BoardFileEntity)
            // join
            // select * from board_table b, board_file_table bf where b.id=bf.board_id
            // and where b.id=?
        }
        return boardDTO;
    }
}
