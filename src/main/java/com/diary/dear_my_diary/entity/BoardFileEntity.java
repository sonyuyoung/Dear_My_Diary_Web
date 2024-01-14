package com.diary.dear_my_diary.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
/*
* boar_table(부모)
- board_file_table(자식)
 : 게시글하나에여러개 파일, 하나의 파일에 여러개의게시물 x    */
//create table board_table
//        (
//        id             bigint auto_increment primary key,
//        created_time   datetime     null,
//        updated_time   datetime     null,
//        board_contents varchar(500) null,
//        board_hits     int          null,
//        board_pass     varchar(255) null,
//        board_title    varchar(255) null,
//        board_writer   varchar(20)  not null,
//        file_attached  int          null
//        );
//
//        create table board_file_table
//        (
//        id                 bigint auto_increment primary key,
//        created_time       datetime     null,
//        updated_time       datetime     null,
//        original_file_name varchar(255) null,
//        stored_file_name   varchar(255) null,
//        board_id           bigint       null,
//        constraint FKcfxqly70ddd02xbou0jxgh4o3
//        foreign key (board_id) references board_table (id) on delete cascade
//        );

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;
//    BoardFileEntity를기준으로 board와의 관계
//    게시물과 파일의 관계  1:N
    // boardFile의 기준에서는 N:1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    //부모엔티티값으로적어줘야함 근데 또 나중에 board_id만들어가있음
    private BoardEntity boardEntity;




// String originalFileName, String storedFileName -> 컬럼에 추가 될까바
    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        //pk 값이아니라 부모 엔티티 값을 넘겨줘야함
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }
}