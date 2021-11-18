import request from '@/utils/request'
export default {
    getPageList(page, limit, searchObj) {
        return request({
            url: `/eduservice/coursefront/${page}/${limit}`,
            method: 'post',
            data: searchObj
        })
    },
    // 获取课程二级分类
    getAllSubject() {
        return request({
            url: `/eduservice/subject/tree`,
            method: 'get'
        })
    },
    getById(courseId) {
        return request({
            url: `/eduservice/coursefront/course/${courseId}`,
            method: 'get'
        })
    },


}

