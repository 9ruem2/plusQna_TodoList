package com.QnaApi.board.service;

import com.QnaApi.board.entity.Board;
import com.QnaApi.board.repository.BoardRepository;
import com.QnaApi.exception.BusinessLogicException;
import com.QnaApi.exception.ExceptionCode;
import com.QnaApi.member.entity.Member;
import com.QnaApi.member.repository.MemberRepository;
import com.QnaApi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

// 빈 검색으로 잡히게 하겠다, autowired대상이 되게 하겠다
@Component
@RequiredArgsConstructor
public class BoardManager {
    // private final BoardService boardService; 양방향에서 주입할수가 x 그래서 매니저에서는 서비스를 생성자주입을 하면 안됨
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    /**
     * TODO
     *
     * - 메서드의 이름은 동작을 나타내는 것이 일반적입니다. 현재는 명사가 사용되고 있으며, 명사는 일반적으로 클래스에 사용됩니다.
     * @param board
     * @return
     */
    // 최종적으로 게시글을 업데이트해서 service한테 리턴시켜주는 updater 구현로직
    public Board boardUpdater(Board board) {
        // TODO 검증이 끝난 엔티티 객체가 사용이 되지 않는다면 find보다는 check나 verify 등으로 단순히 검증만 하도록 하면 좋을 것 같습니다.
        // board를 작성한 멤버가 실제로 있는지 없는지 확인하기
        Member findMember = memberService.findVerifiedMember(board.getMember().getMemberId());

        // (board)게시글이 존재하는지 확인하는 로직
        Board findBoard = findVerifiedBoard(board.getBoardId()); // FindBoard은 원본게시글이 저장됨

        //게시글을 등록한 멤버Id와 업데이트를 요청 멤버의 id가 같은지 확인하기
        checkNotExistBoard(board);

        /**
         * TODO
         * - 파라미터로 전달 받은 `board` 객체에는 수정하고자 하는 정보들이 포함되어 있을텐데, 수정하고자 하는 정보에 포함된
         * board.getQuestionStatus()이 QUESTION_ANSWERED라고 비교하는게 맞지 않습니다.
         *      - board.getQuestionStatus()의 비교 대상은 DB에서 조회한 질문에 포함된  QuestionStatus가 되어야 논리적으로 정상적인
         *      검증이 됩니다.
         *          - 왜냐하면, DB에 저장되어 있는 질문이 이미 `답변 완료 상태`이면 해당 질문을 수정할 수 없어야할테니까요.
         * - `QUESTION_ANSWERED`는 클라이언트 쪽에서 request로 전달될 필요가 없는 QuestionStatus입니다.
         *      - `QUESTION_ANSWERED`는 답변이 등록될 경우, 서비스 클래스 내부에서 사용되는 값이기 때문입니다.
         */
        // 게시글의 상태가 답변완료인 경우 게시글을 수정할수없도록 체크함
        checkQuestionAnswered(board);

        /**
         * TODO
         * - changeContent() 내부에서 findVerifiedBoard()를 호출하는데 그럴 필요가 없습니다.
         *      - 40번 라인에서 findVerifiedBoard()를 이미 호출해서 findBoard 객체를 얻었기 때문에 이 객체를 가지고,
         *      수정할 내용들을 채워 넣고, DB에 저장하면 됩니다.
         *          - 이렇게 해야 불 필요한 메서드가 두 번 호출되지 않습니다.
         */
        // repository.board의 title, content, status(공개/ 비공개)로 수정하기
        return changeContent(board);
    }

    //boardId로 게시글이 존재하는지 확인하기
    public Board findVerifiedBoard(Long boardId){
        Optional<Board> optionalBoard = boardRepository.findById(boardId); // boardRepository에서 뭘 찾을건데 BYid로 찾을거야 지금 전달받은 board의 Id로 보드를 찾을거야
        Board findBoard =
            optionalBoard.orElseThrow(()-> // Optional클래스의 null값이 NPE에러를 발생시키지 않도록 하는 예외처리로직작성
                new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND)); // boardId로 게시글을 찾을 수 없다면 예외처리를 함
        return findBoard;
    }

    // 게시글의 답변상태가 '답변완료'인지 확인하는 로직
    // (1) QUESTION_ANSWERED(답변완료) 질문상태는 변경하지 못하도록 막는 로직이 필요함
    public void checkQuestionAnswered(Board board){
        if (board.getQuestionStatus().equals(Board.QuestionStatus.QUESTION_ANSWERED)) {
           throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ANSWERED_QUESTION);
        }
    }

    /**
     * TODO
     * - 메서드 이름만 보면 board(질문(?))가 존재하지 않는지를 체크한다라고 생각되는데 질문을 등록한 memberId와 수정을 하려는 memberId가
     * 일치하는지를 체크한다라고 설명이 적혀있네요.
     */
    //(2) 등록된 질문의 제목과 내용은 질문을 등록한 회원(고객)만 수정,삭제,조회할 수 있어야 한다.
    public void checkNotExistBoard(Board board){// 업데이트할 내용이 저장되어있는 board라고 가정
        Long customerMemberId = board.getMember().getMemberId(); //업데이트를 요청한 멤버의 아이디를 변수에 저장
        // TODO 업데이트 하고자 하는 Board를 조회하면 회원 정보를 가져올 수 있기 때문에 memberService로 Member를 조회할 필요가 없을 것 같습니다.
        Member findMember = memberService.findVerifiedMember(customerMemberId); // 멤버아이디를 통해 memberRepository에 저장된 멤버객체를 가져옴
        List<Board> boardList = findMember.getBoards(); // 업데이트를 요청한 멤버(findMember)가 작성했던 bordList들을 전부 가져옴

        /**
         * TODO
         * - 왜 회원이 여태까지 등록한 모든 질문을 가져와서 boardId를 검증하는지 모르겠네요.
         *  업데이트 하려는 boardId로 DB에서 조회해서 있는지 없는지만 확인하면 될것 같은데요.
         */
        // 업데이트,삭제,조회를 요청한 회원이 작성한 게시글들 중에 수정,삭제,조회 하고자하는 게시글의 id가 있는지 for문을 통해 확인
        boolean existBoard = false;
        for(Board b:boardList){
            if(board.getBoardId() == b.getBoardId()){ //업데이트를 요청한 멤버의 게시글목록중 업데이트요청 게시글의 boardId가 있는지 확인
                existBoard = true; // 있다면 true
                break; // 찾았으니 종료!
            }
        }

        /**
         * TODO
         * - `CANNOT_CHANGE_BOARD`는 질문을 수정할 수 없다는 의미일텐데 의도한건 질문이 존재하지 않는다라는걸 표현하기 위함이라면 어색합니다.
         */
        if(!existBoard){ // existBoard==false라면 (업데이트를 요청한 멤버가 쓴 게시글이 아니었다면
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_BOARD); // 게시글을 찾을 수 없다고 예외를 던져라
        }
    }



    // (1),(2)가 통과되었다는 가정하의 동작이 실행해야함
    public Board changeContent(Board board){
        Board findBoard = findVerifiedBoard(board.getBoardId());

        // TODO 중복 로직이 발생하므로 CustomBeanUtils를 이용하세요. 섹션 3 Spring Data JPA Solution 코드에 포함되어 있음.
        Optional.ofNullable(board.getTitle())
                .ifPresent(findBoard::setTitle);
        Optional.ofNullable(board.getContent())
                .ifPresent(findBoard::setContent);
        Optional.ofNullable(board.getContentStatus())
                .ifPresent(findBoard::setContentStatus);

        return findBoard;
    }

    /**
     * TODO
     * - 메서드 명 오타
     * - 메서드 명 어색
     * - 쿼리 추가 발생
     * @param board
     * @return
     */
    public Board delteContent(Board board){
        Member findMember = memberService.findVerifiedMember(board.getMember().getMemberId());
        Board findBoard = findVerifiedBoard(board.getBoardId()); // FindBoard은 원본게시글이 저장됨
        checkNotExistBoard(board);
//        - 이미 삭제 상태인 질문은 삭제할 수 없다.
        if(findBoard.getQuestionStatus()==Board.QuestionStatus.QUESTION_DELETE){
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_BOARD);
        }

//        - 질문 삭제 시, 테이블에서 row 자체가 삭제되는 것이 아니라 질문 상태 값이 (QUESTION_DELETE)으로 변경되어야 한다.
        findBoard.setQuestionStatus(Board.QuestionStatus.QUESTION_DELETE);
        return findBoard;
    }
}
