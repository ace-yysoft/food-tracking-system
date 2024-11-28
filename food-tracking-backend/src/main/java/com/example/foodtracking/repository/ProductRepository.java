package com.example.foodtracking.repository;

import com.example.foodtracking.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository를 상속받으면 기본적인 CRUD 메서드가 자동으로 생성됩니다.
    // findAll(), findById(), save(), deleteById() 등
}