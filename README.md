IDE: IntelliJ IDEA Community
Spring Boot 2.6.13
JDK 11
mysql
Spring Data JPA
Thymeleaf
게시판 주요기능
글쓰기(/board/save)
글목록(/board/)
글조회(/board/{id})
글수정(/board/update/{id})
상세화면에서 수정 버튼 클릭
서버에서 해당 게시글의 정보를 가지고 수정 화면 출력
제목, 내용 수정 입력 받아서 서버로 요청
수정 처리
글삭제(/board/delete/{id})
페이징처리(/board/paging)
/board/paging?page=2
/board/paging/2
게시글 14
한페이지에 5개씩 => 3개
한페이지에 3개씩 => 5개
파일(이미지)첨부하기
단일 파일 첨부

다중 파일 첨부

파일 첨부와 관련하여 추가될 부분들

save.html
BoardDTO
BoardService.save()
BoardEntity
BoardFileEntity, BoardFileRepository 추가
detail.html
github에 올려놓은 코드를 보시고 어떤 부분이 바뀌는지 잘 살펴봐주세요.

board_table(부모) - board_file_table(자식)
