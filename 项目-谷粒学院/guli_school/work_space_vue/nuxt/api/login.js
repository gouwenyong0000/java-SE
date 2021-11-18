import request from '@/utils/request'

export default {

    /**
     * 注册用户
     * @returns 
     */
  login(user) {
    return request({
      url: `/educenter/member/login`,
      method: 'post',
      data:user
    })
  },
  getUserInfoByToken(){
    return request({
      url: `/educenter/member/auth/getLoginInfo`,
      method: 'get',
    })
  }
  
}