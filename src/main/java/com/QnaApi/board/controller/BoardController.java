package com.QnaApi.board.controller;

import com.QnaApi.board.dto.*;
import com.QnaApi.board.entity.Board;
import com.QnaApi.board.mapper.BoardMapper;
import com.QnaApi.board.service.BoardService;
import com.QnaApi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO
 * - `Board`라는 네이밍이 적절하지 않은 것 같습니다. REST API 애플리케이션에서는 Controller가 일종의 리소스를 제공하는
 * API 엔드포인트 역할을 하는데 어떤 리소스를 제공하는지 구체적이지 않기 때문에 네이밍을 변경하는게 좋을 것 같습니다.
 */
@RestController // @Controller와 @ResponsBody의 동작을 하나로 결합한 편의 컨트롤러, 모든 핸들러 메소드에서 @ResponseBody를 사용할 필요가 없다는 것입니다.
@RequestMapping("/v1/boards")
@Validated //유효성검사 시 필요
@Slf4j
@RequiredArgsConstructor //final이 붙어있는 애들만 빈으로 인식 필요한 애들만 생성자를 만들어줌 따라서 컨트롤러에서는 @RAC~~
public class BoardController {
    private final BoardMapper mapper; //의존성주입때문에 스프링 컨테이너가 매퍼랑 보드서비스를 빈의 형태로 가지고 있는건데 어떤객체에 연결을 해줘야할지 몰라서 에러가 나고있는 상황
    private final BoardService boardService;
    private final MemberService memberService;

    /**
     * TODO
     * - 질문 등록이 이 쪽 Controller에서 처리한다고 해서 애플리케이션이 안돌아가는건 아닙니다. 다만, 질문이라는 리소스 자체가 단순히
     * 다른 리소스와 관련이 없는 독립적인 리소스가 아니라 특정 회원이 질문을 등록하는 것이기 때문에 MemberController에서 요청을 처리하는 것이
     * REST API 리소스 관점에서 더 자연스럽습니다.
     *      - 예를 들어, CoffeeController의 경우에는 Coffee 리소스 자체를 등록할 경우에는 회원(Member) 같은 특정 리소스와 연관이 없기 때문에
     *      CoffeeController에서 등록을 해도 자연스럽습니다.
     * @param boardPostDto
     * @return
     */
// 게시글 추가
    @PostMapping
    public ResponseEntity postBoard(@Valid @RequestBody BoardPostDto boardPostDto){
        Board board = boardService.createBoard(mapper.boardPostDtoToBoard(boardPostDto));
        return new ResponseEntity<>(
                mapper.BoardToBoardResponseDto(board), HttpStatus.CREATED);
    }

    @PatchMapping("/{board-id}")
    public ResponseEntity patchBoard(@PathVariable("board-id") @Positive Long boardId, // TODO path variable에서 추출되는(extract)를 사용하세요.
                                     @Valid @RequestBody BoardPatchDto boardPatchDto){
    //Dto를 mapper로 바꿔서 service로직에서 UpdateBoard()를 실행
        Board board = mapper.boardPatchDtoToBoard(boardPatchDto);
        Board patchBoard = boardService.updateBoard(board);
        return new ResponseEntity<>(mapper.BoardToBoardResponseDto(patchBoard), HttpStatus.OK);
    }

/*
등록한 1건의 질문을 조회하는 기능
- 이미 삭제 상태인 질문은 조회할 수 없다.
- 비밀글 상태인 질문은 질문을 등록한 회원(고객)과 관리자만 조회할 수 있다.
- 1건의 특정 질문은 회원(고객)과 관리자 모두 조회할 수 있다.
- 1건의 질문 조회 시, 해당 질문에 대한 답변이 존재한다면 답변도 함께 조회되어야 한다.
 */

    /**
     * TODO
     * - BoardService에서 해야 될 일들은 BoardService 내부에서 처리하는 것이 좋습니다. 현재는 isDeleted(), getArticle() 같은
     *  메서드가 Controller에서 호출하는데 BoardService 내부에서 동작을 수행하는 것이 좋습니다.
     * @param boardId
     * @param boardGetDto
     * @return
     */
    @GetMapping("/{boardId}")
    public ResponseEntity getBoard(@PathVariable ("boardId") int boardId,
                                       @Valid @RequestBody BoardGetDto boardGetDto){
        Board findBoard = boardService.findVerifiedBoard(mapper.boardGetDtoToBoard(boardGetDto).getBoardId());
        //Board board = boardService.findVerifiedBoard();

        // 이미 삭제 상태인 질문은 조회할 수 없다.
        // 따라서 보드의 상태가 delete면 가져올 수 없다고 예외를 던진다.
        boardService.isDeleted(findBoard);

        // 비밀글 이라면 질문을 등록한 회원과 관리자만 조회할 수 있다.
        // 게시글이 공개인지, 비공개인지 확인->공개글이라면 회원과 관리자 모두 조회할 수 있다.
        /**
         * TODO
         * - getXXXX 은 일반적으로 데이터를 조회할 때 사용하는데 여기서는 상태를 검증하는 용도로만 사용하고 있어서 적절한 네이밍이 아닌 것 같습니다.
         */
        boardService.getArticle(findBoard);

        //1건의 질문 조회 시 질문에 대한 답변이 존재한다면 답변도 함께 조회되어야 한다.
        //1:1 = 하나의 게시글에는 하나의 답변만 받을 수 있다 (Frequently Asked Questions, FAQ, 자주묻는 질문과 답변)
        //1:n = 일반적인 게시물: 하나의 질문글에는 여러개의 답변을 받을 수 있다
        BoardResponseDto boardResponseDto = mapper.BoardToBoardResponseDto(findBoard);
        return new ResponseEntity<>(boardResponseDto,HttpStatus.OK);
    }

/*
등록한 여러 건의 질문을 조회하는 기능
- 여러 건의 질문 목록은 회원(고객)과 관리자 모두 조회할 수 있다.
- 삭제 상태가 아닌 질문만 조회할 수 있다. //Fixme
- 여러 건의 질문 목록에서 각각의 질문에 답변이 존재한다면 답변도 함께 조회 할수있어야한다.
- 여러 건의 질문 목록은 페이지네이션 처리가 되어 일정 건수 만큼의 데이터만 조회할 수 있어야 한다.
- 여러 건의 질문 목록은 아래의 조건으로 정렬해서 조회할 수 있어야 한다.
    ᄂ 최신글 순으로
    ᄂ 오래된글순으로
    ᄂ 좋아요가 많은 순으로(좋아요 구현 이후 적용)
    ᄂ 좋아요가 적은 순으로(좋아요 구현 이후 적용)
    ᄂ 조회수가 많은 순으로(조회수 구현 이후 적용)
    ᄂ 조회수가 적은 순으로(조회수 구현 이후 적용)
 */

/*
public ResponseEntity getOrders(@Positive @RequestParam int page,
                                @Positive @RequestParam int size) {
질문하고싶은 내용? 같은 http메서드도 Get이고 Uri도 같으니까 boads를 구분해 주기 위해서 @RequesParam을 사용한 것인데
@RequestParam대신 dto로는 받을 수 없는지 다른 방법이 없는지?

ᄂ 최신글 순으로
ᄂ 오래된글순으로 어떻게 요청을받아야하는지 ?
 */
    @GetMapping
    public ResponseEntity getBoards(@Positive @RequestParam int page,
                                    @Positive @RequestParam int size){
        Page<Board> pageBoards = boardService.findBoards(page -1, size);
        List<Board> boards = pageBoards.getContent();

        return new ResponseEntity<>(new MultiResponseDto(
                mapper.boardsToBoardResponseDtos(boards), pageBoards),HttpStatus.OK);
    }


/*
질문삭제 구현
- 1건의 질문은 회원(고객)만 삭제할 수 있다.
- 1건의 질문 삭제는 질문을 등록한 회원만 가능하다.
- 질문 삭제 시, 테이블에서 row 자체가 삭제되는 것이 아니라 질문 상태 값이 (QUESTION_DELETE)으로 변경되어야 한다.
- 이미 삭제 상태인 질문은 삭제할 수 없다.
*/
    @DeleteMapping
    public ResponseEntity cancelOrder(@RequestBody BoardDeleteDto boardDeleteDto){
        Board board = mapper.boardDeleteDtoToBoard(boardDeleteDto);
        boardService.cancelOrder(board);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
