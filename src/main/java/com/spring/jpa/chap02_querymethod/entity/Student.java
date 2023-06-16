package com.spring.jpa.chap02_querymethod.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Setter @Getter //실무적 측면에서 setter는 신중하게 만들 것
@ToString @EqualsAndHashCode(of = "id") //아이디 같으면 같은 객체로 인식
@NoArgsConstructor @AllArgsConstructor
@Builder @Entity
@Table(name = "tbl_student")
public class Student {

    @Id
    @Column(name = "stu_id")
    @GeneratedValue(generator = "uid")
    @GenericGenerator(strategy = "uuid", name="uid") //전략: uuid
    private String id; //변수명이 네임과 같으면 이 문장은 쓸 필요가 없다.

    @Column(name = "stu_name", nullable = false)
    private String name;

    private String city;
    private String major;
}
