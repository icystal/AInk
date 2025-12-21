<script setup>
import {computed, reactive, ref} from "vue";
import {generateOutline} from "@/api/book.js";
import {ElMessage} from "element-plus";
import {Delete, DocumentAdd, Edit, Plus, Promotion} from "@element-plus/icons-vue";

const props = defineProps({
  briefValue: {
    type: Object,
    default: null
  },
  bookId: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['update:briefValue', 'save'])

/* -------------- 状态 -------------- */
const generating = ref(false)
const editing = ref(false)

const draft = reactive({background: '', beginning: '', development: '', climax: '', ending: ''});
const hasData = computed(() => !!props.briefValue)
const brief = computed(() => props.briefValue || {})

/* -------------- 方法 -------------- */
function create() {
  startEdit()
}
function startEdit() {
  resetDraft()
  Object.assign(draft, brief.value)
  editing.value = true
}
function cancelEdit() {
  editing.value = false
}
function resetDraft() {
  Object.keys(draft).forEach(key => {
    draft[key] = null;
  });
}

async function generate() {
  try {
    resetDraft()
    generating.value = true
    const book = await generateOutline(props.bookId, 'brief', null)
    Object.assign(draft, book.outline.brief)
    editing.value = true
    ElMessage.success('生成成功')
  } catch {
    ElMessage.error('生成失败，请重试')
  } finally {
    generating.value = false
  }
}

function save() {
  emit('update:briefValue', { ...draft })
  emit('save')
  editing.value = false
}
function del() {
  emit('update:briefValue', null)
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

    <!-- 编辑 -->
    <div v-if="editing" class="edit-form">
      <div class="form-group">
        <label class="form-label">故事背景</label>
        <textarea class="form-textarea" v-model="draft.background" rows="1"></textarea>
      </div>
      <div class="form-group">
        <label class="form-label">开端</label>
        <textarea class="form-textarea" v-model="draft.beginning" rows="1"></textarea>
      </div>
      <div class="form-group">
        <label class="form-label">发展</label>
        <textarea class="form-textarea" v-model="draft.development" rows="1"></textarea>
      </div>
      <div class="form-group">
        <label class="form-label">高潮</label>
        <textarea class="form-textarea" v-model="draft.climax" rows="1"></textarea>
      </div>
      <div class="form-group">
        <label class="form-label">结局</label>
        <textarea class="form-textarea" v-model="draft.ending" rows="1"></textarea>
      </div>
      <div class="form-actions">
        <el-button class="btn-cancel" @click="cancelEdit">取消</el-button>
        <el-button class="btn-primary" @click="save">保存</el-button>
      </div>
    </div>

    <!-- 展示 -->
    <div v-else-if="hasData">
      <div v-if="brief.background" class="structure-section">
        <h3 class="structure-title">故事背景</h3>
        <p class="structure-content">{{brief.background}}</p>
      </div>
      <div v-if="brief.beginning" class="structure-section">
        <h3 class="structure-title">开端</h3>
        <p class="structure-content">{{brief.beginning}}</p>
      </div>
      <div v-if="brief.development" class="structure-section">
        <h3 class="structure-title">发展</h3>
        <p class="structure-content">{{brief.development}}</p>
      </div>
      <div v-if="brief.climax" class="structure-section">
        <h3 class="structure-title">高潮</h3>
        <p class="structure-content">{{brief.climax}}</p>
      </div>
      <div v-if="brief.ending" class="structure-section">
        <h3 class="structure-title">结局</h3>
        <p class="structure-content">{{brief.ending}}</p>
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





















