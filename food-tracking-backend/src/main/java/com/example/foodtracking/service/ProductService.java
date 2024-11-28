package com.example.foodtracking.service;

import com.example.foodtracking.entity.Product;
import com.example.foodtracking.repository.ProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service    // 이 클래스가 서비스 계층임을 표시
@Transactional  // 트랜잭션 관리를 위한 어노테이션
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired  // 의존성 주입
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 제품 목록 조회
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 제품 저장 (추가/수정)
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // ID로 제품 조회
    public Product getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // 제품 삭제
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}