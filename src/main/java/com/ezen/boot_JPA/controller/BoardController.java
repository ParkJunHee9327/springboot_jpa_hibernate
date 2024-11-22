package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.dto.BoardFileDTO;
import com.ezen.boot_JPA.dto.FileDTO;
import com.ezen.boot_JPA.dto.PagingVO;
import com.ezen.boot_JPA.handler.FileHandler;
import com.ezen.boot_JPA.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RequestMapping("/board/*")
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;
    private final FileHandler fileHandler;

    @GetMapping("/register")
    public void register() {}

    // 파일 업로드를 하지 않았을 때의 /register 메서드이다.
//    @PostMapping("/register")
//    public String register(BoardDTO boardDTO) {
//        log.info(" > BoardController register start. BoardDTO: {}", boardDTO);
//        // DB에서 insert, update, delete를 수행하면 반환 값이 int(영향을 받은 줄의 숫자)였다.
//        // JPA에서는 insert, update, delete를 하면 반환 값으로 id, 즉 기본키를 준다.
//        Long bno = boardService.insert(boardDTO);
//        log.info(bno > 0 ? "BoardController register succeeded." : "BoardController register Failed.");
//        return "/index";
//    }

    @PostMapping("/register")
    public String register(BoardDTO boardDTO, @RequestParam(name = "files", required = false) MultipartFile[] files) {
        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0) {
            // 파일 핸들러 작업
            fileList = fileHandler.uploadFiles(files);
        }
        Long bno = boardService.insert(new BoardFileDTO(boardDTO, fileList));
        return "redirect:/board/list";
    }

   // 페이지네이션을 하지 않았을 때의 /list 경로의 GetMapping 메서드.
      /*@GetMapping("/list")
      public void list(Model model) {
          List<BoardDTO> boardDTOList = boardService.getList();
          model.addAttribute("list", boardDTOList);
      }*/

    @GetMapping("/list")
    // "pageNum"의 기본값이 1이 아닌 0인 이유: 사용하는 페이지네이션 라이브러리의 시작 번지가 0이기 때문.
    public void list(Model model, @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum) {
        // Page: JPA에서 제공하는 페이지네이션용 객체다.
        // 화면에서 들어오는 pageNum = 1이면 여기서 0으로 처리해야 한다. 시작 번지가 0이기 때문이다.
        log.info(" pageNum > {}", pageNum);
        pageNum = (pageNum == 0 ? 0 : pageNum - 1);
        log.info(" pageNum > {}", pageNum);

        Page<BoardDTO> boardDTOList = boardService.getList(pageNum);
        log.info(" > BoardController list start. boardDTOList: {}.", boardDTOList);
        log.info(" > BoardController list start. Total count: {}", boardDTOList.getTotalElements()); //전체 글 수
        log.info(" > BoardController list start. Total page: {}", boardDTOList.getTotalPages()); // 전체 페이지 수(realEndPage)
        log.info(" > BoardController list start. The current page: {}", boardDTOList.getNumber()); // 페이지 번호(pageNum)
        log.info(" > BoardController list start. The amount of post per page: {}", boardDTOList.getSize()); // 한 페이지에 표시되는 글의 줄 양(postPerPage)
        log.info(" > BoardController list start. Can move to next: {}", boardDTOList.hasNext()); // 다음 페이지로 갈 수 있는지 여부
        log.info(" > BoardController list start. Can move to previous: {}", boardDTOList.hasPrevious()); // 이전 페이지로 갈 수 있는지 여부
        model.addAttribute("list", boardDTOList);

        // Page 객체는 백엔드 전용(DB에서 값 받아오기용) 객체라서 화면에 띄울 수 있도록 다른 객체가 필요하다.
        PagingVO pagingVO = new PagingVO(boardDTOList, pageNum);
        model.addAttribute("pagingVO", pagingVO);
        model.addAttribute("list", boardDTOList);
    }

    @GetMapping("/detail")
    public void detail(Model model, @RequestParam("bno") Long bno) {
        // 파일 기능이 추가되어 boardDTO를 사용하지 않는다.
        //  BoardDTO boardDTO = boardService.getDetail(bno);
        BoardFileDTO boardFileDTO = boardService.getDetail(bno);
        model.addAttribute("boardFileDTO", boardFileDTO);
    }

    // 파일 기능을 지원하게 되어 기존의 /modify는 사용하지 않게 되었다.
//    @PostMapping("/modify")
//    public String modify(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
//        Long bno = boardService.modify(boardDTO);
//        redirectAttributes.addAttribute("bno", boardDTO.getBno());
//        return "redirect:/board/detail";
//    }


    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO,
                         @RequestParam(name = "files", required = false) MultipartFile[] files, RedirectAttributes redirectAttributes) {
        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0) {
            fileList = fileHandler.uploadFiles(files);
        }
        Long bno = boardService.modify(new BoardFileDTO(boardDTO, fileList));
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/detail";
    }

    @GetMapping("/delete")
    public String delete(Long delBno) {
        int isOk= boardService.delete(delBno);
        log.info(isOk == 1 ? "BoardController delete succeeded." : "BoardController delete failed.");
        return "redirect:/board/list";
    }

    @ResponseBody
    @DeleteMapping("/fileRemove/{uuid}")
    public String fileDelete(@PathVariable("uuid") String uuid) {
        log.info("BoardController fileDelete started. uuid: {}", uuid);
        long bno = boardService.deleteFile(uuid);
        return bno > 0 ? "1" : "0";
    }
}
