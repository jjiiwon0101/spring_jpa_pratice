package com.spring.jpa.chap05_practice.service;


import com.spring.jpa.chap05_practice.dto.*;
import com.spring.jpa.chap05_practice.entity.HashTag;
import com.spring.jpa.chap05_practice.entity.Post;
import com.spring.jpa.chap05_practice.repository.HashTagRepository;
import com.spring.jpa.chap05_practice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional //jpa 레파지토리는 트랜젝션 단위로 동작하기 땨문에 작성해 주세요!
public class PostService {
    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;

    public PostListResponseDTO getPosts(PageDTO dto) {

        //pageable 객체 생성
        Pageable pagealbe = PageRequest.of(
                dto.getPage() - 1,
                dto.getSize(),
                Sort.by("createDate").descending()
        );

        //데이터베이스에서 게시물 목록 조회
        Page<Post> posts = postRepository.findAll(pagealbe);

        //게시물 정보만 꺼내기
        List<Post> postList = posts.getContent();


        //엔티티 객체를 DTO 객체로 변환한 결과 리스트.
        List<PostDetailResponseDTO> detailList
        = postList.stream()
                .map(post -> new PostDetailResponseDTO(post))
                .collect(Collectors.toList());

        //DB에서 조회한 정보(ENTITY)를 JSON 형태에 맞는 DTO로 변환
         PostListResponseDTO responseDTO = PostListResponseDTO.builder()
                .count(detailList.size()) //총 게시물 수가 아니라 조회된 게시물 수
                .pageInfo(new PageResponseDTO(posts))
                .posts(detailList)
                .build();

        return responseDTO;
    }

    public PostDetailResponseDTO getDetail(long id) {
        Post postEntity = postRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(id + "번 게시물이 존재하지 않습니다.")
                );

        return new PostDetailResponseDTO(postEntity);
    }

    public PostDetailResponseDTO insert(final PostCreateDTO dto) throws RuntimeException{
        //게시물 저장
         Post saved = postRepository.save(dto.toEntity()); //엔티티밖에 안받도록 선언.

        //해시태그 저장
        List<String> hashTags = dto.getHashTags();
        if(hashTags != null && hashTags.size() > 0) {
            hashTags.forEach(ht -> {

                HashTag savedTag = hashTagRepository.save(
                        HashTag.builder()
                                .tagName(ht)
                                .post(saved)
                                .build()
                );
                //Post Entity는 DB에 save를 진행할 때 HashTag에 대한 내용을 갱신하지 않습니다.
                //HashTag Entity는 따로 save를 진행합니다.
                //HashTag는 연관관계의 주인이기 때문에 save를 진행할 때 Post를 전달하기 때문에
                //DB와 Entity와의 상태가 도일하지만, Post는 HashTag의 정보가 비어있는 상태입니다.
                //Post Entity에 연관관게 편의 메서드를 작성하여 HashTag의 내용을 동기화 해야
                //추후에 진행되는 과정에서 문제가 발생하지 않습니다. (연관관계의 주인이 아니기 때문에)
                saved.addHashTag(savedTag);

            });
        }
        return new PostDetailResponseDTO(saved);
    }

    private Post getPost(long id) {
        Post postEntity = postRepository.findById(id)
                .orElseThrow(
                () -> new RuntimeException(id + "번 게시물이 존재하지 않습니다.")
                );
        return postEntity;
    }

    public PostDetailResponseDTO modify(PostModifyDTO dto) {
        Post postEntity = getPost(dto.getPostNo());

        //수정 시작
        postEntity.setTitle(dto.getTitle());
        postEntity.setContent(dto.getContent());

        //수정 완료
        Post modifiedPost = postRepository.save(postEntity);

        return new PostDetailResponseDTO(modifiedPost);
    }

    public void delete(long id) {
        postRepository.deleteById(id);
    }
}
