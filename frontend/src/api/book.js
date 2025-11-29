import request, {unwrapResponse} from "@/util/request.js";

export const createBook = async (title) => {
  try {
    const response = await request.post(`/book/create/${title}`)
    return unwrapResponse(response)
  } catch (error) {
    throw new Error(error.response?.data?.message || '创建失败')
  }
}

export const deleteBook = async (bookId) => {
  try {
    const response = await request.delete(`/book/delete/${bookId}`)
    return unwrapResponse(response)
  } catch (error) {
    throw new Error(error.response?.data?.message || '删除失败')
  }
}

export const renameBook = async (bookId, title) => {
  try {
    const response = await request.post(`/book/rename/${bookId}/${title}`)
    return unwrapResponse(response)
  } catch (error) {
    throw new Error(error.response?.data?.message || '重命名失败')
  }
}

export const querySpines = async () => {
  try {
    const response = await request.get('/book/spines')
    return unwrapResponse(response)
  } catch (error) {
    throw new Error(error.response?.data?.message || '查询书籍集合失败')
  }
}

