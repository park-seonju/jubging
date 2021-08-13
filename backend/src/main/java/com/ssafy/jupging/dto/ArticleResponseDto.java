package com.ssafy.jupging.dto;

import com.ssafy.jupging.domain.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {

    private Long articleId;
    private String content;
    private String photosPath;
    private int likeCnt;
    private LocalDateTime createdDate;
    private String date;
    private Long userId;

    private int commentCnt;

    private String nickname;

    private String profilePath;

    private List<String> hashlist;

    public ArticleResponseDto(Article article){
        this.articleId=article.getArticleId();
        this.content=article.getContent();
        this.photosPath=article.getPhotosPath();
        this.likeCnt=article.getLikeCnt();
        this.userId=article.getUserId();
        this.createdDate = article.getCreatedDate();
        String[] date = article.getCreatedDate().toString().split("T");
        this.date = date[0];
    }

}
