<template>
  <div class="home-container">
    <el-container>
      <el-aside width="250px" class="left-sidebar">

      </el-aside>

      <el-main class="center-content">
      </el-main>

      <el-aside width="300px" class="right-sidebar">
        <UserBar/>
      </el-aside>
    </el-container>
  </div>
</template>

<script setup>

import UserBar from "@/components/UserBar.vue";
</script>

<style scoped>
.el-aside::-webkit-scrollbar {
  display: none;
}
</style>
