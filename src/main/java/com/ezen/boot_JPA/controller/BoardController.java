package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.service.BoardService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@lombok.extern.slf4j.Slf4j
@Slf4j
@RequestMapping("/board/*")
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/register")
    public void register() {}

    @PostMapping("/register")
    public String register(BoardDTO boardDTO) {
        log.info(" > BoardController register start. BoardDTO: {}", boardDTO);
        // DB에서 insert, update, delete를 수행하면 반환 값이 int(영향을 받은 줄의 숫자)였다.
        // JPA에서는 insert, update, delete를 하면 반환 값으로 id, 즉 기본키를 준다.
        Long bno = boardService.insert(boardDTO);
        log.info(bno > 0 ? "BoardController register succeeded." : "BoardController register Failed.");
        return "/index";
    }

    @GetMapping("/list")
    public void list(Model model) {
        List<BoardDTO> boardDTOList = boardService.getList();
        model.addAttribute("list", boardDTOList);
    }

    @GetMapping("/detail")
    public void detail(Model model, @RequestParam("bno") Long bno) {
        BoardDTO boardDTO = boardService.getDetail(bno);
        model.addAttribute("boardDTO", boardDTO);
    }

    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        Long bno = boardService.modify(boardDTO);
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/detail";
    }

    @GetMapping("/delete")
    public String delete(Long delBno) {
        int isOk= boardService.delete(delBno);
        log.info(isOk == 1 ? "BoardController delete succeeded." : "BoardController delete failed.");
        return "redirect:/board/list";
    }
}
