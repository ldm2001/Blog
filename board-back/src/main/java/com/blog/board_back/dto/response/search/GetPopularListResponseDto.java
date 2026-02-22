package com.blog.board_back.dto.response.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;         
import org.springframework.http.ResponseEntity;    

import com.blog.board_back.common.ResponseCode;
import com.blog.board_back.common.ResponseMessage;
import com.blog.board_back.dto.response.ResponseDto;
import com.blog.board_back.repository.resultSet.GetPopularListResultSet;

import lombok.Getter;

@Getter
public class GetPopularListResponseDto extends ResponseDto {

    // 인기 검색어 리스트
    private List<String> popularWordList;

    // 인기 검색어 결과 집합 리스트를 받아 문자열 리스트로 변환하는 private 생성자
    private GetPopularListResponseDto(List<GetPopularListResultSet> resultSets) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 성공 코드/메시지 세팅

        List<String> popularWordList = new ArrayList<>(); // 검색어 리스트 초기화
        for (GetPopularListResultSet resultSet: resultSets){
            String popularWord = resultSet.getSearchWord(); // 인기 검색어 추출
            popularWordList.add(popularWord); // 리스트에 추가
        }

        this.popularWordList = popularWordList; // 필드에 세팅
    }

    // 인기 검색어 조회 성공 응답 생성용 static 메서드
    public static ResponseEntity<GetPopularListResponseDto> success(List<GetPopularListResultSet> resultSets) {
        GetPopularListResponseDto result = new GetPopularListResponseDto(resultSets); // DTO 생성
        return ResponseEntity.status(HttpStatus.OK).body(result); // 200 반환
    }

}
