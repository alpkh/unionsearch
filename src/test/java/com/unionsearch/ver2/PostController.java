package com.unionsearch.ver2;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    // 메모리 내 게시글 데이터를 저장하는 간단한 리스트
    private List<String> posts = new ArrayList<>();

    // 모든 게시글 조회
    @GetMapping
    public List<String> getAllPosts() {
        return posts;
    }

    // 새로운 게시글 추가
    @PostMapping
    public String createPost(@RequestBody String post) {
        posts.add(post);
        return "Post added successfully";
    }
}
