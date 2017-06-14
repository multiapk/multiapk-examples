package business.smartrobot.api

import com.alibaba.fastjson.JSON

class DefaultResponseModel<T>() {
    var version: Int = 0//协议版本号
    var category: Int = 0//协议分类
    var platform: Int = 0//1:ios 2:android
    var status: Int = 0//状态 0:成功 1:失败 2:未登录
    var message = ""//响应描述
    var data: T? = null

    override fun toString(): String {
        return JSON.toJSONString(this, true)
    }
}