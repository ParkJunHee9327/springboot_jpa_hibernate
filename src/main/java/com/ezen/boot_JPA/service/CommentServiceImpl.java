package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.CommentDTO;
import com.ezen.boot_JPA.entity.Comment;
import com.ezen.boot_JPA.repository.CommentRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Long storeComment(CommentDTO commentDTO) {
        return commentRepository.save(convertDtoToEntity(commentDTO)).getCno();
    }

    @Override
    public List<CommentDTO> getCommentList(long bno) {
        // 값을 가져올 때 기본 키를 기준으로 where을 쓰면 편한데, 이 경우에는 기본키가 아닌 bno를 기준으로 해야 한다.
        // CommentRepository에 해당 기능에 대한 메서드를 만든 후 사용한다.
        List<Comment> commentList = commentRepository.findByBno(bno);
        List<CommentDTO> commentDTOList = commentList.stream().map(c -> convertEntityToDto(c)).toList();
        return commentDTOList;
    }
}
