package com.blog.board_back.service.implement;

import java.util.ArrayList;
import java.util.List;

import com.blog.board_back.dto.request.board.PatchBoardRequestDto;
import com.blog.board_back.dto.response.board.*;
import com.blog.board_back.entity.*;
import com.blog.board_back.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.board_back.dto.request.board.PostBoardRequestDto;
import com.blog.board_back.dto.request.board.PostCommentRequestDto;
import com.blog.board_back.dto.response.ResponseDto;
import com.blog.board_back.repository.resultSet.GetBoardResultSet;
import com.blog.board_back.repository.resultSet.GetCommentListResultSet;
import com.blog.board_back.repository.resultSet.GetFavoriteListResultSet;
import com.blog.board_back.service.BoardService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service // 서비스 빈으로 등록
@RequiredArgsConstructor // 생성자 자동 주입
@Transactional(readOnly = true) // 모든 메서드 기본 읽기 전용 트랜잭션
public class BoardServiceImplement implements BoardService {

  private static final Logger log = LoggerFactory.getLogger(BoardServiceImplement.class);

  // 게시글 CRUD 레포지토리
  private final BoardRepository boardRepository;
  // 게시글 첨부 이미지 레포지토리
  private final ImageRepository imageRepository;
  // 유저 조회 레포지토리
  private final UserRepository userRepository;
  // 댓글 레포지토리
  private final CommentRepository commentRepository;
  // 좋아요 레포지토리
  private final FavoriteRepository favoriteRepository;
  // 검색 로그 레포지토리
  private final SearchLogRepository searchLogRepository;
  // 게시글 목록 뷰 레포지토리 (조회용 뷰 엔티티)
  private final BoardListViewRepository boardListViewRepository;

  // 한 페이지에 보여줄 게시글 수
  private static final int PAGE_SIZE = 5;

  // 게시글 단건 조회 서비스
  @Override
  public ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber) {

    GetBoardResultSet resultSet = null;
    List<ImageEntity> imageEntities = new ArrayList<>();

    try {
      // 게시글 조회 (커스텀 쿼리: 작성자 닉네임/이미지 JOIN)
      resultSet = boardRepository.getBoard(boardNumber);
      if(resultSet == null) return GetBoardResponseDto.noExistBoard();

      // 게시글에 첨부된 이미지 목록 조회
      imageEntities = imageRepository.findByBoardNumber(boardNumber);

    } catch (Exception exception) {
        log.error("BoardService error", exception);
        return ResponseDto.databaseError();
    }

    return GetBoardResponseDto.success(resultSet, imageEntities);

  }

  // 특정 게시글의 좋아요 목록 조회 서비스
  @Override
  public ResponseEntity<? super GetFavoriteListResponseDto> getFavoriteList(Integer boardNumber) {

    List<GetFavoriteListResultSet> resultSets = new ArrayList<>();

    try {
      // 게시글 존재 여부 확인
      boolean existedBoard = boardRepository.existsByBoardNumber(boardNumber);
      if (!existedBoard) return GetFavoriteListResponseDto.noExistBoard();

      // 좋아요 목록 조회
      resultSets = favoriteRepository.getFavoriteList(boardNumber);

    } catch (Exception exception) {
        log.error("BoardService error", exception);
        return ResponseDto.databaseError();
    }

    return GetFavoriteListResponseDto.success(resultSets);

  }

  // 특정 게시글의 댓글 목록 조회 서비스
  @Override
  public ResponseEntity<? super GetCommentListResponseDto> getCommentList(Integer boardNumber) {

    List<GetCommentListResultSet> resultSets = new ArrayList<>();

    try {
      // 게시글 존재 여부 확인
      boolean existedBoard = boardRepository.existsByBoardNumber(boardNumber);
      if (!existedBoard) return GetCommentListResponseDto.noExistBoard();

      // 댓글 목록 조회
      resultSets = commentRepository.getCommentList(boardNumber);

    } catch (Exception exception) {
        log.error("BoardService error", exception);
        return ResponseDto.databaseError();
    }

    return GetCommentListResponseDto.success(resultSets);

  }

  // 최신 게시글 목록 조회 서비스 (작성일시 내림차순)
  @Override
  public ResponseEntity<? super GetLatestBoardListResponseDto> getLatestBoardList() {

    List<BoardListViewEntity> boardListViewEntities = new ArrayList<>();

    try {
      // 작성일시 최신순으로 전체 게시글 목록 조회
      boardListViewEntities = boardListViewRepository.findByOrderByWriteDatetimeDesc();

    } catch (Exception exception) {
        log.error("BoardService error", exception);
        return ResponseDto.databaseError();
    }

    return GetLatestBoardListResponseDto.success(boardListViewEntities);

  }

  // 랜덤 게시글 3개 조회 서비스
  @Override
  public ResponseEntity<? super GetTop3BoardListResponseDto> getTop3BoardList() {

    List<BoardListViewEntity> boardListViewEntities = new ArrayList<>();

    try {
      // 전체 게시글 중 랜덤 3개 조회
      boardListViewEntities = boardListViewRepository.findRandom3();

    } catch (Exception exception) {
      log.error("BoardService error", exception);
      return ResponseDto.databaseError();
    }

    return GetTop3BoardListResponseDto.success(boardListViewEntities);

  }

  // 검색어로 게시글 목록 페이징 조회 + 검색 로그 저장 서비스
  @Override
  @Transactional
  public ResponseEntity<? super GetSearchBoardListResponseDto> getSearchBoardList(String searchWord, String preSearchWord, int page) {

    Page<BoardListViewEntity> boardPage = null;

    try {
      // 제목 또는 내용에 검색어가 포함된 게시글 페이징 조회
      Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
      boardPage = boardListViewRepository.findByTitleContainsOrContentContainsOrderByWriteDatetimeDesc(searchWord, searchWord, pageable);

      // 검색어 로그 저장 (연관 검색어 추적용)
      SearchLogEntity searchLogEntity = new SearchLogEntity(searchWord, preSearchWord, false);
      searchLogRepository.save(searchLogEntity);

      // 이전 검색어가 있으면 역방향 관계 로그도 저장
      boolean relation = preSearchWord != null;
      if (relation) {
        searchLogEntity = new SearchLogEntity(preSearchWord, searchWord, relation);
        searchLogRepository.save(searchLogEntity);
      }

    } catch (Exception exception) {
      log.error("BoardService error", exception);
      return ResponseDto.databaseError();
    }

    return GetSearchBoardListResponseDto.success(boardPage);

  }

  // 특정 유저가 작성한 게시글 목록 페이징 조회 서비스
  @Override
  public ResponseEntity<? super GetUserBoardListResponseDto> getUserBoardList(String email, int page) {

    Page<BoardListViewEntity> boardPage = null;

    try {
      // 작성자 이메일로 게시글 페이징 조회 (최신순)
      Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
      boardPage = boardListViewRepository.findByWriterEmailOrderByWriteDatetimeDesc(email, pageable);

    } catch (Exception exception) {
      log.error("BoardService error", exception);
      return ResponseDto.databaseError();
    }

    return GetUserBoardListResponseDto.success(boardPage);

  }

  // 게시글 작성 서비스
  @Override
  @Transactional
  public ResponseEntity<? super PostBoardResponseDto> postBoard(PostBoardRequestDto dto, String email) {

    try {
      // 작성자 존재 여부 확인
      boolean existedEmail = userRepository.existsByEmail(email);
      if (!existedEmail) return PostBoardResponseDto.noExistUser();

      // 게시글 엔티티 생성 및 저장
      BoardEntity boardEntity = new BoardEntity(dto, email);
      boardRepository.save(boardEntity);

      // 저장된 게시글 번호로 이미지 엔티티 생성 및 일괄 저장
      int boardNumber = boardEntity.getBoardNumber();
      List<String> boardImageList = dto.getBoardImageList();
      List<ImageEntity> imageEntities = new ArrayList<>();
      for (String image: boardImageList) {
        ImageEntity imageEntity = new ImageEntity(boardNumber, image);
        imageEntities.add(imageEntity);
      }
      imageRepository.saveAll(imageEntities);

    } catch (Exception exception) {
        log.error("BoardService error", exception);
        return ResponseDto.databaseError();
    }

    return PostBoardResponseDto.success();

  }

  // 댓글 작성 서비스
  @Override
  @Transactional
  public ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer boardNumber, String email) {

    try {
      // 게시글 존재 여부 확인
      BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
      if (boardEntity == null) return PostCommentResponseDto.noExistBoard();

      // 작성자 존재 여부 확인
      boolean existedUser = userRepository.existsByEmail(email);
      if(!existedUser) return PostCommentResponseDto.noExistUser();

      // 댓글 엔티티 저장 및 게시글 댓글 수 증가
      CommentEntity commentEntity = new CommentEntity(dto, boardNumber, email);
      commentRepository.save(commentEntity);
      boardEntity.increaseCommentCount();
      boardRepository.save(boardEntity);

    } catch (Exception exception) {
        log.error("BoardService error", exception);
        return ResponseDto.databaseError();
    }

    return PostCommentResponseDto.success();

  }

  // 좋아요 토글 서비스 (없으면 추가, 있으면 취소)
  @Override
  @Transactional
  public ResponseEntity<? super PutFavoriteResponseDto> putFavorite(Integer boardNumber, String email) {

    try {
      // 유저/게시글 존재 여부 확인
      boolean existedUser = userRepository.existsByEmail(email);
      if (!existedUser) return PutFavoriteResponseDto.noExistUser();

      BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
      if (boardEntity == null) return PutFavoriteResponseDto.noExistBoard();

      // 이미 좋아요한 경우 취소, 아닌 경우 좋아요 추가
      FavoriteEntity favoriteEntity = favoriteRepository.findByBoardNumberAndUserEmail(boardNumber, email);
      if (favoriteEntity == null) {
        favoriteEntity = new FavoriteEntity(email, boardNumber);
        favoriteRepository.save(favoriteEntity);
        boardEntity.increaseFavoriteCount();
      } else {
        favoriteRepository.delete(favoriteEntity);
        boardEntity.decreaseFavoriteCount();
      }

      boardRepository.save(boardEntity);

    } catch (Exception exception) {
      log.error("BoardService error", exception);
      return ResponseDto.databaseError();
    }

    return PutFavoriteResponseDto.success();

  }

  // 게시글 수정 서비스 (작성자 본인만 가능)
  @Override
  @Transactional
  public ResponseEntity<? super PatchBoardResponseDto> patchBoard(PatchBoardRequestDto dto, Integer boardNumber, String email) {
    try {
      // 게시글/유저 존재 여부 및 작성자 권한 확인
      BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
      if (boardEntity == null) return PatchBoardResponseDto.noExistBoard();

      boolean existedUser = userRepository.existsByEmail(email);
      if (!existedUser) return PatchBoardResponseDto.noExistUser();

      String writerEmail = boardEntity.getWriterEmail();
      boolean isWriter = writerEmail.equals(email);
      if (!isWriter) return PatchBoardResponseDto.noPermission();

      // 게시글 내용 수정 및 저장
      boardEntity.patchBoard(dto);
      boardRepository.save(boardEntity);

      // 기존 이미지 전부 삭제 후 새 이미지 저장
      imageRepository.deleteByBoardNumber(boardNumber);
      List<String> boardImageList = dto.getBoardImageList();
      List<ImageEntity> imageEntities = new ArrayList<>();
      for (String image: boardImageList) {
        ImageEntity imageEntity = new ImageEntity(boardNumber, image);
        imageEntities.add(imageEntity);
      }
      imageRepository.saveAll(imageEntities);

    } catch (Exception exception) {
        log.error("BoardService error", exception);
        return ResponseDto.databaseError();
    }

    return PatchBoardResponseDto.success();

  }

  // 게시글 조회수 증가 서비스
  @Override
  @Transactional
  public ResponseEntity<? super IncreaseViewCountResponseDto> increaseViewCount(Integer boardNumber) {

    try {
      // 게시글 조회 후 조회수 증가 및 저장
      BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
      if (boardEntity == null) return IncreaseViewCountResponseDto.noExistBoard();

      boardEntity.increaseViewCount();
      boardRepository.save(boardEntity);
    } catch (Exception exception) {
        log.error("BoardService error", exception);
        return ResponseDto.databaseError();
    }

    return IncreaseViewCountResponseDto.success();
  }

  // 게시글 삭제 서비스 (작성자 본인만 가능)
  @Override
  @Transactional
  public ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(Integer boardNumber, String email) {

    try {
      // 유저/게시글 존재 여부 및 작성자 권한 확인
      boolean existedUser = userRepository.existsByEmail(email);
      if (!existedUser) return DeleteBoardResponseDto.noExistUser();

      BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
      if (boardEntity == null) return DeleteBoardResponseDto.noExistBoard();

      String writerEmail = boardEntity.getWriterEmail();
      boolean isWriter = writerEmail.equals(email);
      if (!isWriter) return DeleteBoardResponseDto.noPermission();

      // 연관 데이터(이미지/댓글/좋아요) 먼저 삭제 후 게시글 삭제
      imageRepository.deleteByBoardNumber(boardNumber);
      commentRepository.deleteByBoardNumber(boardNumber);
      favoriteRepository.deleteByBoardNumber(boardNumber);
      boardRepository.delete(boardEntity);

    } catch(Exception exception) {
      log.error("BoardService error", exception);
      return ResponseDto.databaseError();
    }

    return DeleteBoardResponseDto.success();

  }

}
