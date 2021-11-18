import request from '@/utils/request'

export default {
    /**
     * 获取课程分类树结构
     */
    getTree() {
        return request({
            url: `/eduservice/subject/tree`,
            method: 'get'
        })
    },
 



}

