package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.dto.CommentDTO;
import com.ezen.boot_JPA.entity.Board;
import com.ezen.boot_JPA.entity.Comment;

import java.util.List;

public interface CommentService {

    default Comment convertDtoToEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .bno(commentDTO.getBno())
                .writer(commentDTO.getWriter())
                .content(commentDTO.getContent())
                .build();
    }

    default CommentDTO convertEntityToDto(Comment comment) {
        return CommentDTO.builder()
                .bno(comment.getBno())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .registerTime(comment.getRegisterTime())
                .modifyTime(comment.getModifyTime())
                .build();
    }

    Long storeComment(CommentDTO commentDTO);

    List<CommentDTO> getCommentList(long bno);
}
