package com.ezen.boot_JPA.service;


import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.entity.Board;
import com.ezen.boot_JPA.repository.BoardRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Override
    public Long insert(BoardDTO boardDTO) {
        // DB에 저장할 객체는 Board이다.
        // save() 메서드: insert한 후 저장 객체의 id를 반환한다. Entity 객체만 파라미터로 받아서 DTO를 사용할 수 없다.
        return boardRepository.save(convertDtoToEntity(boardDTO)).getBno(); // DTO를 Board로 변환해서 건네준 후 기본키인 bno를 받아온다.
    }

    @Override
    public List<BoardDTO> getList() {
        // controller로 보내야 하는 반환값은 List<BoardDTO>이다.
        // 그러나 DB에서 가져오는 List는 List<Board>다.
        // 따라서 변환 작업이 필요하다.
        // 모두 가져와야 하니 findAll() 메서드를 사용한다. 정렬은 Sort.by(Sort.Direction.DESC, "정렬 기준 컬럼 이름")으로 하면 된다.
        List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "bno"));

        // 변환 방법 1
//        List<BoardDTO> boardDTOList = new ArrayList<>();
//        for(Board board : boardList) {
//            boardDTOList.add(convertEntityToDto(board));
//        }

        // 변환 방법 2
        List<BoardDTO> boardDTOList = boardList.stream().map(b -> convertEntityToDto(b)).toList();

        return boardDTOList;
    }

    @Override
    public BoardDTO getDetail(Long bno) {
        // findById: 기본키를 주고 해당 객체를 반환한다.
        // findById는 Optional<Object>로 반환한다.
        // Optional<T>: nullPointException이 발생하지 않도록 도와준다.
        // Optional.isEmpty(): null일 경우 확인 가능하다. true, false로 반환한다.
        // Optional.isPresent(): 값이 있는지 확인한다. true, false로 반환한다.
        // Optional.get(): 객체를 가져온다.
        Optional<Board> optionalBoard = boardRepository.findById(bno);
        if(optionalBoard.isPresent()) {
            BoardDTO boardDTO = convertEntityToDto(optionalBoard.get());
            return boardDTO;
        } else {
            return null;
        }
    }

    @Override
    public Long modify(BoardDTO boardDTO) {
        // JPA는 update를 지원하지 않는다. 기존 객체를 지우고 다시 추가할 수도 있으나, 기존 객체를 가져와서
        // setter로 수정 후 다시 저장할 수도 있다.
        Optional<Board> optionalBoard = boardRepository.findById(boardDTO.getBno());
        if(optionalBoard.isPresent()) {
            Board existingBoard = optionalBoard.get();
            existingBoard.setTitle(boardDTO.getTitle());
            existingBoard.setContent(boardDTO.getContent());
            // save 메서드를 사용하면 해당 객체의 기본키와 같은 기본키인 컬럼이 있는지 자동적으로 탐색한다.
            // 같은 기본키를 가진 컬럼이 있으면 덮어쓰고, 없으면 새로운 컬럼을 생성한다.
            return boardRepository.save(existingBoard).getBno();
        } else {
            return null;
        }
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
