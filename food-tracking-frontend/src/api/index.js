import axios from 'axios'

// axios 인스턴스 생성
const api = axios.create({
  baseURL: 'http://localhost:8080/api',  // 백엔드 서버 주소
  timeout: 10000,  // 10초
  headers: {
    'Content-Type': 'application/json'
  }
})

// 제품 관련 API
export const productApi = {
  // 제품 목록 조회
  getProducts: (params) => api.get('/products', { params }),
  
  // 제품 상세 조회
  getProduct: (id) => api.get(`/products/${id}`),
  
  // 제품 추가
  createProduct: (data) => api.post('/products', data),
  
  // 제품 수정
  updateProduct: (id, data) => api.put(`/products/${id}`, data),
  
  // 제품 삭제
  deleteProduct: (id) => api.delete(`/products/${id}`)
}