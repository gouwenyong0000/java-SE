import request from '@/utils/request'

export default {
  getPageList(page, limit) {   
    return request({
      url: `/eduservice/teacherfront/getTacherFrontList/${page}/${limit}`,
      method: 'get'
    })
  },
  //查询讲师详情
  getById(teacherId) {
    return request({
        url: `/eduservice/teacherfront/teacherInfo/${teacherId}`,
        method: 'get'
    })
}

}
