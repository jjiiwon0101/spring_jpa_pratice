package com.spring.jpa.chap05_practice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.jpa.chap05_practice.entity.HashTag;
import com.spring.jpa.chap05_practice.entity.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PostDetailResponseDTO {

    private String author;
    private String title;
    private String content;
    private List<String> hashTags;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDate;

    //Entity를 DTO로 변환하는 생성자 (return타입 없음)
    public PostDetailResponseDTO(Post post) {
        this.author = post.getWriter();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.regDate = post.getCreateDate();
        //HashTag 같은 경우에는 가공해서 집어넣어야 합니다. (리스트의 타입이 서로 다름.)
        //전통적 방식
//        List<String> list = new ArrayList<>();
//        for (HashTag ht : post.getHashTags()) {
//            list.add(ht.getTagName());
//        }
//        this.hashTags = list;

        //간결한 방식, 성능이 일반 for문보다 더 좋음.
        this.hashTags = post.getHashTags()
                .stream() //stream 객체를 받아옴. (컬렉션 데이터를 함수선언 형식으로 처리할 수 있게 해 주는 객체)
                // 스트림 내의 요소들에 대해 함수가 적용된 결과를 새로운 요소로 맵핑.
                .map(HashTag::getTagName)
                //스트림 객체를 새로운 리스트의 형태로 리턴. -> this.hashTags에게 대입
                .collect(Collectors.toList());
    }
}
