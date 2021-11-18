import request from '@/utils/request'
export default {
    /**
     * 查询轮播图数据
     * @returns 
     */
  getListBanner() {
    return request({
      url: `/educms/banner/getAllBanner`,
      method: 'get'
    })
  }
}
