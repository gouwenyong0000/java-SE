import request from '@/utils/request'

export default {

    /**
     * 注册用户
     * @returns 
     */
  registerUser(formItem) {
    return request({
      url: `/educenter/member/register`,
      method: 'post',
      data:formItem
    })
  },
  /**获取验证码*/
  getCode(phone){
    return request({
        url: `/msm/api/send/${phone}`,
        method: 'get'
      })
  }
}