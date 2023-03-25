package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    /**
     * 글 작성 처리 : entity에 저장
     */
    public void write(Board board){
        boardRepository.save(board);
    }


    /**
     * 게시글 리스트 처리 : 레포지토리에서 작성한 모든 글의 정보를 불러옴
     */
    public List<Board> boardList(){

        return boardRepository.findAll();
    }

    /**
     * 특정 게시글 불러오기 : 입력한 id값에 일치하는 게시글을 불러온다
     */
    public Board boardView(Integer id){

        return boardRepository.findById(id).get();
    }


}
