<template>
  <div class="home-container">
    <el-container>
      <el-main class="center-content">
        <div class="step-container">
          <h1 class="section-title">
            一句话简介
          </h1>

          <div v-if="generatingSentence" class="edit-form">
            <div class="form-group">
              <label class="form-label">类型</label>
              <select class="form-select" v-model="sentenceForm.genre">
                <option value="悬疑">悬疑</option>
                <option value="爱情">爱情</option>
                <option value="武侠">武侠</option>
                <option value="玄幻">玄幻</option>
                <option value="文学">文学</option>
                <option value="历史">历史</option>
                <option value="情色">情色</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">主角身份</label>
              <textarea class="form-textarea" v-model="sentenceForm.identity" rows="1"></textarea>
            </div>
            <div class="form-actions">
              <el-button class="btn-secondary" @click="cancelGenerateSentence">取消</el-button>
              <el-button class="btn-primary" @click="submitSentencePrompt">生成</el-button>
            </div>
          </div>
          <div v-else-if="editingSentence" class="edit-form">
            <div class="form-group">
              <label class="form-label">类型</label>
              <select class="form-select" v-model="sentenceForm.genre">
                <option value="悬疑">悬疑</option>
                <option value="爱情">爱情</option>
                <option value="武侠">武侠</option>
                <option value="玄幻">玄幻</option>
                <option value="文学">文学</option>
                <option value="历史">历史</option>
                <option value="情色">情色</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">主角身份</label>
              <textarea class="form-textarea" v-model="sentenceForm.identity" rows="1"></textarea>
            </div>
            <div class="form-group">
              <label class="form-label">简单介绍</label>
              <textarea class="form-textarea" v-model="sentenceForm.content" rows="2"></textarea>
            </div>
            <div class="form-actions">
              <el-button class="btn-secondary" @click="cancelEditSentence">取消</el-button>
              <el-button class="btn-primary" @click="saveSentence">保存</el-button>
            </div>
          </div>
          <div v-else-if="!hasSentence && !editingSentence" class="empty-state">
            <div class="empty-icon">📖</div>
            <el-button class="btn-primary" @click="createSentence">
              <el-icon><Plus /></el-icon> 创建
            </el-button>
            <el-button class="btn-generate" @click="generateSentence">
              <el-icon><Promotion /></el-icon> AI生成
            </el-button>
          </div>
          <div v-else>
            <div v-if="outline.sentence.genre" class="structure-section">
              <h3 class="structure-title">类型</h3>
              <p class="structure-content">{{outline.sentence.genre}}</p>
            </div>
            <div v-if="outline.sentence.identity" class="structure-section">
              <h3 class="structure-title">主角身份</h3>
              <p class="structure-content">{{outline.sentence.identity}}</p>
            </div>
            <div v-if="outline.sentence.content" class="structure-section">
              <h3 class="structure-title">简单介绍</h3>
              <p class="structure-content">{{outline.sentence.content}}</p>
            </div>
            <el-button class="btn-secondary" @click="startEditSentence">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button class="btn-danger" @click="deleteSentence">
              <el-icon><Delete /></el-icon> 删除
            </el-button>

          </div>

          <h1 class="section-title">
            一段话简介
          </h1>

          <div v-if="editingBrief" class="edit-form">
            <div class="form-group">
              <label class="form-label">故事背景</label>
              <textarea class="form-textarea" v-model="briefForm.background" rows="1"></textarea>
            </div>
            <div class="form-group">
              <label class="form-label">开端</label>
              <textarea class="form-textarea" v-model="briefForm.beginning" rows="1"></textarea>
            </div>
            <div class="form-group">
              <label class="form-label">发展</label>
              <textarea class="form-textarea" v-model="briefForm.development" rows="1"></textarea>
            </div>
            <div class="form-group">
              <label class="form-label">高潮</label>
              <textarea class="form-textarea" v-model="briefForm.climax" rows="1"></textarea>
            </div>
            <div class="form-group">
              <label class="form-label">结局</label>
              <textarea class="form-textarea" v-model="briefForm.ending" rows="1"></textarea>
            </div>
            <div class="form-actions">
              <el-button class="btn-secondary" @click="cancelEditBrief">取消</el-button>
              <el-button class="btn-primary" @click="saveBrief">保存</el-button>
            </div>
          </div>
          <div v-else-if="!hasBrief && !editingBrief" class="empty-state">
            <div class="empty-icon">📖</div>
            <el-button class="btn-primary" @click="createBrief">
              <el-icon><Plus /></el-icon> 创建
            </el-button>
            <el-button class="btn-generate" @click="generateBrief">
              <el-icon><Promotion /></el-icon> AI生成
            </el-button>
          </div>
          <div v-else>
            <div v-if="outline.brief.background" class="structure-section">
              <h3 class="structure-title">故事背景</h3>
              <p class="structure-content">{{outline.brief.background}}</p>
            </div>
            <div v-if="outline.brief.beginning" class="structure-section">
              <h3 class="structure-title">开端</h3>
              <p class="structure-content">{{outline.brief.beginning}}</p>
            </div>
            <div v-if="outline.brief.development" class="structure-section">
              <h3 class="structure-title">发展</h3>
              <p class="structure-content">{{outline.brief.development}}</p>
            </div>
            <div v-if="outline.brief.climax" class="structure-section">
              <h3 class="structure-title">高潮</h3>
              <p class="structure-content">{{outline.brief.climax}}</p>
            </div>
            <div v-if="outline.brief.ending" class="structure-section">
              <h3 class="structure-title">结局</h3>
              <p class="structure-content">{{outline.brief.ending}}</p>
            </div>
            <el-button class="btn-secondary" @click="startEditBrief">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button class="btn-danger" @click="deleteBrief">
              <el-icon><Delete /></el-icon> 删除
            </el-button>

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
import {reactive, ref} from "vue";
import {generateOutline, queryBook, saveOutline} from "@/api/book.js";
import {ElMessage} from "element-plus";
import {Delete, Edit, Plus, Promotion} from "@element-plus/icons-vue";

// 数据
const currentBookId = ref('');

const outline = ref({
  sentence: null,
  brief: null,
  profiles: null,
});

const hasSentence = ref(false);
const hasBrief = ref(false);
const profileNum = ref(0);

const editingSentence = ref(false);
const generatingSentence = ref(false);
const editingBrief = ref(false);
const editingProfile = ref(null);

const sentenceForm = reactive({
  genre: '',
  identity: '',
  content: ''
});
const briefForm = reactive({
  background: '',
  beginning: '',
  development: '',
  climax: '',
  ending: ''
});
const profileForm = reactive({
  name: '',
  age: '',
  identity: '',
  values: []
});

const newValue = ref('');

// 创建一句话简介
const createSentence = () => {
  console.log('创建一句话简介')
  outline.value.sentence = {
    genre: '',
    identity: '',
    content: ''
  }
  startEditSentence()
}
// ai生成一句话简介
const generateSentence = () => {
  console.log('生成一句话简介')
  outline.value.sentence = {
    genre: '',
    identity: '',
    content: ''
  }
  Object.assign(sentenceForm, outline.value.sentence)
  generatingSentence.value = true
}
// 编辑一句话简介
const startEditSentence = () => {
  Object.assign(sentenceForm, outline.value.sentence)
  editingSentence.value = true;
};
// 一句话简介 取消编辑
const cancelEditSentence = () => {
  editingSentence.value = false;
}
// 一句话简介 取消ai生成
const cancelGenerateSentence = () => {
  generatingSentence.value = false;
}
// 提交一句话简介 prompt
const submitSentencePrompt = async () => {
  try {
    const prompt = {
      'sentence-genre': null,
      'sentence-identity': null
    }
    if (sentenceForm.genre) {
      prompt["sentence-genre"] = sentenceForm.genre
    }
    if (sentenceForm.identity) {
      prompt["sentence-identity"] = sentenceForm.identity
    }
    const book = await generateOutline(currentBookId.value, 'sentence', prompt)
    generatingSentence.value = false

    sentenceForm.genre = book?.outline?.sentence?.genre
    sentenceForm.identity = book?.outline?.sentence?.identity
    sentenceForm.content = book?.outline?.sentence?.content
    editingSentence.value = true
    ElMessage.success('生成一句话简介成功');
  } catch (error) {
    console.error('生成一句话简介失败:', error);
    ElMessage.error('生成一句话简介失败, 请稍后再试');
  }
}
const saveSentence = async () => {
  try {
    outline.value.sentence = { ...sentenceForm };
    const book = await saveOutline({
      'id': currentBookId.value,
      'outline': outline.value
    })
    flush(book)
    editingSentence.value = false;
    ElMessage.success('保存一句话简介成功');
  } catch (error) {
    console.error('保存一句话简介失败:', error);
    ElMessage.error('保存一句话简介失败, 请稍后再试');
  }
}
const deleteSentence = async () => {
  try {
    outline.value.sentence = null;
    const book = await saveOutline({
      'id': currentBookId.value,
      'outline': outline.value
    })
    flush(book)
    editingSentence.value = false;
    hasSentence.value = false;
    ElMessage.success('删除一句话简介成功');
  } catch (error) {
    console.error('删除一句话简介失败:', error);
    ElMessage.error('删除一句话简介失败, 请稍后再试');
  }
}

// 创建一段话简介
const createBrief = () => {
  console.log('创建一段话简介')
  outline.value.brief = {
    background: '',
    beginning: '',
    acy2: '',
    climax: '',
    acy4: ''
  }
  startEditBrief()
}
// ai生成一句话简介
const generateBrief = () => {
  console.log('生成一段话简介')
  outline.value.brief = {
    background: '',
    beginning: '',
    acy2: '',
    climax: '',
    acy4: ''
  }
  Object.assign(briefForm, outline.value.brief)
  submitBriefPrompt()

}
// 编辑一段话简介
const startEditBrief = () => {
  Object.assign(briefForm, outline.value.brief)
  editingBrief.value = true;
};
// 一句段简介 取消编辑
const cancelEditBrief = () => {
  editingBrief.value = false;
}
// 提交一段话简介 prompt
const submitBriefPrompt = async () => {
  try {
    const book = await generateOutline(currentBookId.value, 'brief', null)
    Object.assign(briefForm, book.outline.brief)
    editingBrief.value = true
    ElMessage.success('生成一段话简介成功');
  } catch (error) {
    console.error('生成一段话简介失败:', error);
    ElMessage.error('生成一段话简介失败, 请稍后再试');
  }
}
const saveBrief = async () => {
  try {
    outline.value.brief = { ...briefForm };
    const book = await saveOutline({
      'id': currentBookId.value,
      'outline': outline.value
    })
    flush(book)
    editingBrief.value = false;
    ElMessage.success('保存一段话简介成功');
  } catch (error) {
    console.error('保存一段话简介失败:', error);
    ElMessage.error('保存一段话简介失败, 请稍后再试');
  }
}
const deleteBrief = async () => {
  try {
    outline.value.brief = null;
    const book = await saveOutline({
      'id': currentBookId.value,
      'outline': outline.value
    })
    flush(book)
    editingBrief.value = false;
    hasBrief.value = false;
    ElMessage.success('删除一段话简介成功');
  } catch (error) {
    console.error('删除一段话简介失败:', error);
    ElMessage.error('删除一段话简介失败, 请稍后再试');
  }
}

const flush = (book) => {
  try {
    currentBookId.value = book.id
    if (book.outline) {
      outline.value = book.outline
    }

    hasSentence.value = book.outline?.sentence
    hasBrief.value = hasSentence && book.outline?.brief
    if (hasBrief.value && book.outline?.profiles) {
      profileNum.value = book.outline.profiles.length
    } else {
      profileNum.value = 0
    }
  } catch (error) {
    console.error('加载大纲内容失败, 请刷新页面')
  }
}

const loadOutline = async (bookId) => {
  try {
    const book = await queryBook(bookId)
    flush(book)
  } catch (error) {
    console.error('获取书籍大纲失败:', error);
    ElMessage.error('获取书籍大纲失败');
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

.structure-section {
  margin-bottom: 25px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 15px;
  transition: all 0.3s ease;
}

.structure-section:hover {
  background: rgba(255, 255, 255, 0.8);
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.structure-title {
  font-size: 18px;
  font-weight: 500;
  color: #4a5568;
  margin-bottom: 10px;
}

.structure-content {
  font-size: 16px;
  line-height: 1.8;
  color: #718096;
  white-space: pre-wrap;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  padding: 10px 20px;
  border-radius: 25px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.btn-generate {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  padding: 10px 20px;
  border-radius: 25px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.btn-generate:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.btn-secondary {
  background: rgba(160, 174, 192, 0.2);
  border: none;
  color: #4a5568;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.btn-secondary:hover {
  background: rgba(160, 174, 192, 0.3);
}

.btn-danger {
  background: rgba(245, 101, 101, 0.1);
  border: 1px solid rgba(245, 101, 101, 0.3);
  color: #f56565;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.btn-danger:hover {
  background: rgba(245, 101, 101, 0.2);
}


.edit-form {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 15px;
  padding: 25px;
  margin-top: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  font-size: 14px;
  color: #4a5568;
  margin-bottom: 8px;
  font-weight: 500;
}

.form-select {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  font-size: 15px;
  transition: all 0.3s ease;
  background: white;
}

.form-textarea:focus, .form-select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-textarea {
  width: 100%;
  resize: vertical;
  min-height: 120px;
  font-family: inherit;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #a0aec0;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 20px;
  opacity: 0.5;
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
