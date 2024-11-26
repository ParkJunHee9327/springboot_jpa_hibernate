package com.ezen.boot_JPA.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@Setter
@ToString
public class PagingVO {
    private int totalPage;
    private int startPage;
    private int endPage;
    private boolean hasPrev, hasNext;
    private int pageNum;

    // 검색을 위한 필드들이다.
    private String sortType;
    private String keyword;

    public PagingVO(Page<BoardDTO> boardDTOList, int pageNum, String sortType, String keyword) {
        // 검색 기능이 추가되어 내가 작성한 코드.
        this.sortType = sortType;
        this.keyword = keyword;

        this.pageNum = pageNum + 1;
        this.totalPage = boardDTOList.getTotalPages();

        this.endPage = (int) Math.ceil(this.pageNum / 10.0) * 10;
        this.startPage = endPage - 9;
        if(endPage > totalPage) {
            endPage = totalPage;
        }
        this.hasPrev = this.startPage > 10;
        this.hasNext = this.endPage < this.totalPage;
    }
}
