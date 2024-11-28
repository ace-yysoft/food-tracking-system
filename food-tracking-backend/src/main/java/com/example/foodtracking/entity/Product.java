package com.example.foodtracking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity                // 이 클래스가 데이터베이스 테이블과 매핑됨을 나타냄
@Getter @Setter       // Lombok이 getter/setter 메서드를 자동 생성
public class Product {
    @Id     // 기본키(Primary Key) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)    // ID 자동 생성
    private Long id;

    @Column(nullable = false)    // NOT NULL 설정
    private String productCode;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String category;
}