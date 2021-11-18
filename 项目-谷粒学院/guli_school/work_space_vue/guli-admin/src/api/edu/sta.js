import request from '@/utils/request'

export default {
    /**
     * 生成统计数据
     */
     createStatistics(day) {
        return request({
            url: `/staservice/sta/${day}`,
            method: 'post'
        })
    }, 
//获取到统计数据
getShowData(searchObj){
    return request({
        url: `/staservice/sta/showData/${searchObj.type}/${searchObj.begin}/${searchObj.end}`,
        method: 'get'
    })
}

}


