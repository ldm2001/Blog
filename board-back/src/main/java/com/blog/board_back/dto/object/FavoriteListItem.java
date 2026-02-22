package com.blog.board_back.dto.object;

import java.util.ArrayList;
import java.util.List;

import com.blog.board_back.repository.resultSet.GetFavoriteListResultSet;

import lombok.AllArgsConstructor; 
import lombok.Getter;            
import lombok.NoArgsConstructor; 

@Getter // 모든 필드 getter 메서드 자동 생성
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
@AllArgsConstructor // 전체 필드 초기화 생성자 자동 생성
public class FavoriteListItem {
    // 즐겨찾기(좋아요)한 사용자의 이메일
    private String email;
    // 즐겨찾기한 사용자의 닉네임
    private String nickname;
    // 즐겨찾기한 사용자의 프로필 이미지
    private String profileImage;

    // ResultSet 객체에서 FavoriteListItem으로 변환하는 생성자
    public FavoriteListItem(GetFavoriteListResultSet resultSet) {
        this.email = resultSet.getEmail();
        this.nickname = resultSet.getNickname();
        this.profileImage = resultSet.getProfileImage();
    }

    // GetFavoriteListResultSet 리스트를 FavoriteListItem 리스트로 변환하는 static 메서드
    public static List<FavoriteListItem> copyList(List<GetFavoriteListResultSet> resultSets) {
        List<FavoriteListItem> list = new ArrayList<>();
        // 각 ResultSet 객체를 FavoriteListItem으로 변환해서 리스트에 추가
        for (GetFavoriteListResultSet resultSet: resultSets) {
            FavoriteListItem favoriteListItem = new FavoriteListItem(resultSet);
            list.add(favoriteListItem);
        }
        return list; // 변환된 리스트 반환
    }
}

