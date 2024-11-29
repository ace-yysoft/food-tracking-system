<template>
  <div class="home">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>대시보드</span>
        </div>
      </template>
      <div class="welcome-message">
        <h2>식품이력추적시스템에 오신 것을 환영합니다</h2>
        <p>왼쪽 메뉴에서 원하는 기능을 선택하세요.</p>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'HomeView'
}
</script>

<style scoped>
.welcome-message {
  text-align: center;
  padding: 40px 0;
}

.welcome-message h2 {
  color: #303133;
  margin-bottom: 20px;
}

.welcome-message p {
  color: #606266;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 