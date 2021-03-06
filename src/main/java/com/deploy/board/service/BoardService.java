package com.deploy.board.service;

import com.deploy.board.domain.entity.BoardEntity;
import com.deploy.board.dto.BoardDto;
import com.deploy.board.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BoardService {
    private BoardRepository boardRepository;

    @Transactional
    public List<BoardDto> searchPosts(String keyword) {
        List<BoardEntity> boardEntities = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if (boardEntities.isEmpty()) return boardDtoList;

        for (BoardEntity boardEntity : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }

        return boardDtoList;
    }

    private BoardDto convertEntityToDto(BoardEntity boardEntity) {
        return BoardDto.builder()
                .id(boardEntity.getId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .writer(boardEntity.getWriter())
                .createdDate(boardEntity.getCreatedDate())
                .build();
    }

    @Transactional
    public List<BoardDto> getBoardList() {
        List<BoardEntity> boardEntities=boardRepository.findAll();
        List<BoardDto> boardDtoList=new ArrayList<>();

        for(BoardEntity boardEntity:boardEntities) {
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }

        return boardDtoList;
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public BoardDto getPost(Long id) {
        Optional<BoardEntity> boardEntityWrapper = boardRepository.findById(id);
        BoardEntity boardEntity = boardEntityWrapper.get();

        BoardDto boardDTO = this.convertEntityToDto(boardEntity);

        return boardDTO;
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }
}
