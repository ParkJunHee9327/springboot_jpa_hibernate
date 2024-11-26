package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.dto.BoardFileDTO;
import com.ezen.boot_JPA.dto.FileDTO;
import com.ezen.boot_JPA.entity.Board;
import com.ezen.boot_JPA.entity.File;
import org.springframework.data.domain.Page;

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

    // File 객체용 converter다.
    // FileDTO를 File Entity로, File Entity를 FileDTO로 변환한다.
    default File convertDtoToEntity(FileDTO fileDTO) {
        return File.builder()
                .uuid(fileDTO.getUuid())
                .bno(fileDTO.getBno())
                .saveDir(fileDTO.getSaveDir())
                .fileName(fileDTO.getFileName())
                .fileSize(fileDTO.getFileSize())
                .fileType(fileDTO.getFileType())
                .build();
    }

    default FileDTO convertEntityToDto(File file) {
        return FileDTO.builder()
                .uuid(file.getUuid())
                .bno(file.getBno())
                .saveDir(file.getSaveDir())
                .fileName(file.getFileName())
                .fileSize(file.getFileSize())
                .fileType(file.getFileType())
                .registerTime(file.getRegisterTime())
                .modifyTime(file.getModifyTime())
                .build();
    }

    Long insert(BoardDTO boardDTO);

    long insert(BoardFileDTO boardFileDTO);

//    BoardDTO getDetail(Long bno);

    BoardFileDTO getDetail(Long bno);

//    Long modify(BoardDTO boardDTO);

    Long modify(BoardFileDTO boardFileDTO);

    int delete(Long delBno);

//    List<BoardDTO> getList();

    Page<BoardDTO> getList(int pageNum, String type, String keyword);

    Long deleteFile(String uuid);

    List<FileDTO> getAllFile();
}
