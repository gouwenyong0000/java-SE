import request from '@/utils/request'
export default {
    
    //1、创建订单
    createOrder(cid) {
        return request({
            url: '/eduorder/order/createOrder/'+cid,
            method: 'post'
        })
    },
    //2、根据id获取订单
    getById(cid) {
        return request({
            url: '/eduorder/order/getOrder/'+cid,
            method: 'get'
        })
    },
    //3、生成微信支付二维码
    createNative(cid) {
        return request({
            url: '/eduorder/payLog/createNative/'+cid,
            method: 'get'
        })
    },
    //4、根据id获取订单支付状态
    queryPayStatus(cid) {
        return request({
            url: '/eduorder/payLog/queryPayStatus/'+cid,
            method: 'get'
        })
    }
}
