// 이 파일이 어디에 있는지 알려주는 주소
package com.example.foodtracking.controller;

// 필요한 도구들을 가져옵니다
import com.example.foodtracking.entity.Product;
import com.example.foodtracking.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// "이 클래스는 웹 요청을 처리하는 컨트롤러예요"라고 스프링에게 알려줍니다
@RestController
// "제품 관련 요청은 모두 /api/products로 시작해요"라고 알려줍니다
@RequestMapping("/api/products")
public class ProductController {

    // ProductService를 사용하기 위한 변수를 선언합니다
    private final ProductService productService;

    // ProductService를 자동으로 연결해줍니다
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET 요청으로 모든 제품 목록을 가져오는 메서드
    // 웹에서 /api/products 로 GET 요청이 오면 실행됩니다
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // GET 요청으로 특정 ID의 제품을 가져오는 메서드
    // 웹에서 /api/products/1 처럼 ID를 지정하여 요청이 오면 실행됩니다
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    // POST 요청으로 새 제품을 추가하는 메서드
    // 웹에서 /api/products 로 POST 요청이 오면 실행됩니다
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // PUT 요청으로 기존 제품을 수정하는 메서드
    // 웹에서 /api/products/1 처럼 ID를 지정하여 PUT 요청이 오면 실행됩니다
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);  // ID 설정
        return productService.saveProduct(product);
    }

    // DELETE 요청으로 제품을 삭제하는 메서드
    // 웹에서 /api/products/1 처럼 ID를 지정하여 DELETE 요청이 오면 실행됩니다
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}