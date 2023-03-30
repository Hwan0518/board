package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    /**
     * 글 작성 처리 : entity에 저장
     */
    public void write(Board board, MultipartFile file) throws Exception{

        //경로 지정
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        //식별자 생성
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        //파일 경로와 이름을 넣어서 파일을 생성
        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        //파일 이름과 경로를 설정해줌
        board.setFilename(fileName);
        board.setFilepath("/files/"+fileName);
        boardRepository.save(board);
    }


    /**
     * 게시글 리스트 처리 : 레포지토리에서 작성한 모든 글의 정보를 불러옴
     */
    public Page<Board> boardList(Pageable pageable){

        return boardRepository.findAll(pageable);
    }

    /**
     * 특정 게시글 불러오기 : 입력한 id값에 일치하는 게시글을 불러온다
     */
    public Board boardView(Integer id){

        return boardRepository.findById(id).get();
    }

    /**
     * 특정 게시글 삭제
     */
    public void boardDelete(Integer id){

        boardRepository.deleteById(id);
    }

}
