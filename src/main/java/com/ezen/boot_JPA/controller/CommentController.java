package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.CommentDTO;
import com.ezen.boot_JPA.service.CommentService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@lombok.extern.slf4j.Slf4j
@RestController
@RequestMapping("/comment/*")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("insert")
    public ResponseEntity<String> insertComment(@RequestBody CommentDTO commentDTO) {
        log.info(" CommentController insertComment operating. commentDTO: {}", commentDTO);
        Long cno = commentService.storeComment(commentDTO);
        // JS에서 결과값이 "1"인지 아닌지 볼 거라서 그냥 반환 값을 String으로 하고 잘 되면 "1", 아니면 "0"을 줘도 된다.
        return cno != null ?
                new ResponseEntity<String>("1", HttpStatus.OK) :
                new ResponseEntity<String>("0", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/list/{bno}")
    @ResponseBody
    public List<CommentDTO> listComment(@PathVariable("bno") long bno) {
        List<CommentDTO> commentDTOList = commentService.getCommentList(bno);
        log.info(" > commentDTOList: {}", commentDTOList);
        return commentDTOList;
    }
}
