package com.springboot.dogmeeting.api.blog.dto;

import com.springboot.dogmeeting.datasource.article.Article;
import lombok.Getter;

@Getter
public class AritcleResponse {

    private final String title;
    private final String content;

    public AritcleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
