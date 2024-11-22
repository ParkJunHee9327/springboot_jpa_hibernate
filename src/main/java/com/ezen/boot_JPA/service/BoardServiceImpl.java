package com.ezen.boot_JPA.service;


import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.dto.BoardFileDTO;
import com.ezen.boot_JPA.dto.FileDTO;
import com.ezen.boot_JPA.entity.Board;
import com.ezen.boot_JPA.entity.File;
import com.ezen.boot_JPA.repository.BoardRepository;
import com.ezen.boot_JPA.repository.FileRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@lombok.extern.slf4j.Slf4j
@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    @Override
    public Long insert(BoardDTO boardDTO) {
        // DB에 저장할 객체는 Board이다.
        // save() 메서드: insert한 후 저장 객체의 id를 반환한다. Entity 객체만 파라미터로 받아서 DTO를 사용할 수 없다.
        return boardRepository.save(convertDtoToEntity(boardDTO)).getBno(); // DTO를 Board로 변환해서 건네준 후 기본키인 bno를 받아온다.
    }

    @Override
    public long insert(BoardFileDTO boardFileDTO) {
        long bno = insert(boardFileDTO.getBoardDTO());
        log.info("bno: {}", bno);
        // 쿼리가 수행되었다면 bno는 0보다 클 것이다.
        if(bno > 0 && boardFileDTO.getFileDTOList() != null) {
            for (FileDTO fileDTO : boardFileDTO.getFileDTOList()) {
                fileDTO.setBno(bno);
                bno = fileRepository.save(convertDtoToEntity(fileDTO)).getBno();
            }
        }
        return bno;
    }


    // 페이지네이션이 추가되어 기존의 list 메서드를 사용하지 않는다.
//    @Override
//    public List<BoardDTO> getList() {
//        // controller로 보내야 하는 반환값은 List<BoardDTO>이다.
//        // 그러나 DB에서 가져오는 List는 List<Board>다.
//        // 따라서 변환 작업이 필요하다.
//        // 모두 가져와야 하니 findAll() 메서드를 사용한다. 정렬은 Sort.by(Sort.Direction.DESC, "정렬 기준 컬럼 이름")으로 하면 된다.
//        List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "bno"));
//
//        // 변환 방법 1
////        List<BoardDTO> boardDTOList = new ArrayList<>();
////        for(Board board : boardList) {
////            boardDTOList.add(convertEntityToDto(board));
////        }
//
//        // 변환 방법 2
//        List<BoardDTO> boardDTOList = boardList.stream().map(b -> convertEntityToDto(b)).toList();
//
//        return boardDTOList;
//    }

    @Override
    public Page<BoardDTO> getList(int pageNum) {
        // 한 페이지에, 글을 10개 띄우고, bno 기준으로 내림차순으로 페이지네이션 한다.
        // pageNum이 0부터 시작한다. pageNum이 0이면 DB에서 글을 가져올 때 limit 0, 10이 된다.
        // pageNum이 1이면 limit 10, 10이 된다.
        Pageable pageable = PageRequest.of(pageNum, 10, Sort.by("bno").descending());
        // pageble에 따라 DB에서 값을 추출한 뒤 boardList에 데이터를 넣는다.
        Page<Board> boardList = boardRepository.findAll(pageable);
        // Page는 자체적으로 stream의 성질이 있어서 .toList()를 붙일 필요가 없다.
        Page<BoardDTO> boardDTOList = boardList.map(b -> convertEntityToDto(b));
        return boardDTOList;
    }

    @Override
    public Long deleteFile(String uuid) {
        Optional<File> optionalResult = fileRepository.findById(uuid);
        if(optionalResult.isPresent()) {
            fileRepository.deleteById(uuid);
            return optionalResult.get().getBno();
        }
        return null;
    }

    @Override
    public List<FileDTO> getAllFile() {
        List<File> fileList = fileRepository.findAll();
        List<FileDTO> fileDTOList = new ArrayList<>();
        for(File file : fileList) {
            fileDTOList.add(convertEntityToDto(file));
        }
        return fileDTOList;
    }


    // 파일 기능이 추가됨에 따라 기존의 getDetail은 사용하지 않게 되었다.
//    @Override
//    public BoardDTO getDetail(Long bno) {
//        // findById: 기본키를 주고 해당 객체를 반환한다.
//        // findById는 Optional<Object>로 반환한다.
//        // Optional<T>: nullPointException이 발생하지 않도록 도와준다.
//        // Optional.isEmpty(): null일 경우 확인 가능하다. true, false로 반환한다.
//        // Optional.isPresent(): 값이 있는지 확인한다. true, false로 반환한다.
//        // Optional.get(): 객체를 가져온다.
//        Optional<Board> optionalBoard = boardRepository.findById(bno);
//        if(optionalBoard.isPresent()) {
//            BoardDTO boardDTO = convertEntityToDto(optionalBoard.get());
//            return boardDTO;
//        } else {
//            return null;
//        }
//    }

    @Override
    public BoardFileDTO getDetail(Long bno) {
        // file bno에 일치하는 모든 파일 리스트를 가져와야 한다.
        // select * from file where bno = #{bno}가 되어야 한다. bno는 기본키가 아니다.
        List<File> fileList = fileRepository.findFileListByBno(bno);
        List<FileDTO> fileDTOList = new ArrayList<>();
        for(File file : fileList) {
            fileDTOList.add(convertEntityToDto(file));
        }

        Optional<Board> optionalBoard = boardRepository.findById(bno);
        if(optionalBoard.isPresent()) {
            BoardDTO boardDTO = convertEntityToDto(optionalBoard.get());
            return new BoardFileDTO(boardDTO, fileDTOList);
        }

        return null;
    }

    // 파일이 추가되어 기존의 modify를 사용하지 않는다.
//    @Override
//    public Long modify(BoardDTO boardDTO) {
//        // JPA는 update를 지원하지 않는다. 기존 객체를 지우고 다시 추가할 수도 있으나, 기존 객체를 가져와서
//        // setter로 수정 후 다시 저장할 수도 있다.
//        Optional<Board> optionalBoard = boardRepository.findById(boardDTO.getBno());
//        if(optionalBoard.isPresent()) {
//            Board existingBoard = optionalBoard.get();
//            existingBoard.setTitle(boardDTO.getTitle());
//            existingBoard.setContent(boardDTO.getContent());
//            // save 메서드를 사용하면 해당 객체의 기본키와 같은 기본키인 컬럼이 있는지 자동적으로 탐색한다.
//            // 같은 기본키를 가진 컬럼이 있으면 덮어쓰고, 없으면 새로운 컬럼을 생성한다.
//            return boardRepository.save(existingBoard).getBno();
//        } else {
//            return null;
//        }
//    }

    @Override
    public Long modify(BoardFileDTO boardFileDTO) {
        long bno = insert(boardFileDTO);
        return bno;
    }

    // 삭제: deleteById(ID id)으로 수행한다.
    @Override
    public int delete(Long delBno) {
        // 삭제는 반환 값을 주지 않는데
        // 나는 반환 값을 받고 싶어서 try-catch를 쓰고 성공하면 1, 실패하면 0을 주게 했음.
        try {
            boardRepository.deleteById(delBno);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
