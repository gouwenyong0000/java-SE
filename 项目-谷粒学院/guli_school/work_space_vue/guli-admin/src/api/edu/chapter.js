import request from '@/utils/request'

export default {
    /**
     * 获取章节信息
     * @returns 
     */
    getAllChapterVideo(courseId) {
        return request({
            url: `/eduservice/chapter/getChapterVideo/${courseId}`,
            method: 'get'
        })
    },
    /**
     * 添加章节
     * @param {*} chapter 
     * @returns 
     */
    addChapter(chapter) {
        return request({
            url: `/eduservice/chapter/add`,
            method: 'post',
            data: chapter
        })
    },
    /**
     * 修改章节信息
     * @param {*} chapter 
     * @returns 
     */
    updateChapter(chapter) {
        return request({
            url: `/eduservice/chapter/update`,
            method: 'post',
            data: chapter
        })
    },
    /**
     * 删除章节
     * @param {*} id 
     * @returns 
     */
    deleteChapter(id) {
        return request({
            url: `/eduservice/chapter/delete/${id}`,
            method: 'post',
        })
    },
    /**
     * 根据id查询章节信息
     * @param {*} id 
     * @returns 
     */
    queryChapter(id) {
        return request({
            url: `/eduservice/chapter/getChapter/${id}`,
            method: 'get',
        })
    },
    
}

