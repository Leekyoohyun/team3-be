package likelion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion.dto.PostCreateRequestDto;
import likelion.dto.PostResponseDto;
import likelion.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
@Tag(name = "Post", description = "게시글/dto에 String으로 되어있는데 -> Json파일 넣어서 테스트해주세요")
public class PostController {

    private final PostService postService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart("dto") String dtoString,
                                  @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        PostCreateRequestDto dto = objectMapper.readValue(dtoString, PostCreateRequestDto.class);
        Long id = postService.createPost(dto, image);
        return ResponseEntity.created(URI.create("/api/posts/" + id))
                .body(Map.of("id", id));
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {
        PostResponseDto post = postService.findOne(postId);
        return ResponseEntity.ok(post);
    }
}
