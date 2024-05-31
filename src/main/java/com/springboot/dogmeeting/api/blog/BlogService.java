package com.springboot.dogmeeting.api.blog;

import com.springboot.dogmeeting.api.blog.dto.AddArticleRequest;
import com.springboot.dogmeeting.api.blog.dto.UpdateArticleRequest;
import com.springboot.dogmeeting.datasource.article.Article;
import com.springboot.dogmeeting.datasource.article.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor    // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    // 데이터베이스에 저장되어 있는 글 하나 가져오는 메소드
    public Article findById(Long id){
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        article.update(request.getTitle(), request.getContent());
        return article;
    }

    public void delete(Long id) {
        blogRepository.deleteById(id);
    }

}
