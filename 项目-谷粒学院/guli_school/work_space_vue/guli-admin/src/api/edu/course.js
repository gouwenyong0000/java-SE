import request from '@/utils/request'

export default {
    /**
     * 添加课程信息
     * @returns 
     */
    addCourseInfo(courseInfo) {
        return request({
            url: `/eduservice/course/addCourseInfo`,
            method: 'post',
            data: courseInfo
        })
    },
    /**
     * 修改课程信息
     * @returns 
     */
    update(courseInfo) {
        return request({
            url: `/eduservice/course/updateCourse`,
            method: 'put',
            data: courseInfo
        })
    },
    /**
     * 查询所有讲师
     * @returns 
     */
    getAll() {
        return request({
            url: `/eduservice/teacher/findAll`,
            method: 'get'

        })
    },
    /**
     * 根据id查询课程详情回显
     * @returns 
     */
    getCourse(id) {
        return request({
            url: `/eduservice/course/${id}`,
            method: 'get'

        })
    },
    //根据条件查询课程信息
    getCoursePage(pageNo,pageSize,query) {
        return request({
            url: `/eduservice/course/query/${pageNo}/${pageSize}`,
            method: 'post',
            data:query

        })
    },
    deleteCourse(id){
        return request({
            url: `/eduservice/course/${id}`,
            method: 'delete'

        })
    }
}

