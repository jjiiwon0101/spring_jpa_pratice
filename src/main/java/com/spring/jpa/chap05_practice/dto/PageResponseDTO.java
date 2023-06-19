package com.spring.jpa.chap05_practice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class PageResponseDTO {

    private int startPage;

    private int endPage;
    private boolean prev;
    private boolean next;
}
