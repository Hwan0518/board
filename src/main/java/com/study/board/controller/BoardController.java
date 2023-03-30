package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write") //localhost:8080/board/write
    public String boardWriteFrom() {
        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception {
        // 글을 작성
        boardService.write(board, file);

        // 글작성 완료 메시지 출력
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    /**
     * 게시글 리스트
     */
    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        //Page 객체
        Page<Board> list = boardService.boardList(pageable);

        //현재,시작,끝 페이지
        int nowPage = list.getPageable().getPageNumber() +1; //페이지는 0부터 시작되기 때문에 +1처리
        int startPage = Math.max(nowPage -4,1);
        int endPage = Math.min(nowPage +5, list.getTotalPages());

        // 모델에 데이터 추가
        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "boardlist";
    }


    /**
     * 상세페이지
     */
    @GetMapping("/board/view")
    public String boardView(Model model, Integer id) {

        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    /**
     * @Getmapping 의 {id}부분이 변수로 인식이 되어서
     * @PathVariable 에서 id로 들어오게 된다
     */
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id,
                              Model model) {
        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }

    /**
     * 수정하는법
     * 1. 기존의 내용을 검색해서 가져오고
     * 2. 새로운 내용을 기존의 내용에 덮어쓴다
     */
    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id")Integer id, Board board, Model model, MultipartFile file) throws Exception {

        //기존의 내용 조회해서 가져옴 : boardTemp == id에 해당하는 기존의 내용
        Board boardTemp = boardService.boardView(id);

        //이후 기존의 내용에 새로운 내용을 덮어씌움
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());
        boardService.write(boardTemp, file);

        // 글 수정 완료 메시지 출력
        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("modifyUrl", String.format("/board/view?id=%s",id));

        return "message";
    }



}

