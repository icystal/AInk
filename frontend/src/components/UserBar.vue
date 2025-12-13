<script setup>
import {onMounted, ref} from "vue";
import {createBook, deleteBook, querySpines, renameBook} from "@/api/book.js";
import {ElMessage, ElMessageBox, ElCollapse, ElCollapseItem} from "element-plus";
import {MoreFilled} from "@element-plus/icons-vue";

  // 定义事件发射器
  const emit = defineEmits(['book-changed']);

  // 书架展开状态
  const activeNames = ref(['spine']);

  // 书籍列表
  const spineList = ref([]);

  const currentBookId = ref('-1')

  const loadSpines = async () => {
    try {
      spineList.value = await querySpines()
      if (spineList.value?.length) {
        console.log('默认选中的书籍', spineList.value[0])
        changeBook(spineList.value[0].id)
      }
    } catch (error) {
      console.error('获取书架内容失败:', error);
      ElMessage.error('获取书架内容失败');
    }
  }

  // 选择书籍
  const changeBook = (bookId) => {
    console.log(`切换书籍: ${currentBookId.value} -> ${bookId}`)
    currentBookId.value = bookId;
    emit('book-changed', bookId);
  };

  const handleRename = async (spine) => {
    try {
      const { value } = await ElMessageBox.prompt(
        '请输入新的书籍名称',
        '重命名书籍',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: spine.title,
          inputPattern: /\S+/,
          inputErrorMessage: '书籍名称不能为空'
        }
      );

      const book = await renameBook(spine.id, value)
      spine.title = value
      if (currentBookId === spine.id) {
        changeBook(book.id)
      }
      ElMessage.success('重命名成功');
    } catch (error) {
      if (error !== 'cancel') {
        console.error('重命名失败:', error);
        ElMessage.error('重命名失败');
      }
    }
  }

  const handleDelete = async (spine) => {
    try {
      await ElMessageBox.confirm(
        `确定要删除 "${spine.title}" 吗？`,
        '删除书籍',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      );

      await deleteBook(spine.id)

      // 从本地列表移除
      const index = spineList.value.findIndex(item => item.id === spine.id);
      if (index > -1) {
        spineList.value.splice(index, 1);
      }

      if (currentBookId === spine.id) {
        changeBook('-1')
      }
      ElMessage.success('删除成功');
    } catch (error) {
      if (error !== 'cancel') {
        console.error('删除失败:', error);
        ElMessage.error('删除失败');
      }
    }
  }

  // 处理书籍操作命令
  const handleCommand = async (command, spine) => {
    switch (command) {
      case 'edit':
        changeBook(spine.id);
        break;
      case 'rename':
        await handleRename(spine);
        break;
      case 'delete':
        await handleDelete(spine);
        break;
    }
  };

  const handleCreate = async () => {
    try {
      const { value } = await ElMessageBox.prompt(
        '请输入书名',
        '新建书籍',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /\S+/,
          inputErrorMessage: '书名不能为空'
        }
      );

      const book = await createBook(value)
      spineList.value.push(
        {
          id: book.id,
          title: book.title
        }
      );

      changeBook(book.id)
      ElMessage.success('创建成功');
    } catch (error) {
      if (error !== 'cancel') {
        console.error('创建失败:', error);
        ElMessage.error('创建失败');
      }
    }
  }

  // 组件挂载时获取书架列表
  onMounted(() => {
    loadSpines();
  });


</script>

<template>
  <!-- 书架折叠菜单 -->
  <el-collapse v-model="activeNames" class="shelf-collapse">
    <el-collapse-item name="spines" title="我的书架" class="shelf-item">

      <!-- 书籍列表 -->
      <div class="spines-list">
        <div
          v-for="spine in spineList"
          :key="spine.id"
          class="spine-item"
          :class="{ 'selected': currentBookId === spine.id }"
          @click="changeBook(spine.id)"
        >
          <div class="book-name">{{ spine.title }}</div>
          <el-dropdown
            @command="handleCommand($event, spine)"
            class="book-options"
          >
              <span class="el-dropdown-link">
                <el-icon><MoreFilled /></el-icon>
              </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="edit">编辑</el-dropdown-item>
                <el-dropdown-item command="rename">重命名</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <!-- 新建书籍按钮 -->
      <div class="new-book-btn-container">
        <el-button
          type="primary"
          size="default"
          @click="handleCreate"
          class="new-book-btn"
        >
          + 新建书籍
        </el-button>
      </div>
    </el-collapse-item>
  </el-collapse>
</template>

<style scoped>
.shelf-collapse {
  border: none;
}

.shelf-collapse :deep(.el-collapse-item__header) {
  background-color: #f5f7fa;
  border-radius: 8px;
  padding: 0 15px;
  font-weight: 500;
  color: #303133;
}

.shelf-collapse :deep(.el-collapse-item__wrap) {
  border-bottom: none;
}

.shelf-collapse :deep(.el-collapse-item__content) {
  padding: 15px 0;
}

.spines-list {
  max-height: calc(100vh - 220px);
  overflow-y: auto;
  padding: 0 10px;
}

.spine-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  margin-bottom: 8px;
  background-color: #ffffff;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  transition: all 0.3s ease;
  cursor: pointer;
}

.spine-item:hover {
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  transform: translateY(-1px);
}

.spine-item.selected {
  background-color: #ecf5ff;
  border-left: 4px solid #409EFF;
}

.book-name {
  flex: 1;
  font-size: 14px;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 10px;
}

.book-options {
  cursor: pointer;
  color: #909399;
  font-size: 16px;
}

.book-options:hover {
  color: #409EFF;
}

.new-book-btn-container {
  padding: 15px 10px 0;
}

.new-book-btn {
  width: 100%;
  background-color: #409EFF;
  border-color: #409EFF;
}

.new-book-btn:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

:deep(.el-dropdown-menu) {
  min-width: 120px;
}
</style>
