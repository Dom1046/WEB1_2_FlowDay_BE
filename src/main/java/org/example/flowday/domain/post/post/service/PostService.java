package org.example.flowday.domain.post.post.service;

import lombok.RequiredArgsConstructor;
import org.example.flowday.domain.course.course.entity.Course;
import org.example.flowday.domain.course.course.repository.CourseRepository;
import org.example.flowday.domain.member.entity.Member;
import org.example.flowday.domain.member.exception.MemberException;
import org.example.flowday.domain.member.repository.MemberRepository;
import org.example.flowday.domain.post.post.dto.PostRequestDTO;
import org.example.flowday.domain.post.post.dto.PostResponseDTO;
import org.example.flowday.domain.post.post.entity.Post;
import org.example.flowday.domain.post.post.mapper.PostMapper;
import org.example.flowday.domain.post.post.repository.PostRepository;
import org.example.flowday.global.security.util.SecurityUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;

    // 게시글 생성
    @Transactional
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO , Long userId) {
        Member writer = memberRepository.findById(userId).orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);
        Course course = courseRepository.findById(postRequestDTO.getCourseId()).get();
        Post post = postMapper.toEntity(postRequestDTO , writer ,course);
        Post savedPost = postRepository.save(post);
        return postMapper.toResponseDTO(savedPost);
    }

    // 게시글 조회 - ID
    public Optional<PostResponseDTO> getPostById(Long id) {
        return postRepository.findById(id).map(postMapper::toResponseDTO);
    }

    // 모든 게시글 조회
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDTO> responseDTOList = new ArrayList<>();
        for (Post post : posts) {
            responseDTOList.add(postMapper.toResponseDTO(post));
        }
        return responseDTOList;
    }

    // 게시글 수정
//    @Transactional
//    public PostResponseDTO updatePost(Long id, PostRequestDTO updatedPostDTO) {
//        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
//        post.setTitle(updatedPostDTO.getTitle());
//        post.setContents(updatedPostDTO.getContents());
//        post.setCity(updatedPostDTO.getCity());
//        post.setCourseId(updatedPostDTO.getCourseId());
//        return postMapper.toResponseDTO(post);
//    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}