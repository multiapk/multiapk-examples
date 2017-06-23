### 案例
https://github.com/multiapk/multiapk-examples/tree/atlas/android

### 建议趋势:
1. 组件化(atlas)，标准化基础类库(library-base)
2. kotlin/rxjava

### 建议目标:
1. android 新建分支,逐渐组件化
2. android 标准化基础类库/与之匹配的独立热更新CMS
3. android ios html5 一套统一的 hybird 接口框架


### 其它建议
1. 统一初始化入口(hybird/外部跳转/notification)
2. 统一线程池分配(glide/fresco/retrofit)
3. 最后的最后可以尝试 react-native 实现与 ios 统一化
4. 数据库 GREENDAO + RXJAVA
5. 网络 OKHTTP + RETROFIT + RXJAVA 的形式
6. Toast 推荐使用 Snackbar
7. Titlebar 推荐使用 ToolBar(点触范围与/联动效果)
8. Titlebar/Dialog 所有样式统一化,与视觉设计师协商复用
9. 最大化使用 VectorDrawable
10. View 的双击检测等使用 rxbingding
11. 删除EventBus, RxJava可以自己实现
12. 统一使用 AppCompatActivity
13. 可以加上侧滑返回上一个页面效果
14. jackson可以很好的支持kotlin,且retrofit自带适配器，fastjson次之
15. 多渠道打包使用walle,可以很好的与kotlin/atlas兼容
16. 使用单Activity+单Fragment形式,则只需要写fragment逻辑,而使用同一个DefaultActivity(launchModel=default)加载,减少 AndroidManifest.xml 里面 activity的定义数量
17. 推送+埋点+crash

### 变量规范
1. 变量名由于使用kotlin,layout与kt里面的变量名其实一直，个人主张不要+m前缀
2. 最小侵入原则，Activity/Fragment 父类尽量不要有逻辑，达到较好的自由度，简单的逻辑可以直接在 activity 里写，复杂的使用 谷歌推荐的基于rxjava的 mvp简便模式
3. mvp 数据库+网络层放在 model 层
      activity/fragment只负责界面显示，
      presenter负责业务逻辑
      这里一些抽象方法可以一起协商下
