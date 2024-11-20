package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.entity.Board;

import java.util.List;

public interface BoardService {

    // Board는 bno, title, writer, content만 있다. 그러나 화면에 보여주려면 registerTime과 modifyTime도 필요하다.
    // 화면에서 가져온 BoardDTO 객체를 DB에 저장하기 위해 Board로 변환할 수 있어야 한다.
    // DB에서 가져온 Board 객체를 화면에 보여주기 위해 BoardDTO로 변환할 수 있어야 한다.
    // 인터페이스의 default 메서드로 이 변환을 구현할 수 있다.

    default Board convertDtoToEntity(BoardDTO boardDTO) {
        return Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .writer(boardDTO.getWriter())
                .content(boardDTO.getContent())
                .build();
    }

    default BoardDTO convertEntityToDto(Board board) {
        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .writer(board.getWriter())
                .content(board.getContent())
                .registerTime(board.getRegisterTime())
                .modifyTime(board.getModifyTime())
                .build();
    }

    Long insert(BoardDTO boardDTO);

    List<BoardDTO> getList();

    BoardDTO getDetail(Long bno);

    Long modify(BoardDTO boardDTO);

    int delete(Long delBno);
}
