<template>
  <div class="home-container">
    <el-container>
      <el-main class="center-content">
        <div class="step-container">
          <div v-if="showSection.sentence">
            <h1 class="section-title">一句话简介</h1>
            <SentenceSection
              v-model:sentence-value="outline.sentence"
              :book-id="currentBookId"
              @save="save"
            />
          </div>

          <div v-if="showSection.brief">
            <h1 class="section-title">
              一段话简介
            </h1>
            <BriefSection
              v-model:brief-value="outline.brief"
              :book-id="currentBookId"
              @save="save"
            />
          </div>
        </div>
      </el-main>

      <el-aside width="300px" class="right-sidebar">
        <UserBar @book-changed="loadOutline"/>
      </el-aside>
    </el-container>
  </div>
</template>

<script setup>

import UserBar from "@/components/UserBar.vue";
import {queryBook, saveOutline} from "@/api/book.js";
import SentenceSection from "@/components/outline/SentenceSection.vue";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import BriefSection from "@/components/outline/BriefSection.vue";

// 数据
/* ------------------------------------ 数据 ------------------------------------ */
const steps = ['sentence', 'brief']

const currentBookId = ref('')
const outline = ref({ sentence: null, brief: null })
const showSection = ref({ sentence: false, brief: false })

/* ------------------------------------ 方法 ------------------------------------ */
async function loadOutline(bookId) {
  try {
    const book = await queryBook(bookId)
    renderPage(book)
  } catch {
    ElMessage.error('获取书籍大纲失败')
  }
}

function renderPage(book) {
  currentBookId.value = book.id
  outline.value = book.outline || { sentence: null, brief: null, profiles: null }

  let previous = true;
  steps.forEach(step => {
    showSection.value[step] = previous
    previous = !!outline.value[step]
  })
}

async function save() {
  try {
    const book = await saveOutline({ id: currentBookId.value, outline: outline.value })
    renderPage(book)
    ElMessage.success('保存成功')
  } catch {
    ElMessage.error('保存失败，请重试')
  }
}
</script>

<style scoped>
.section-title {
  font-size: 24px;
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.step-container {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 8px 32px rgba(31, 38, 135, 0.1);
  margin-bottom: 30px;
}

.el-aside::-webkit-scrollbar {
  display: none;
}

.home-container {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
  color: #4a5568;
}
</style>
