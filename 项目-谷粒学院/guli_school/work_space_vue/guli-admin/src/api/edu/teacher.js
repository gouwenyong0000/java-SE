import request from '@/utils/request'

export default {
    /**
     * 讲师列表（条件分页讲师列表）
     * current:当前页
     * limit：每页数量
     * teacherQuery：查询条件
     */
    getTacherListPage(current, limit, teacherQuery) {
        return request({
            url: `/eduservice/teacher/pageTeacherCondition/${current}/${limit}`,
            method: 'post',
            data:teacherQuery//把对象转换成json进行传递到后台
        })

    },
    /**
     * 根据id删除讲师
     * @param {*} id 
     * @returns 
     */
    deleteTeacher(id){

        return request({
            url: `/eduservice/teacher/${id}`,
            method: 'delete',
          
        })

    },
    /**
     * 保存讲师
     * @param {teacher对象} teacher 
     * @returns 
     */
    saveTeacher(teacher){
        return request({
            url: `/eduservice/teacher/addTeacher`,
            method: 'post',
            data:teacher
        })
    },
    getTeacherInfo(id){
        return request({
            url: `/eduservice/teacher/getTeacher/${id}`,
            method: 'get'
        })
    },
    updateTeacher(teacher){
        return request({
            url: `/eduservice/teacher/updateTeacher`,
            method: 'post',
            data:teacher
        })
    },
   



}

