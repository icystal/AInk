<script setup>
import {computed, reactive, ref} from "vue";
import {generateOutline} from "@/api/book.js";
import {ElMessage} from "element-plus";
import {Delete, DocumentAdd, Edit, Plus, Promotion} from "@element-plus/icons-vue";

const props = defineProps({
  sentenceValue: {
    type: Object,
    default: null
  },
  bookId: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['update:sentenceValue', 'save'])

/* -------------- 状态 -------------- */
const showGenerateDialog = ref(false)
const generating = ref(false)
const editing = ref(false)

const draft = reactive({ genre: '', identity: '', content: '' })
const hasData = computed(() => !!props.sentenceValue)
const sentence = computed(() => props.sentenceValue || {})

/* -------------- 方法 -------------- */
function create() {
  startEdit()
}
function startEdit() {
  resetGenerateForm()
  Object.assign(draft, sentence.value)
  editing.value = true
}
function cancelEdit() {
  editing.value = false
}
function generate() {
  resetGenerateForm()
  showGenerateDialog.value = true
}
function resetGenerateForm() {
  draft.genre = ''
  draft.identity = ''
  draft.content = ''
  generating.value = false
}
async function submitGenerate() {
  generating.value = true
  try {
    const prompt = {}
    if (draft.genre) {
      prompt['sentence-genre'] = draft.genre
    }
    if (draft.identity) {
      prompt['sentence-identity'] = draft.identity
    }
    const book = await generateOutline(props.bookId, 'sentence', prompt)
    Object.assign(draft, book.outline.sentence)
    showGenerateDialog.value = false
    editing.value = true
    ElMessage.success('生成成功')
  } catch {
    ElMessage.error('生成失败，请重试')
  } finally {
    generating.value = false
  }
}
function save() {
  emit('update:sentenceValue', { ...draft })
  emit('save')
  editing.value = false
}
function del() {
  emit('update:sentenceValue', null)
  emit('save')
}

</script>

<template>
  <div>
    <!-- 空状态 -->
    <div v-if="!hasData && !editing && !generating" class="empty-state">
      <el-icon :size="60">
        <DocumentAdd />
      </el-icon>
      <div>
        <el-button class="btn-primary" @click="create">
          <el-icon><Plus /></el-icon> 创建
        </el-button>
        <el-button class="btn-generate" @click="generate">
          <el-icon><Promotion /></el-icon> AI生成
        </el-button>
      </div>
    </div>

    <!-- AI 生成弹窗 -->
    <el-dialog
      append-to-body
      v-model="showGenerateDialog"
      title="AI 生成小说设定"
      width="420px"
      :close-on-click-modal="false"
    >
      <div class="form-group">
        <label class="form-label">类型</label>
        <select class="form-select" v-model="draft.genre">
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
        <textarea class="form-textarea" v-model="draft.identity" rows="1"/>
      </div>
      <template #footer>
        <el-button class="btn-cancel" @click="showGenerateDialog = false">取消</el-button>
        <el-button class="btn-generate" :loading="generating" @click="submitGenerate">生成</el-button>
      </template>
    </el-dialog>

    <!-- 编辑 -->
    <div v-if="editing" class="edit-form">
      <div class="form-group">
        <label class="form-label">类型</label>
        <select class="form-select" v-model="draft.genre">
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
        <textarea class="form-textarea" v-model="draft.identity" rows="1"/>
      </div>
      <div class="form-group">
        <label class="form-label">简单介绍</label>
        <textarea class="form-textarea" v-model="draft.content" rows="2"/>
      </div>
      <div class="form-actions">
        <el-button class="btn-cancel" @click="cancelEdit">取消</el-button>
        <el-button class="btn-primary" @click="save">保存</el-button>
      </div>
    </div>

    <!-- 展示 -->
    <div v-else-if="hasData">
      <div v-if="sentence.genre" class="structure-section">
        <h3 class="structure-title">类型</h3>
        <p class="structure-content">{{ sentence.genre }}</p>
      </div>
      <div v-if="sentence.identity" class="structure-section">
        <h3 class="structure-title">主角身份</h3>
        <p class="structure-content">{{ sentence.identity }}</p>
      </div>
      <div v-if="sentence.content" class="structure-section">
        <h3 class="structure-title">简单介绍</h3>
        <p class="structure-content">{{ sentence.content }}</p>
      </div>
      <el-button class="btn-primary" @click="startEdit">
        <el-icon><Edit /></el-icon> 编辑
      </el-button>
      <el-button class="btn-delete" @click="del">
        <el-icon><Delete /></el-icon> 删除
      </el-button>
    </div>
  </div>
</template>

<style>
@import "@/assets/css/outline-common.css";
</style>




















