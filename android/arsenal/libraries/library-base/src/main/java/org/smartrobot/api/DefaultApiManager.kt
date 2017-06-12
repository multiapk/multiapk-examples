package org.smartrobot.api


open class DefaultApiManager {
    /*static
    {
        MDebugFragment.addUrl(new MDebugFragment . UrlEntity "主服务", baseUrl, true), new MDebugFragment.UrlEntity("老贺本机", heJinGuoUrl, false));
    }*/

    companion object {
        private val baseUrl = "http://api.jiduojia.com/jdhome-server/appservice/"
        private val heJinGuoUrl = "http://hejinguo1987.oicp.net/jdhome-server/appservice/"
        private val retrofitClient = DefaultRetrofitClient()

        @Synchronized fun getService(): DefaultApi {
            return retrofitClient.getApi(baseUrl, DefaultApi::class.java)
        }
    }

}