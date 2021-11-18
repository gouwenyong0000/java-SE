import request from '@/utils/request'
export default {
  /**
   * 查询热门课程和名师
   * @returns 
   */
  getIndexData() {
    return request({
      url: `/eduservice/index/index`,
      method: 'get'
    })
  }
}
