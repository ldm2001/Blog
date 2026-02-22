package com.blog.board_back.dto.object;

import java.util.ArrayList;
import java.util.List;

import com.blog.board_back.repository.resultSet.GetCommentListResultSet;

import lombok.AllArgsConstructor; 
import lombok.Getter;            
import lombok.NoArgsConstructor; 

@Getter // 모든 필드의 getter 메서드 자동 생성
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성
public class CommentListItem {
    // 댓글 작성자 닉네임
    private String nickname;
    // 댓글 작성자 프로필 이미지
    private String profileImage;
    // 댓글 작성 시간(문자열)
    private String writeDatetime;
    // 댓글 본문 내용
    private String content;

    // ResultSet에서 CommentListItem으로 변환하는 생성자
    public CommentListItem(GetCommentListResultSet resultSet) {
        this.nickname = resultSet.getNickname();
        this.profileImage = resultSet.getProfileImage();
        this.writeDatetime = resultSet.getWriteDatetime().toString();
        this.content = resultSet.getContent();
    }

    // GetCommentListResultSet 리스트를 CommentListItem 리스트로 변환하는 static 메서드
    public static List<CommentListItem> copyList(List<GetCommentListResultSet> resultSets) {
        List<CommentListItem> list = new ArrayList<>();
        // 각 ResultSet 객체를 CommentListItem으로 변환해서 리스트에 추가
        for (GetCommentListResultSet resultSet: resultSets) {
            CommentListItem commentListItem = new CommentListItem(resultSet);
            list.add(commentListItem);
        }
        return list; // 변환된 리스트 반환
    }
}

