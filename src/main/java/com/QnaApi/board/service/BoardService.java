package com.QnaApi.board.service;

import com.QnaApi.board.entity.Board;
import com.QnaApi.board.repository.BoardRepository;
import com.QnaApi.exception.BusinessLogicException;
import com.QnaApi.exception.ExceptionCode;
import com.QnaApi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


/**
 * TODO
 * - 트랜잭션 처리가 안되어 있습니다.
 * - BoardManager 클래스가 처리하는 일들을 나중에 Board라는 도메인 엔티티가 처리하도록 수정해보세요.
 */
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final BoardManager boardManager;

    // 게시글추가
    // 회원가입이 된 멤버만 글을 쓸 수 있다는 조건
    public Board createBoard(Board board) {
        // 회원가입이 된 멤버인지 확인하기
        memberService.findVerifiedMember(board.getMember().getMemberId());
        return saveBoard(board);
    }

    // 게시글수정하기 로직
    // 세부동작들은 클래스를 하나 만들어주고 하는게 좋음 (이유는? 코드의 가독성과 재사용을 위해서)
    public Board updateBoard(Board board){ // 보드에서 업데이트하라는 요청이 왔을 때 받아주는 역할을 하는 로직으로 쓰임
        Board updatedBoard = boardManager.boardUpdater(board);
        // 게시글 저장하기
        return saveBoard(updatedBoard);
    }

    // 수정된 게시글을 다시 repository에 .save()저장하기
    private Board saveBoard(Board board){
        return boardRepository.save(board);
    }

    public Board findVerifiedBoard(Long boardId){
        Board board = boardManager.findVerifiedBoard(boardId);
        return board;
    }


// 질문 삭제 시, 테이블에서 row 자체가 삭제되는 것이 아니라 질문 상태 값이 (QUESTION_DELETE)으로 변경되어야 한다.
/*
- 1건의 질문은 회원(고객)만 삭제할 수 있다.
- 1건의 질문 삭제는 질문을 등록한 회원만 가능하다.
- 질문 삭제 시, 테이블에서 row 자체가 삭제되는 것이 아니라 질문 상태 값이 (QUESTION_DELETE)으로 변경되어야 한다.
- 이미 삭제 상태인 질문은 삭제할 수 없다.
*/

    /**
     * TODO
     * - 메서드 명이 적절하지 않으니 수정하세요.
     * @param board
     */
    public void cancelOrder(Board board) {
        Board findBoard = findVerifiedBoard(board.getBoardId());
        Board deletedBoard = boardManager.delteContent(findBoard);
       // 변경된 Status를 REPOSITORY에 저장
        boardRepository.save(deletedBoard);
    }

    /**
     * TODO
     * - `is`는 일반적으로 boolean 값을 리턴할 때 많이 사용됩니다. check 또는 verify 정도로 바꾸는게 좋을 것 같습니다.
     * @param board
     */
    public void isDeleted(Board board) { //fixme QuestionStatus.QUESTION_DELETE 일때 에러가 클라이언트측과 콘솔창에 다르게뜨는 문
        //- 보드의 상태가 delete인지 확인 하고 delete라면 삭제상태라면 보드를 읽어올 수 없다고 예외를 던짐
        if (board.getQuestionStatus() == Board.QuestionStatus.QUESTION_DELETE) {
            throw new BusinessLogicException(ExceptionCode.BOARD_HAS_BEEN_DELETED);
        }
    }

//    게시글이 공개인지, 비공개인지 확인하기
    public boolean getPublicationStatus (Board board){
        boolean boardSecret = false;
        if (board.getContentStatus().equals(Board.ContentStatus.SECRET)) {
            return boardSecret = true; // 게시글 = 비공개 상태
        }
        return boardSecret;
    }

    /**
     * TODO
     * - 게시글이라는 의미로 Article의 의미를 잘 모르겠네요. 질문을 의미하는 것 같긴한데..
     * @param board
     */
        //1건의 게시글을 조회하는 메서드
    public void getArticle (Board board){
        // TODO boolean을 반환 받는다면 isSecret이 더 적절할 것 같네요.
        boolean boardSecret = getPublicationStatus(board); // TODO publication이라는 용어가 좀 어색합니다.
        if (boardSecret) {
            // 게시글이 비공개 상태라면
            // 게시글을 조회하려는 사람의 memberId와 저장되어있는 게시글을 작성한 memberId가 같은지 확인하기
            boardManager.checkNotExistBoard(board);
        }
    }

        //  1건의 질문 조회 시 질문에 대한 답변이 존재한다면 답변도 함께 조회되어야 한다.
    public Page<Board> findBoards ( int page, int size){
        return boardRepository.findAll(PageRequest.of(page, size,
                Sort.by("boardId").descending())); //보드id를 기준으로 내림차순으로 보드를 정렬해서 해당하는 페이지에 대한 정보를 넘겨줌
    }
}
