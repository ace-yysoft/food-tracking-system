<template>
    <div class="product-list">
      <!-- 검색 카드 -->
      <el-card class="search-card">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="제품명">
            <el-input
              v-model="searchForm.productName"
              placeholder="제품명 검색"
              clearable
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="카테고리">
            <el-select 
              v-model="searchForm.category" 
              placeholder="전체"
              clearable
            >
              <el-option label="음료" value="음료" />
              <el-option label="과자" value="과자" />
              <el-option label="유제품" value="유제품" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">검색</el-button>
          </el-form-item>
        </el-form>
      </el-card>
  
      <!-- 목록 카드 -->
      <el-card class="list-card">
        <template #header>
          <div class="card-header">
            <span>제품 목록</span>
            <el-button type="primary" @click="handleAdd">새 제품</el-button>
          </div>
        </template>
  
        <el-table 
          v-loading="loading"
          :data="paginatedProducts" 
          border 
          style="width: 100%"
        >
          <el-table-column prop="productCode" label="제품 코드" width="120" />
          <el-table-column prop="productName" label="제품명" />
          <el-table-column prop="category" label="카테고리" width="120" />
          <el-table-column label="작업" width="150">
            <template #default="scope">
              <el-button-group>
                <el-button size="small" @click="handleEdit(scope.row)">수정</el-button>
                <el-button 
                  size="small" 
                  type="danger" 
                  @click="handleDelete(scope.row)"
                >삭제</el-button>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
  
        <!-- 페이지네이션 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[5, 10, 20, 50]"
            layout="total, sizes, prev, pager, next"
            :total="filteredProducts.length"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
  
      <!-- 다이얼로그 -->
      <el-dialog
        v-model="dialogVisible"
        :title="isEdit ? '제품 수정' : '새 제품'"
        width="500px"
      >
        <el-form :model="form" label-width="100px">
          <el-form-item label="제품명">
            <el-input v-model="form.productName" />
          </el-form-item>
          <el-form-item label="제품 코드">
            <el-input v-model="form.productCode" />
          </el-form-item>
          <el-form-item label="카테고리">
            <el-select v-model="form.category" placeholder="카테고리 선택">
              <el-option label="음료" value="음료" />
              <el-option label="과자" value="과자" />
              <el-option label="유제품" value="유제품" />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">취소</el-button>
            <el-button type="primary" @click="handleSubmit">확인</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import { ref, computed, onMounted } from 'vue'
  import { ElMessageBox, ElMessage } from 'element-plus'
  
  export default {
    name: 'ProductList',
    setup() {
      const products = ref([])
      const loading = ref(false)
      
      // API에서 제품 목록 가져오기
      const fetchProducts = async () => {
        try {
          loading.value = true
          const response = await fetch('http://localhost:8080/api/products')
          if (!response.ok) {
            throw new Error('Network response was not ok')
          }
          const data = await response.json()
          products.value = data
          console.log('Fetched products:', data)  // 데이터 확인용
        } catch (error) {
          console.error('Error fetching products:', error)
          ElMessage.error('제품 목록을 불러오는데 실패했습니다.')
        } finally {
          loading.value = false
        }
      }
  
      const dialogVisible = ref(false)
      const isEdit = ref(false)
      const editingId = ref(null)
      const form = ref({
        productName: '',
        productCode: '',
        category: ''
      })
  
      const searchForm = ref({
        productName: '',
        category: ''
      })
  
      const currentPage = ref(1)
      const pageSize = ref(10)
  
      const filteredProducts = computed(() => {
        return products.value.filter(product => {
          const nameMatch = product.productName.toLowerCase().includes(searchForm.value.productName.toLowerCase())
          const categoryMatch = !searchForm.value.category || product.category === searchForm.value.category
          return nameMatch && categoryMatch
        })
      })
  
      const paginatedProducts = computed(() => {
        const start = (currentPage.value - 1) * pageSize.value
        const end = start + pageSize.value
        return filteredProducts.value.slice(start, end)
      })
  
      const handleAdd = () => {
        isEdit.value = false
        editingId.value = null
        form.value = {
          productName: '',
          productCode: '',
          category: ''
        }
        dialogVisible.value = true
      }
  
      const handleEdit = (row) => {
        isEdit.value = true
        editingId.value = row.id
        form.value = { ...row }
        dialogVisible.value = true
      }
  
      const handleDelete = async (row) => {
        try {
          await ElMessageBox.confirm('이 제품을 삭제하시겠습니까?', '경고')
          loading.value = true
          
          const response = await fetch(`http://localhost:8080/api/products/${row.id}`, {
            method: 'DELETE'
          })
          
          if (response.ok) {
            ElMessage.success('삭제되었습니다.')
            fetchProducts()  // 목록 새로고침
          }
        } catch (error) {
          if (error !== 'cancel') {
            ElMessage.error('삭제하는데 실패했습니다.')
            console.error('Error:', error)
          }
        } finally {
          loading.value = false
        }
      }
  
      const handleSubmit = async () => {
        try {
          loading.value = true
          const url = isEdit.value 
            ? `http://localhost:8080/api/products/${editingId.value}`
            : 'http://localhost:8080/api/products'
          
          const response = await fetch(url, {
            method: isEdit.value ? 'PUT' : 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(form.value)
          })
          
          if (response.ok) {
            ElMessage.success(isEdit.value ? '수정되었습니다.' : '추가되었습니다.')
            dialogVisible.value = false
            fetchProducts()  // 목록 새로고침
          }
        } catch (error) {
          ElMessage.error('저장하는데 실패했습니다.')
          console.error('Error:', error)
        } finally {
          loading.value = false
        }
      }
  
      const handleSearch = () => {
        currentPage.value = 1
      }
  
      const handleSizeChange = (val) => {
        pageSize.value = val
        currentPage.value = 1
      }
  
      const handleCurrentChange = (val) => {
        currentPage.value = val
      }
  
      onMounted(() => {
        fetchProducts()
      })
  
      return {
        products: paginatedProducts,
        loading,
        searchForm,
        dialogVisible,
        form,
        currentPage,
        pageSize,
        handleAdd,
        handleEdit,
        handleDelete,
        handleSubmit,
        handleSearch,
        handleSizeChange,
        handleCurrentChange,
        filteredProducts,
        paginatedProducts
      }
    }
  }
  </script>
  
  <style scoped>
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
  
  .search-card {
    margin-bottom: 20px;
  }
  
  .list-card {
    margin-bottom: 20px;
  }
  
  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
  </style>


# Food Tracking System

A web application built with Spring Boot and Vue.js for tracking food products.

## Features
- Product management (CRUD)
- Search functionality
- Pagination

## Tech Stack
- Backend: Spring Boot, H2 Database
- Frontend: Vue.js, Element Plus

## Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- npm or yarn

## Installation & Setup

### Backend (Spring Boot)
1. Clone the repository