package com.spring.jpa.chap05_practice.dto;

import com.spring.jpa.chap05_practice.entity.Post;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PostCreateDTO {

    @NotBlank
    @Size(min = 2, max = 5)
    private String writer;

    // @NotNull -> null을 허용하지 않음. "", " "은 허용
    // @NotEmpty -> null, ""을 허용하지 않음. " "은 허용
    @NotBlank
    @Size(min = 1, max = 20)
    private String title;

    private String content;

    private List<String> hashTags;

    //dto를 엔티티로 변환하는 메서드
    public Post toEntity() {
        return Post.builder()
              .writer(this.writer)
              .title(this.title)
              .content(this.content)
              // 해시태그는 여기서 넣는게 아니에요~~ (해시태그에 대한 컬럼이 아예 존재하지 않아서)
              .build();
    }
}
