package com.springboot.dogmeeting.api.blog;

import com.springboot.dogmeeting.api.blog.dto.AddArticleRequest;
import com.springboot.dogmeeting.datasource.article.Article;
import com.springboot.dogmeeting.datasource.article.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor    // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }
}