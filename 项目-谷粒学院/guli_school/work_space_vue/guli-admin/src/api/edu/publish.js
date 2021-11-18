import request from '@/utils/request'

export default {
    /**
     * 发布页面确认信息
     */
    getPublishInfo(id) {
        return request({
            url: `/eduservice/course/getPublishInfo/${id}`,
            method: 'get'
        })
    }, 
    /**
     * 发布页面确认信息
     */
    publishCourse(id) {
        return request({
            url: `/eduservice/course/publish/${id}`,
            method: 'post'
        })
    },
    



}

