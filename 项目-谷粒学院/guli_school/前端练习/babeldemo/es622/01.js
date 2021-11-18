'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});
//定义方法，设置哪些方法可以被其他js调用
exports.default = {
    getList: function getList() {
        console.log('getList.....');
    },
    update: function update() {
        console.log('update.....');
    }
};