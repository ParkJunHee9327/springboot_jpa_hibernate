package com.ezen.boot_JPA.repository;

import com.ezen.boot_JPA.entity.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ezen.boot_JPA.entity.QBoard.board;

public class BoardCustomRepositoryImpl implements BoardCustomRepository {

    // JPAQueryFactory는 생성자가 필수적으로 필요하다.
    private final JPAQueryFactory queryFactory;

    // em은 EntityManager를 지칭하는 관례적인 변수 이름이다.
    public BoardCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // BoardCustomRepository는 인식시키기 위한 수단이고, 실제 메서드는 여기서 구현한다.
    @Override
    public Page<Board> searchBoard(String sortType, String keyword, Pageable pageable) {
        // 조건이 많은 경우 BooleanExpression 객체를 이용하여 AND를 다룰 수 있다.
        // 예시: select * from board where isDel = 'n' and title like '%key%';
        // Boolean Expression condition = board.isDel.eq('N');
        BooleanExpression condition = null;

        // 동적 검색 조건 추가
        // 받은 sortType을 문자열 배열에 넣어서 뽑아쓰는 방법
        if(sortType != null && keyword != null) {
            String[] typeArr = sortType.split("");

            for(String str : typeArr) {
                switch (str) {
                    case "t":
                        condition = (condition == null) ?
                                board.title.containsIgnoreCase(keyword) : condition.or(board.title.containsIgnoreCase(keyword));
                        break;
                    case "w":
                        condition = (condition == null) ?
                                board.writer.containsIgnoreCase(keyword) : condition.or(board.writer.containsIgnoreCase(keyword));
                        break;
                    case "c":
                        condition = (condition == null) ?
                                board.content.containsIgnoreCase(keyword) : condition.or(board.content.containsIgnoreCase(keyword));
                        break;
                    case "tw":
                        condition = (board.title.containsIgnoreCase(keyword)).or(board.content.containsIgnoreCase(keyword));
                        break;
                }
            }


            // 모든 조건을 switch로 명시하는 방법
//            switch (sortType) {
//                case "t":
//                    condition = board.title.containsIgnoreCase(keyword);
//                    break;
//                case "w":
//                    condition = board.writer.containsIgnoreCase(keyword);
//                    break;
//                case "c":
//                    condition = board.content.containsIgnoreCase(keyword);
//                    break;
//                case "tw":
//                    condition = (board.title.containsIgnoreCase(keyword)).or(board.content.containsIgnoreCase(keyword));
//                    break;
//            }
        }

        // 쿼리 작성 및 페이징 적용
        List<Board> result = queryFactory
                .selectFrom(board)
                .where(condition)
                .orderBy(board.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 검색된 데이터의 전체 개수가 필요하다.
        long searchTotal = queryFactory
                .selectFrom(board)
                .where(condition)
                .fetch().size(); // fetchCount()가 폐기되어서 .fetch().size()로 대체하였다.

        return new PageImpl<>(result, pageable, searchTotal);
    }
}

