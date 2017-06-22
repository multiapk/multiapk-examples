###案例
https://github.com/multiapk/multiapk-examples/tree/atlas/android

###其它建议
1:推荐使用 RXJAVA，使用其生命周期管理，很好的解决异步导致的内存问题
2:数据库使用 GREENDAO + RXJAVA 的形式
3:网络使用 RETROFIT + RXJAVA 的形式
4:toast与视觉协商使用snackbar titlebar 与视觉协商使用 ToolBar,每个按钮都有自己的点触范围与效果，也可以有联动效果，自定义的很麻烦且效果不好
5:最小侵入原则，父类尽量不要有逻辑，达到较好的自由度，简单的逻辑可以直接在 activity 里写，复杂的使用 谷歌推荐的基于rxjava的 mvp简便模式
6:主题与activity统一使用AppCompatActivity
7:变量名由于使用kotlin,layout与kt里面的变量名其实一直，个人主张不要+m前缀
8:删除 eventbus,rxjava本身可以实现
9:与视觉协商最大化使用vectorDrawable,让视觉提供svg or 与视觉协商最大化使用vectorDrawable
10:推荐使用单Actiity+单Fragment形式，则只需要写fragment逻辑，而使用同一的DefaultActivity(launchModel=default)加载，减少 manifist里面 activity的定义数量
11:view的点击事件可以使用rxbinding,而非doublecheck
12:模块间应最大化独立，方便以后组件化与不同项目之间的直接引用，这里个人推荐 atlas,经过实测，兼容kotlin,    
   如果实现了组件化，那么项目以后无论多大，多复杂，都能游刃有余，热更新，远程加载模块等，唯一不变的是冷启动，需要再次启动才能应用变动
13:简化的mvp,与简化的基础类库,与组件化的实现，项目基本轻松应对大多数问题
14:内存监控前期简要使用leakcanary
15:图片库新项目个人推荐 fresco, 最好与 retrofit 统一线程池，控制资源
16:注意大小图浏览模式的开关
17:个人推荐仿ios侧滑返回上一个页面的效果，非常好用
18:mvp 数据库+网络层放在 m    activity/fragment只负责 界面显示，p负责业务逻辑，统一后以后我们的代码基本只要看 presenter就能很好的阅读他人代码，这里一些抽象方法可以一起协商下
19:尽量全部使用recyclerview,多使用design包里面的view
20:最好与视觉协商 整个app 一共有多少种 titlebar样式，多少种 弹框样式，统一起来，而不是每个周期都有新的，强制使用以前的，避免项目变大，资源重复又冗余
21:jackson可以很好的支持kotlin,且retrofit自带适配器，fastjson次之
22:guawa 库 + rxjava 方法数很有可能导致第一个dex数量过65535，斟酌用之
23:推送+埋点+crash日志追踪(bugly)
24:优化页面之间跳转事件
25:部分页面可以使用缓存，再网络刷新之
26:多渠道打包使用walle,可以很好的与kotlin/atlas兼容

###建议目标:
23:逐渐标准化，可以尝试自己实现服务端热更新与远程模块加载，很方便的适用其它任何规模的项目，可以尝试放到github(基础类库与简要框架模式)
24:浏览器跳转与hybird内部跳转可以统一入口，包括Notication跳转，需要一个全局初始化的地方，
25:hybird 与 webview 可以自己实现(android/ios/html5)一个轻便易用的接口框架，可以标准化，轻便化
26:以上做的差不多后，可以尝试 react-native 与 ios 统一化
27:组件化的推广可以新建分支，当前代码要全部转换还是有些耗时的

###建议趋势:
1:组件化，标准化
2:kotlin/rxjava 推广
