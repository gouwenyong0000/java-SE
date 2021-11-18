import request from '@/utils/request'

export default {

    /**
     * 添加小节信息
     * @param {*} video 
     */
    addVideo(video) {
        return request({
            url: `/eduservice/video/add`,
            method: 'post',
            data: video
        })
    },
    deleteVideo(id) {
        return request({
            url: `/eduservice/video/delete/${id}`,
            method: 'delete'
        })
    },
    getVideoById(id) {
        return request({
            url: `/eduservice/video/${id}`,
            method: 'get'
        })
    },
    updateVideoMethod(video) {
        return request({
            url: `/eduservice/video/update`,
            method: 'put',
            data: video
        });
    },
    //根据视频id删除视频
    deleteAliyunVideo(id) {
        return request({
            url: `/eduvod/video/${id}`,
            method: 'delete',
        })
    },
}



