# 车享开发规范-ANDROID

##目录

### 1 AndroidStudio

1. 编码格式统一为**UTF-8**；
2. 编辑完`.java`、`.kt`、 `.xml`等文件后一定要**格式化**，每次提交可以只格式化自己写的那部分代码（配置文件后续提供）
3. 删除多余的import，减少警告出现，可利用AS的`Optimize Imports`；

### 2 Java/Kt 代码命名规范

代码中的命名严禁使用拼音与英文混合的方式,更不允许直接使用中文的方式。正确的英文拼写和语法可以让阅读者易于理解,避免歧义。

> 注意：即使纯拼音命名方式也要避免采用。但`alibaba`、`taobao`、`youku`、`hangzhou`等国际通用的名称,可视同英文。

#### 2.1 包名

包名全部小写，连续的单词只是简单地连接起来，不使用下划线，采用反域名命名规则，全部使用小写字母。一级包名是顶级域名，通常为`com`,`edu`,`gov`,`net`,`org`等，二级包名为公司名，三级包名根据应用进行命名，后面就是对包名的划分了，关于包名的划分，推荐使用按功能分，避免按照层区分。可以参考～**[Package by features, not layers][Package by features, not layers]**，～**[iosched][iosched]**。

#### 3.2 类名

类名都以`UpperCamelCase`风格编写，通常是名词或名词短语，接口名称有时可能是形容词或形容词短语。

名词，采用大驼峰命名法，尽量避免缩写，除非该缩写是众所周知的， 比如`HTML`, `URL`，如果类名称中包含单词缩写，则单词缩写的每个字母均应大写。

| 类                  | 描述                                       | 例如                                       |
| :----------------- | :--------------------------------------- | :--------------------------------------- |
| Activity 类         | Activity为后缀标识                            | 欢迎页面类WelcomeActivity                     |
| Adapter类           | Adapter 为后缀标识                            | 新闻详情适配器 NewDetailAdapter                 |
| 解析类                | Parser为后缀标识                              | 首页解析类HomePosterParser                    |
| 工具方法类              | Utils或Manager为后缀标识（与系统或第三方的Utils区分）或功能+Utils | 线程池管理类：ThreadPoolManager日志工具类：LogUtils（Logger也可）打印工具类：PrinterUtils |
| 数据库类               | 以DBHelper后缀标识                            | 新闻数据库：NewDBHelper                        |
| Service类           | 以Service为后缀标识                            | 时间服务TimeService                          |
| BroadcastReceiver类 | 以Receiver为后缀标识                           | 推送接收JPushReceiver                        |
| ContentProvider类   | 以Provider为后缀标识                           | ShareProvider                            |
| 自定义的共享基础类          | 以Base开头                                  | BaseActivity,BaseFragment                |

测试类的命名以它要测试的类的名称开始，以Test结束。例如：`HashTest`或`HashIntegrationTest`。

接口（interface）：命名规则与类一样采用大驼峰命名法，多以able或ible结尾，如
`interface Runnable`、`interface Accessible`。


#### 3.3 方法名

方法名都以`lowerCamelCase`风格编写，通常是动词或动词短语。

#### 3.4 常量名

常量名命名模式为`CONSTANT_CASE`，全部字母大写，用下划线分隔单词。那，到底什么算是一个常量？

每个常量都是一个静态`final`字段，但不是所有静态`final`字段都是常量。在决定一个字段是否是一个常量时，考虑它是否真的感觉像是一个常量。例如，如果任何一个该实例的观测状态是可变的，则它几乎肯定不会是一个常量。只是永远不打算改变对象一般是不够的，它要真的一直不变才能将它示为常量。

```
java
// Constants
static final int NUMBER = 5;
static final ImmutableListNAMES = ImmutableList.of("Ed", "Ann");
static final Joiner COMMA_JOINER = Joiner.on(','); // because Joiner is immutable
static final SomeMutableType[] EMPTY_ARRAY = {};
enum SomeEnum { ENUM_CONSTANT }

// Not constants
static String nonFinal = "non-final";
final String nonStatic = "non-static";
static final Set mutableCollection = new HashSet();
static final ImmutableSet mutableElements = ImmutableSet.of(mutable);
static final Logger logger = Logger.getLogger(MyClass.getName());
static final String[] nonEmptyArray = {"these", "can", "change"};
```

Android中经常使用键值对(key-value)，如SharedPreferences, Bundle, 以及 Intent，所以必然会时常书写String 常量，这时必须遵循下面的建议：

| 常量用途  | 命名前缀 |
| :--------| ------- |
| SharedPreferences | PREF_ |
| Bundle | BUNDLE_ |
| Fragment Arguments | ARGS_ |
| Intent Extra | EXTRA_ |
| Intent Action | ACTION_ |

示例：

```
java
// 字符串常量值的大小写不定，最好统一为小写
static final String PREF_EMAIL = "pref_key_email";
static final String BUNDLE_AGE = "bundle_age";
static final String ARGS_USER_ID = "args_user_id";

// Intent相关的必须以包名开头
static final String EXTRA_SURNAME = "com.google.EXTRA_SURNAME";
static final String ACTION_OPEN_USER = "com.gogole.action.ACTION_OPEN_USER";
```

#### 3.5 非常量字段名

非常量字段名以`lowerCamelCase`风格的基础上改造为如下风格：基本结构为`scopeVariableNameType`。

**scope：范围**

非公有，非静态字段命名以m开头。

静态字段命名以s开头。

公有非静态字段命名以p开头。

其他字段名以小写字母开头

公开的常量(static final)并须遵从大写字母下划线隔开 ALL_CAPS_WITH_UNDERSCORES

例子：

```
java
public class MyClass {
    public static final int SOME_CONSTANT = 42;
    public int publicField;
    private static MyClass sSingleton;
    int mPackagePrivate;
    private int mPrivate;
    protected int mProtected;
}
```

使用1字符前缀来表示作用范围，1个字符的前缀必须小写，前缀后面是由表意性强的一个单词或多个单词组成的名字，而且每个单词的首写字母大写，其它字母小写，这样保证了对变量名能够进行正确的断句。

**Type：类型**

考虑到Android中使用很多UI控件，为避免控件和普通成员变量混淆以及更好达意，所有用来表示控件的成员变量统一加上控件缩写作为后缀（文末附有缩写表）。

对于普通变量一般不添加类型后缀，如果统一添加类型后缀，请参考文末的缩写表。

用统一的量词通过在结尾处放置一个量词，就可创建更加统一的变量，它们更容易理解，也更容易搜索。

> 注意：如果项目中使用`ButterKnife`，则不添加m前缀，以`lowerCamelCase`风格命名。

例如，请使用`mCustomerStrFirst`和`mCustomerStrLast`，而不要使用`mFirstCustomerStr`和`mLastCustomerStr`。

| 量词列表  | 量词后缀说明      |
| ----- | ----------- |
| First | 一组变量中的第一个   |
| Last  | 一组变量中的最后一个  |
| Next  | 一组变量中的下一个变量 |
| Prev  | 一组变量中的上一个   |
| Cur   | 一组变量中的当前变量  |

说明：

集合添加如下后缀：List、Map、Set
数组添加如下后缀：Arr

> 注意：所有的VO（值对象）统一采用标准的lowerCamelCase风格编写，所有的DTO（数据传输对象）就按照接口文档中定义的字段名编写。


#### 3.6 参数名

参数名以`lowerCamelCase`风格编写。
参数应该避免用单个字符命名。


#### 3.7 局部变量名

局部变量名以`lowerCamelCase`风格编写，比起其它类型的名称，局部变量名可以有更为宽松的缩写。

虽然缩写更宽松，但还是要避免用单字符进行命名，除了临时变量和循环变量。

即使局部变量是final和不可改变的，也不应该把它示为常量，自然也不能用常量的规则去命名它。


#### 3.8 临时变量

临时变量通常被取名为`i`、`j`、`k`、`m`和`n`，它们一般用于整型；`c`、`d`、`e`，它们一般用于字符型。 如：`for (int i = 0; i < len ; i++)`。


#### 3.9 类型变量名

类型变量可用以下两种风格之一进行命名：

1. 单个的大写字母，后面可以跟一个数字(如：`E`, `T`, `X`, `T2`)。
2. 以类命名方式(参考[3.2 类名](#32-类名))，后面加个大写的`T`(如：`RequestT`, `FooBarT`)。


更多还可参考～**[阿里巴巴Java开发手册][阿里巴巴Java开发手册]**

### 4 资源文件规范

所有资源文件通用命名规则：全部小写，采用下划线命名法（abc_def）。另外特别的是不允许出现用拼音来命名的资源。为防止不同业务间资源文件命名冲突，除通用控件外，注意：在业务module中的除了覆盖用的其他所有的内部使用的资源文件必须以业务线缩写开头（cxj_ic_edit.png）

#### 4.1 资源布局文件（XML文件（layout布局文件））

##### 4.1.1 contentView命名

必须以全部单词小写，单词间以下划线分割，使用名词或名词词组。

所有Activity或Fragment的contentView必须与其类名对应，对应规则为：将所有字母都转为小写，将类型和功能调换（也就是后缀变前缀）。

例如：`activity_main.xml`


##### 4.1.2 Dialog命名

规则：`dialog_描述.xml`

例如：`dialog_hint.xml`


##### 4.1.3 PopupWindow命名

规则：`ppw_描述.xml`

例如：`ppw_info.xml`


##### 4.1.4 列表项命名

规则：`item_描述.xml`

例如：`item_city.xml`


##### 4.1.5 包含项命名

规则：`模块_(位置)描述.xml`

例如：`activity_main_head.xml`、`activity_main_bottom.xml`

注意：通用的包含项命名采用：`项目名称缩写_描述.xml`

例如：`xxxx_title.xml`

#### 4.1.6 不完整的布局命名

如被<include>标签包含或者ViewStub inflate 到另外的布局中的布局文件则以layout_开头。

#### 4.1.7 用Xml布局的自定义控件

inflate用的布局文件以widget_开头。

#### 4.2 资源文件（图片drawable文件夹下）

全部小写，采用下划线命名法，加前缀区分

命名模式：可加后缀 `_small` 表示小图， `_big` 表示大图，逻辑名称可由多个单词加下划线组成，采用以下规则：

* `用途_模块名_逻辑名称`
* `用途_模块名_颜色`
* `用途_逻辑名称`
* `用途_颜色`

说明：用途也指控件类型（具体见附录[UI控件缩写表](#ui控件缩写表)）

例如：

| 名称                      | 说明                      |
| ----------------------- | ----------------------- |
| divider_maket_white.png | 分割线`用途_模块名_颜色`          |
| ic_edit.png             | 图标`用途_逻辑名称`             |
| bg_main.png             | 背景`用途_逻辑名称`             |
| ic_head_small.png       | 小头像`用途_逻辑名称`            |
| bg_input.png            | 输入框背景`用途_逻辑名称`          |
| divider_white.png       | 白色分割线`用途_颜色`            |
| bg_main_head            | 主模块头部背景图片`用途_模块名_逻辑名称`  |
| ic_more_help            | 更多帮助图标`用途_逻辑名称`         |
| divider_list_line       | 列表分割线`用途_逻辑名称`          |
| shape_music_ring        | 音乐界面环形形状`用途_模块名_逻辑名称`   |

如果有多种形态，如按钮选择器：`xx_selector.xml`

| 名称                   | 说明                           |
| -------------------- | ---------------------------- |
| xx_selector      | 按钮图片使用`整体效果`（selector）   |
| xx_normal        | 按钮图片使用`正常情况效果`           |
| xx_pressed       | 按钮图片使用`点击时候效果`           |
| xx_focused       | `state_focused`聚焦效果          |
| xx_disabled      | `state_enabled` (false)不可用效果 |
| xx_checked       | `state_checked`选中效果          |
| xx_selected      | `state_selected`选中效果         |
| xx_hovered       | `state_hovered`悬停效果          |
| xx_checkable     | `state_checkable`可选效果        |
| xx_activated     | `state_activated`激活的         |
| xx_windowfocused | `state_window_focused`       |

#### 4.3 动画文件（anim文件夹下）

全部小写，采用下划线命名法，加前缀区分。

具体动画采用以下规则：`模块名_逻辑名称`。

例如：`refresh_progress.xml`、`market_cart_add.xml`、`market_cart_remove.xml`。

普通的tween动画采用如下表格中的命名方式：`动画类型_方向`

| 名称                | 说明      |
| ----------------- | ------- |
| fade_in           | 淡入      |
| fade_out          | 淡出      |
| push_down_in      | 从下方推入   |
| push_down_out     | 从下方推出   |
| push_left         | 推向左方    |
| slide_in_from_top | 从头部滑动进入 |
| zoom_enter        | 变形进入    |
| slide_in          | 滑动进入    |
| shrink_to_middle  | 中间缩小    |


#### 4.4 values中name命名

`values`文件夹中的文件必须是复数的，如`strings.xml`, `styles.xml`, `colors.xml`等。

##### 4.4.1 colors.xml

`colors.xml`的`name`命名使用下划线命名法，在你的`colors.xml`文件中应该只是映射颜色的名称一个ARGB值，而没有其它的。不要使用它为不同的按钮来定义ARGB值。

**不要这样做**

```
xml
  <resources>
      <color name="button_foreground">#FFFFFF</color>
      <color name="button_background">#2A91BD</color>
      <color name="comment_background_inactive">#5F5F5F</color>
      <color name="comment_background_active">#939393</color>
      <color name="comment_foreground">#FFFFFF</color>
      <color name="comment_foreground_important">#FF9D2F</color>
      ...
      <color name="comment_shadow">#323232</color>
```

使用这种格式，你会非常容易的开始重复定义ARGB值，这使如果需要改变基本色变的很复杂。同时，这些定义是跟一些环境关联起来的，如`button`或者`comment`, 应该放到一个按钮风格中，而不是在`colors.xml`文件中。

**相反，这样做**

```
xml
  <resources>

      <!-- grayscale -->
      <color name="white"     >#FFFFFF</color>
      <color name="gray_light">#DBDBDB</color>
      <color name="gray"      >#939393</color>
      <color name="gray_dark" >#5F5F5F</color>
      <color name="black"     >#323232</color>

      <!-- basic colors -->
      <color name="green">#27D34D</color>
      <color name="blue">#2A91BD</color>
      <color name="orange">#FF9D2F</color>
      <color name="red">#FF432F</color>

  </resources>
```

向应用设计者那里要这个调色板，名称不需要跟`"green"`、`"blue"`等等相同。`"brand_primary"`、`"brand_secondary"`、`"brand_negative"`这样的名字也是完全可以接受的。 像这样规范的颜色很容易修改或重构，会使应用一共使用了多少种不同的颜色变得非常清晰。 通常一个具有审美价值的UI来说，减少使用颜色的种类是非常重要的。

> 注意：如果某些颜色和主题有关，那就单独写一个`colors_theme.xml`。


##### 4.4.2 dimens.xml

像对待`colors.xml`一样对待`dimens.xml`文件 与定义颜色调色板一样，你同时也应该定义一个空隙间隔和字体大小的“调色板”。 一个好的例子，如下所示：

```
xml
<resources>

    <!-- font sizes -->
    <dimen name="font_larger">22sp</dimen>
    <dimen name="font_large">18sp</dimen>
    <dimen name="font_normal">15sp</dimen>
    <dimen name="font_small">12sp</dimen>

    <!-- typical spacing between two views -->
    <dimen name="spacing_huge">40dp</dimen>
    <dimen name="spacing_large">24dp</dimen>
    <dimen name="spacing_normal">14dp</dimen>
    <dimen name="spacing_small">10dp</dimen>
    <dimen name="spacing_tiny">4dp</dimen>

    <!-- typical sizes of views -->
    <dimen name="button_height_tall">60dp</dimen>
    <dimen name="button_height_normal">40dp</dimen>
    <dimen name="button_height_short">32dp</dimen>

</resources>

```

布局时在写`margins`和`paddings`时，你应该使用`spacing_xxxx`尺寸格式来布局，而不是像对待`string`字符串一样直接写值。 这样写会非常有感觉，会使组织和改变风格或布局是非常容易。


##### 4.4.3 strings.xml

`strings`的`name`命名使用下划线命名法，采用以下规则：`模块名+逻辑名称`，这样方便同一个界面的所有string都放到一起，方便查找。

| 名称                | 说明      |
| ----------------- | ------- |
| main_menu_about   | 主菜单按键文字 |
| friend_title      | 好友模块标题栏 |
| friend_dialog_del | 好友删除提示  |
| login_check_email | 登录验证    |
| dialog_title      | 弹出框标题   |
| button_ok         | 确认键     |
| loading           | 加载文字    |


##### 4.4.4 styles.xml

`style`的`name`命名使用大驼峰命名法，几乎每个项目都需要适当的使用`style`文件，因为对于一个视图来说有一个重复的外观是很常见的，将所有的外观细节属性（`colors`、`padding`、`font`）放在`style`文件中。
 在应用中对于大多数文本内容，最起码你应该有一个通用的`style`文件。
 * TextAppearance 必须以`TextAppearance.App`. 开头，例如：`TextAppearance.App.Large`，`TextAppearance.App.Tab.Title`等。
 * Widget 必须以`Widget.App.WidgetName`. 开头，例如：`Widget.App.ImageView.Scalable`，`Widget.App.Button.Submit`等。
 * Dialog 必须以`Dialog.App` 开头，例如：`Dialog.App.Animation.Drop`，`Dialog.App.Animation.Out`等。
 * Custom Widget 必须以`Widget.App.CustomWigdetName`. 开头，例如：`Widget.App.Divider`，`Widget.App.GroupFloatingButton`等。例如：

```
<style name=Widget.App.TextView.ContentText">
    <item name="android:textSize">@dimen/font_normal</item>
    <item name="android:textColor">@color/basic_black</item>
</style>
```

应用到`TextView`中:

```
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/price"
    style="@style/Widget.App.TextView.ContentText"
    />    
```

你或许需要为按钮控件做同样的事情，不要停止在那里。将一组相关的和重复`android:****`的属性放到一个通用的`style`中。

**将一个大的`styles.xml`文件分割成多个文件**， 你可以有多个`styles.xml` 文件。Android SDK支持其它文件，`styles.xml`这个文件名称并没有作用，起作用的是在文件里的`<style>`标签。因此你可以有多个style文件，如`styles.xml`、`styles_home.xml`、`styles_item_details.xml`、`styles_forms.xml`。 不同于资源文件路径需要为系统构建起的有意义，在`res/values`目录下的文件可以任意命名。

##### 4.4.5 themes.xml

* Activity和Widget Theme必须以`AppTheme.`开头，例如：`AppTheme.Splash`，`AppTheme.NavigationView`等。

* Dialog Theme必须以`AppTheme.Dialog.` 开头，例如：`AppTheme.Dialog.Translucent`，`AppTheme.Dialog.SearchDialog`等。

#### 4.5 layout中的id命名

命名模式为：`view缩写_模块名_逻辑名`，比如`main_search`

如果想对资源文件进行分包可以参考这篇文章～**[Android Studio下对资源进行分包][Android Studio下对资源进行分包]**

#### 4.6 menu中的命名

菜单文件的命名必须与使用它的Activity类名对应。如`MainActivity`： `main.xml`。

#### 4.7 assets中的命名

`assets`目录中的资源按用途分目录，并遵循小写下划线规则。

### 5 注释规范

为了减少他人阅读你代码的痛苦值，请在关键地方做好注释。

#### 5.1 类注释

每个类完成后应该有作者姓名和联系方式的注释，对自己的代码负责。

```
java
/**
 * <pre>
 *     author : Blankj
 *     e-mail : xxx@xx
 *     time   : 2017/03/07
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */
public class WelcomeActivity {
       ...
}
```

具体可以在AS中自己配制，Settings → Editor → File and Code Templates → Includes → File Header，输入

```java
/**
 * <pre>
 *     author : ${USER}
 *     e-mail : xxx@xx
 *     time   : ${YEAR}/${MONTH}/${DAY}
 *     desc   :
 *     version: 1.0
 * </pre>
 */
```

这样便可在每次新建类的时候自动加上该头注释。


#### 5.2 方法注释

每一个成员方法（包括自定义成员方法、覆盖方法、属性方法）的方法头都必须做方法头注释，在方法前一行输入`/** + 回车`或者设置`Fix doc comment`(Settings → Keymap → Fix doc comment)快捷键，AS便会帮你生成模板，我们只需要补全参数即可，如下所示。

```java
/**
 * bitmap转byteArr
 *
 * @param bitmap bitmap对象
 * @param format 格式
 * @return 字节数组
 */
public static byte[] bitmap2Bytes(Bitmap bitmap, CompressFormat format) {
    if (bitmap == null) return null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(format, 100, baos);
    return baos.toByteArray();
}
```

#### 5.3 块注释

块注释与其周围的代码在同一缩进级别。它们可以是`/* ... */`风格，也可以是`// ...`风格(**`//`后最好带一个空格**）。对于多行的`/* ... */`注释，后续行必须从`*`开始， 并且与前一行的`*`对齐。以下示例注释都是OK的。

```java
/*
 * This is          // And so           /* Or you can
 * okay.            // is this.          * even do this. */
 */
```

注释不要封闭在由星号或其它字符绘制的框架里。

> Tip：在写多行注释时，如果你希望在必要时能重新换行(即注释像段落风格一样)，那么使用`/* ... */`。

#### 5.4 其他一些注释

AS已帮你集成了一些注释模板，我们只需要直接使用即可，在代码中输入`todo`、`fixme`等这些注释模板，回车后便会出现如下注释

``` java
// TODO: 17/3/14 需要实现，但目前还未实现的功能的说明
// FIXME: 17/3/14 需要修正，甚至代码是错误的，不能工作，需要修复的说明
```

### 6 Java编码规范

#### 6.1 Java 语言规则

Java语言书写规则主要脱胎于Android官方的规范：[AOSP Code Style for Contributors][AOSP Code Style for Contributors]

##### 6.1.1 不要忽略异常

有时，完全忽略异常是非常诱人的，比如：

``` java
void setServerPort(String value) {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) { }
}
```

绝对不要这么做。也许你会认为：你的代码永远不会碰到这种出错的情况，或者处理异常并不重要，可类似上述忽略异常的代码将会在代码中埋下一颗地雷，说不定哪天它就会炸到某个人了。你必须在代码中以某种规矩来处理所有的异常。根据情况的不同，处理的方式也会不一样。

无论何时，空的catch语句都会让人感到不寒而栗。虽然很多情况下确实是一切正常，但至少你不得不去忧虑它。在Java中你无法逃离这种恐惧感。 -James Gosling

可接受的替代方案包括（按照推荐顺序）：

* 向方法的调用者抛出异常。

``` java
void setServerPort(String value) throws NumberFormatException {
    serverPort = Integer.parseInt(value);
}
```

* 根据抽象级别抛出新的异常。

``` java
void setServerPort(String value) throws ConfigurationException {

    try {

        serverPort = Integer.parseInt(value);

    } catch (NumberFormatException e) {

        throw new ConfigurationException("Port " + value + " is not valid.");

    }

}
```

* 默默地处理错误并在catch {}语句块中替换为合适的值。

``` java
void setServerPort(String value) {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        serverPort = 80;  // default port for server
    }
}
```

* 捕获异常并抛出一个新的RuntimeException。这种做法比较危险：只有确信发生该错误时最合适的做法就是崩溃，才会这么做。

``` java
void setServerPort(String value) {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        throw new RuntimeException("port " + value " is invalid, ", e);
    }
}
```

> 注意：最初的异常是传递给构造方法的RuntimeException。如果代码必须在Java 1.3版本下编译，需要忽略该异常。

* 最后一招：如果确信忽略异常比较合适，那就忽略吧，但必须把理想的原因注释出来：

``` java
void setServerPort(String value) {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        // Method is documented to just ignore invalid user input.
        // serverPort will just be unchanged.
    }
}
```

##### 6.1.2 不要捕获顶级的Exception

有时在捕获Exception时偷懒也是很吸引人的，类似如下的处理方式：

``` java
try {
    someComplicatedIOFunction();        // may throw IOException
    someComplicatedParsingFunction();   // may throw ParsingException
    someComplicatedSecurityFunction();  // may throw SecurityException
    // phew, made it all the way
} catch (Exception e) {                 // I'll just catch all exceptions
    handleError();                      // with one generic handler!
}
```

不要这么做。绝大部分情况下，捕获顶级的Exception或Throwable都是不合适的，Throwable更不合适，因为它还包含了Error异常。这种捕获非常危险。这意味着本来不必考虑的Exception（包括类似ClassCastException的RuntimeException）被卷入到应用程序级的错误处理中来。这会让代码运行的错误变得模糊不清。这意味着，假如别人在你调用的代码中加入了新的异常，编译器将无法帮助你识别出各种不同的错误类型。绝大部分情况下，无论如何你都不应该用同一种方式来处理各种不同类型的异常。

本规则也有极少数例外情况：期望捕获所有类型错误的特定的测试代码和顶层代码（为了阻止这些错误在用户界面上显示出来，或者保持批量工作的运行）。这种情况下可以捕获顶级的Exception（或Throwable）并进行相应的错误处理。在开始之前，你应该非常仔细地考虑一下，并在注释中解释清楚为什么这么做是安全的。

比捕获顶级Exception更好的方案：

* 分开捕获每一种异常，在一条try语句后面跟随多个catch 语句块。这样可能会有点别扭，但总比捕获所有Exception要好些。请小心别在catch语句块中重复执行大量的代码。

* 重新组织一下代码，使用多个try块，使错误处理的粒度更细一些。把IO从解析内容的代码中分离出来，根据各自的情况进行单独的错误处理。

* 再次抛出异常。很多时候在你这个级别根本就没必要捕获这个异常，只要让方法抛出该异常即可。

> 注意：异常是你的朋友！当编译器指出你没有捕获某个异常时，请不要皱眉头。而应该微笑：编译器帮助你找到了代码中的运行时（runtime）问题。

##### 6.1.3 不要使用Finalizer

Finalizer提供了一个机会，可以让对象被垃圾回收器回收时执行一些代码。

优点：便于执行清理工作，特别是针对外部资源。

缺点：调用finalizer的时机并不确定，甚至根本就不会调用。

结论：我们不要使用finalizers。大多数情况下，可以用优秀的异常处理代码来执行那些要放入finalizer的工作。如果确实是需要使用finalizer，那就定义一个close()方法（或类似的方法），并且在文档中准确地记录下需要调用该方法的时机。相关例程可以参见InputStream。这种情况下还是适合使用finalizer的，但不需要在finalizer中输出日志信息，因为日志不能因为这个而被撑爆。

##### 6.1.4 使用完全限定Import

当需要使用foo包中的Bar类时，存在两种可能的import方式：

* `import foo.*`;

优点：可能会减少import语句。

* `import foo.Bar`;

优点：实际用到的类一清二楚。代码的可读性更好，便于维护。

结论：用后一种写法来import所有的Android代码。不过导入java标准库`(java.util.*、java.io.*等)`和单元测试代码`(junit.framework.*)`时可以例外。

#### 6.2 Java类库规范

使用Android Java类库和工具存在一些惯例。有时这些惯例会作出重大变化，可之前的代码也许会用到过时的模板或类库。如果用到这部分过时的代码，沿用已有的风格就是了（参阅Consistency)。创建新的组件时就不要再使用过时的类库了。

#### 6.3 Java编程规范

##### 6.3.1 使用Javadoc标准注释

每个类和自建的public方法必须包含Javadoc注释，注释至少要包含描述该类或方法用途的语句。并且该语句应该用第三人称的动词形式来开头。

例如：

```java
/** Returns the correctly rounded positive square root of a double value. */
static double sqrt(double a) {
    ...
}
```

或

```java
/**
 * Constructs a new String by converting the specified array of
 * bytes using the platform's default character encoding.
 */
public String(byte[] bytes) {
    ...
}
```

如果所有的Javadoc都会写成“sets Foo”，对于那些无关紧要的类似setFoo()的get和set语句是不必撰写Javadoc的。如果方法执行了比较复杂的操作（比如执行强制约束或者产生很重要的副作用），那就必须进行注释。如果“Foo”属性的意义不容易理解，也应该进行注释。

无论是public的还是其它类型的，所有自建的方法都将受益于Javadoc。public的方法是API的组成部分，因此更需要Javadoc。

Android目前还没有规定自己的Javadoc注释撰写规范，但是应该遵守Sun Javadoc约定。

##### 6.3.2 编写简短的方法

为了把规模控制在合理范围内，方法应该保持简短和重点突出。不过，有时较长的方法也是合适的，所以对方法的代码长度并没有硬性的限制。如果方法代码超过了40行，就该考虑是否可以在不损害程序结构的前提下进行分拆。

##### 6.3.3 在标准的位置定义字段
字段应该定义在文件开头，或者紧挨着使用这些字段的方法之前。

##### 6.3.4 限制变量的作用范围

局部变量的作用范围应该是限制为最小的（Effective Java第29条）。使用局部变量，可以增加代码的可读性和可维护性，并且降低发生错误的可能性。每个变量都应该在最小范围的代码块中进行声明，该代码块的大小只要能够包含所有对该变量的使用即可。

应该在第一次用到局部变量的地方对其进行声明。几乎所有局部变量声明都应该进行初始化。如果还缺少足够的信息来正确地初始化变量，那就应该推迟声明，直至可以初始化为止。

本规则存在一个例外，就是涉及try-catch语句的情况。如果变量是用方法的返回值来初始化的，而该方法可能会抛出一个checked异常，那么必须在try块中进行变量声明。如果需在try块之外使用该变量，那它就必须在try块之前就进行声明了，这时它是不可能进行正确的初始化的。

```java
// Instantiate class cl, which represents some sort of Set
Set s = null;
try {
    s = (Set) cl.newInstance();
} catch(IllegalAccessException e) {
    throw new IllegalArgumentException(cl + " not accessible");
} catch(InstantiationException e) {
    throw new IllegalArgumentException(cl + " not instantiable");
}

// Exercise the set
s.addAll(Arrays.asList(args));
```

但即便是这种情况也是可以避免的，把try-catch 块封装在一个方法内即可：

```java
Set createSet(Class cl) {
    // Instantiate class cl, which represents some sort of Set
    try {
        return (Set) cl.newInstance();
    } catch(IllegalAccessException e) {
        throw new IllegalArgumentException(cl + " not accessible");
    } catch(InstantiationException e) {
        throw new IllegalArgumentException(cl + " not instantiable");
    }
}

...

// Exercise the set
Set s = createSet(cl);
s.addAll(Arrays.asList(args));
```

除非理由十分充分，否则循环变量都应该在for语句内进行声明，：

```java
for (int i = 0; i n; i++) {
    doSomething(i);
}
```

和

```java
for (Iterator i = c.iterator(); i.hasNext(); ) {
    doSomethingElse(i.next());
}
```

##### 6.3.6  使用空格进行缩进

我们的代码块缩进使用4个空格。我们从不使用制表符tab。如果存在疑惑，与前后的其它代码保持一致即可。

我们用8个空格作为换行后的缩进，包括函数调用和赋值。例如这是正确的：

```java
Instrument i =
        someLongexpression_r(that, wouldNotFit, on, one, line);
```

而这是错误的：

```java
Instrument i =
    someLongexpression_r(that, wouldNotFit, on, one, line);
```

##### 6.3.7 使用标准的大括号风格

大括号不单独占用一行；它们紧接着上一行书写。就像这样：

```java
class MyClass {
    int func() {
        if (something) {
            // ...
        } else if (somethingElse) {
            // ...
        } else {
            // ...
        }
    }
}
```

我们需要用大括号来包裹条件语句块。不过也有例外，如果整个条件语句块（条件和语句本身）都能容纳在一行内，也可以（但不是必须）把它们放入同一行中。也就是说，这是合法的：

```java
if (condition) {
    body();
}
```

这也是合法的：

```java
if (condition) body();
```

但这是非法的：

```java
if (condition)
    body();  // bad!
```

##### 6.3.8 限制代码行的长度

每行代码的长度应该不超过`120`个字符。

有关本规则的讨论有很多，最后的结论还是最多不超过`120`个字符。

例外：如果注释行包含了超过`120`个字符的命令示例或者URL文字，为了便于剪切和复制，其长度可以超过`120`个字符。

例外：import行可以超过限制，因为很少有人会去阅读它。这也简化了编程工具的写入操作。

##### 6.3.9 使用标准的Java Annotation

Annotation应该位于Java语言元素的其它修饰符之前。 简单的marker annotation（@Override等）可以和语言元素放在同一行。 如果存在多个annotation，或者annotation是参数化的，则应按字母顺序各占一行来列出。

对于Java 内建的三种annotation，Android标准的实现如下：

* `@Deprecated`：只要某个语言元素已不再建议使用了，就必须使用@Deprecated annotation。如果使用了@Deprecated annotation，则必须同时进行@deprecated Javadoc标记，并且给出一个替代的实现方式。此外请记住，被@Deprecated的方法仍然是能正常执行的。

如果看到以前的代码带有@deprecated Javadoc标记，也请加上@Deprecated annotation。

* `@Override`:只要某个方法覆盖了已过时的或继承自超类的方法，就必须使用@Override annotation。

例如，如果方法使用了`@inheritdocs` Javadoc标记，且继承自超类（而不是interface），则必须同时用@Override标明覆盖了父类方法。

* `@SuppressWarnings`：`@SuppressWarnings` annotation仅用于无法消除编译警告的场合。 如果警告确实经过测试“不可能消除”，则必须使用@SuppressWarnings annotation，以确保所有的警告都能真实反映代码中的问题。

当需要使用`@SuppressWarnings` annotation时，必须在前面加上`TODO`注释行，用于解释“不可能消除”警告的条件。通常是标明某个令人讨厌的类用到了某个拙劣的接口。比如：

```java
// TODO: The third-party class com.third.useful.Utility.rotate() needs generics
@SuppressWarnings("generic-cast")
List<String> blix = Utility.rotate(blax);
```

如果需要使用@SuppressWarnings annotation，应该重新组织一下代码，把需要应用annotation的语言元素独立出来。

##### 6.3.10 简称等同于单词

简称和缩写都视为变量名、方法名和类名。以下名称可读性更强：

| 好 | 差 |
| :--------| ------- |
| XmlHttpRequest | XMLHTTPRequest |
| getCustomerId | getCustomerID |
| class Html | class HTML |
| String url | String URL |
| long id | long ID |

如何对待简称，JDK和Android底层代码存在很大的差异。因此，你几乎不大可能与其它代码取得一致。别无选择，把简称当作完整的单词看待吧。

关于本条规则的进一步解释，请参阅Effective Java第38条和Java Puzzlers第68条。

##### 6.3.11 使用TODO注释

对那些临时性的、短期的、够棒但不完美的代码，请使用TODO注释。

TODO注释应该包含全部大写的TODO，后跟一个冒号：

```java
// TODO: Remove this code after the UrlTable2 has been checked in.
```

和

```java
// TODO: Change this to use a flag instead of a constant.
```

如果TODO注释是“将来要做某事”的格式，则请确保包含一个很明确的日期（“在2005年11月会修正”），或是一个很明确的事件（“在所有代码整合人员理解了V7协议之后删除本段代码”）。

##### 6.3.12 慎用Log

记录日志会对性能产生显著的负面影响。如果日志内容不够简炼的话，很快会丧失可用性。日志功能支持五种不同的级别。以下列出了各个级别及其使用场合和方式。

* ERROR: 该级别日志应该在致命错误发生时使用，也就是说，错误的后果能被用户看到，但是不明确删除部分数据、卸装程序、清除数据区或重新刷机（或更糟糕）就无法恢复。该级别总是记录日志。需要记录ERROR级别日志的事件一般都应该向统计信息收集（statistics-gathering ）服务器报告。

* WARNING: 该级别日志应该用于那些重大的、意外的事件，也就是说，错误的后果能被用户看到，但是不采取明确的动作可能就无法无损恢复，从等待或重启应用开始，直至重新下载新版程序或重启设备。该级别总是记录日志。需记录WARNING级别日志的事件也可以考虑向统计信息收集服务器报告。

* INFORMATIVE: 该级别的日志应该用于记录大部分人都会感兴趣的事件，也就是说，如果检测到事件的影响面可能很广，但不一定是错误。应该只有那些拥有本区域内最高级别身份认证的模块才能记录这些日志（为了避免级别不足的模块重复记录日志）。该级别总是记录日志。

* DEBUG: 该级别的日志应该用于进一步记录有关调查、调试意外现象的设备事件。应该只记录那些有关控件运行所必需的信息。如果debug日志占用了太多的日志空间，那就应该使用详细级别日志（verbose）才更为合适。

即使是发行版本（release build），该级别也会被记录，并且需用if (LOCAL_LOG)或if (LOCAL_LOGD)语句块包裹，这里的LOCAL_LOG[D]在你的类或子控件中定义。这样就能够一次性关闭所有的调试日志。因此在if (LOCAL_LOG)语句块中不允许存在逻辑判断语句。所有日志所需的文字组织工作也应在if (LOCAL_LOG)语句块内完成。如果对记录日志的调用会导致在if (LOCAL_LOG)语句块之外完成文字组织工作，那该调用就必须控制在一个方法内完成。

还存在一些代码仍然在使用if (localLOGV)。这也是可以接受的，虽然名称不是标准的。

* VERBOSE: 该级别日志应用于所有其余的事件。该级别仅会在调试版本（debug build）下记录日志，并且需用if (LOCAL_LOGV)语句块（或等效语句）包裹，这样该部分代码默认就不会编译进发行版本中去了。所有构建日志文字的代码将会在发行版本中剥离出去，并且需包含在if (LOCAL_LOGV)语句块中。

> 注意：

* 除了VERBOSE级别外，在同一个模块中同一个错误应该尽可能只报告一次：在同一个模块内的一系列层层嵌套的函数调用中，只有最内层的函数才返回错误；并且只有能为解决问题提供明显帮助的时候，同一模块中的调用方才写入一些日志。

* 除了VERBOSE级别外，在一系列嵌套的模块中，当较低级别的模块对来自较高级别模块的非法数据进行检测时，应该只把检测情况记录在DEBUG日志中，并且只记录那些调用者无法获取的信息。特别是不需要记录已经抛出异常的情况（异常中应该包含了全部有价值的信息），也不必记录那些只包含错误代码的信息。当应用程序与系统框架间进行交互时，这一点尤为重要。系统框架已能正确处理的第三方应用程序，也不应该记录大于DEBUG级别的日志。仅当一个模块或应用程序检测到自身或来自更低级别模块的错误时，才应该记录INFORMATIVE及以上级别的日志。

* 如果一个通常要记录日志的事件可能会多次发生，则采取一些频次限制措施或许是个好主意，以防日志被很多重复（或类似）的信息给撑爆了。

* 网络连接的丢失可被视为常见现象，也是完全可以预见的，不应该无缘无故就记录进日志。影响范围限于应用程序内部的网络中断应该记录在DEBUG或VERBOSE级别的日志中（根据影响的严重程度及意外程度，再来确定是否在发行版本中也记录日志）。

* 有权访问的文件系统或第三方应用程序发起的系统空间满，应该记录大于INFORMATIVE级别的日志。

* 来自任何未授信源的非法数据（包括共享存储上的任何文件，或来自任何网络连接的数据）可被视为可预见的，如果检测到非法数据也不应该记录大于DEBUG级别的日志（即使记录也应尽可能少）。

* 请记住，对字符串使用+操作符时，会在后台以默认大小（16个字符）缓冲区创建一个`StringBuilder`对象，并且可能还会创建一些其它的临时String对象。换句话说，显式创建StringBuilders对象的代价并不会比用'+'操作符更高（事实上效率还将会提高很多）。还要记住，即使不会再去读取这些日志，调用Log.v()的代码也将编译进发行版中并获得执行，包括创建字符串的代码。

* 所有要被人阅读并存在于发行版本中的日志，都应该简洁明了、没有秘密、容易理解。这里包括所有DEBUG以上级别的日志。

* 只要有可能，日志就应该一句一行。行长最好不超过80或100个字符，尽可能避免超过130或160个字符（包括标识符）的行。

* 报告成功的日志记录绝不应该出现在大于VERBOSE级别的日志中。

* 用于诊断难以重现事件的临时日志应该限于DEBUG或VERBOSE级别，并且应该用if语句块包裹，以便在编译时能够一次全部关闭。

* 小心日志会泄漏隐私。应该避免将私人信息记入日志，受保护的内容肯定也不允许记录。这在编写系统框架级代码时尤为重要，因为很难预知哪些是私人信息和受保护信息。

* 绝对不要使用`System.out.println()` （或本地代码中的`printf()`）。System.out 和 System.err会重定向到/dev/null，因此print语句不会产生任何可见的效果。可是，这些调用中的所有字符串创建工作都仍然会执行。

* 日志的黄金法则是：你的日志记录不会导致其它日志的缓冲区溢出，正如其他人的日志也不会让你的溢出一样。

##### 6.3.13 保持一致

我们的最终想法是：保持一致。如果你正在编写代码，请花几分钟浏览一下前后的其它代码，以确定它们的风格。如果它们在if语句前后使用了空格，那你也应该遵循。如果它们的注释是用星号组成的框框围起来的，那也请你照办。

保持风格规范的重点是有一个公共的代码词汇表，这样大家就可以把注意力集中于你要说什么，而不是你如何说。我们在这里列出了全部的风格规范，于是大家也知道了这些词汇。不过本地化的风格也很重要。如果你要加入的代码和已存在的代码风格迥异，那就会突然打破阅读的节奏。请努力避免这种情况的发生。

##### 6.3.14 遵守测试方法命名规范

命名测试方法时，可以用下划线来分隔测试的条件。这种风格可以让测试的条件一目了然。

比如:

```java
testMethod_specificCase1 testMethod_specificCase2

void testIsDistinguishable_protanopia() {
    ColorMatcher colorMatcher = new ColorMatcher(PROTANOPIA)
    assertFalse(colorMatcher.isDistinguishable(Color.RED, Color.BLACK))
    assertTrue(colorMatcher.isDistinguishable(Color.X, Color.Y))
}
```

### 9 其他规范

1. 合理布局，有效运用`<merge>`、`<ViewStub>`、`<include>`标签；
2. `Activity`和`Fragment`里面有许多重复的操作以及操作步骤，所以我们都需要提供一个`BaseActivity`和`BaseFragment`，让所有的`Activity`和`Fragment`都继承这个基类。
3. 方法基本上都按照调用的先后顺序在各自区块中排列；
4. 相关功能作为小区块放在一起（或者封装掉）；
5. 当一个类有多个构造函数，或是多个同名方法，这些函数/方法应该按顺序出现在一起，中间不要放进其它函数/方法；
6. 数据提供统一的入口。无论是在 MVP、MVC 还是 MVVM 中，提供一个统一的数据入口，都可以让代码变得更加易于维护。比如可使用一个`DataManager`，把 `http`、`preference`、`eventpost`、`database` 都放在`DataManger`里面进行操作，我们只需要与`DataManger`打交道；
7. 多用组合, 少用继承；
8. 提取方法, 去除重复代码。对于必要的工具类抽取也很重要，这在以后的项目中是可以重用的。
10. 项目引入`RxJava` + `RxAndroid`这些响应式编程，可以极大的减少逻辑代码；
11. 通过引入事件总线，如：`EventBus`、`AndroidEventBus`、`RxBus`，它允许我们在`DataLayer`中发送事件，以便`ViewLayer`中的多个组件都能够订阅到这些事件，减少回调；
12. 尽可能使用局部变量；
13. 及时关闭流；
14. 尽量减少对变量的重复计算；

  如下面的操作：

  ``` java
  for (int i = 0; i < list.size(); i++) {
      ...
  }
  ```

  建议替换为：

  ``` java
  for (int i = 0, int length = list.size(); i < length; i++) {
      ...
  }
  ```

15. 尽量采用懒加载的策略，即在需要的时候才创建；

  例如：

  ``` java
  String str = "aaa";
  if (i == 1) {
      list.add(str);
  }
  ```

  建议替换为：

  ``` java
  if (i == 1) {
      String str = "aaa";
      list.add(str);
  }
  ```

16. 不要在循环中使用try…catch…，应该把其放在最外层；
17. 使用带缓冲的输入输出流进行IO操作；
18. 尽量使用HashMap、ArrayList、StringBuilder，除非线程安全需要，否则不推荐使用Hashtable、Vector、StringBuffer，后三者由于使用同步机制而导致了性能开销；
19. 尽量在合适的场合使用单例；

  使用单例可以减轻加载的负担、缩短加载的时间、提高加载的效率，但并不是所有地方都适用于单例，简单来说，单例主要适用于以下三个方面：

  （1）控制资源的使用，通过线程同步来控制资源的并发访问

  （2）控制实例的产生，以达到节约资源的目的

  （3）控制数据的共享，在不建立直接关联的条件下，让多个不相关的进程或线程之间实现通信

20. 把一个基本数据类型转为字符串，`基本数据类型.toString()`是最快的方式、`String.valueOf(数据)`次之、`数据 + ""`最慢；
21. 使用AS自带的Lint来优化代码结构（什么，你不会？右键module、目录或者文件，选择Analyze → Inspect Code）；
22. 最后不要忘了内存泄漏的检测；

---

## 附录

### UI控件缩写表

| 名称             | 缩写   |
| -------------- | ---- |
| TextView       | tv   |
| EditText       | et   |
| ImageButton    | ib   |
| Button         | btn  |
| ImageView      | iv   |
| ListView       | lv   |
| GridView       | gv   |
| ProgressBar    | pb   |
| SeekBar        | sb   |
| RadioButtion   | rb   |
| CheckBox       | cb   |
| ScrollView     | sv   |
| LinearLayout   | ll   |
| FrameLayout    | fl   |
| RelativeLayout | rl   |
| RecyclerView   | rv   |
| WebView        | wv   |
| VideoView      | vv   |
| Spinner        | spn  |
| ToggleButton   | tb   |

### 常见的英文单词缩写表

| 名称                   | 缩写                                       |
| -------------------- | ---------------------------------------- |
| icon                 | ic （主要用在app的图标）                          |
| color                | cl（主要用于颜色值）                              |
| average              | avg                                      |
| background           | bg（主要用于布局和子布局的背景）                        |
| selector             | selector(主要用于某一view多种状态，不仅包括Listview中的selector，还包括按钮的selector） |
| buffer               | buf                                      |
| control              | ctrl                                     |
| default              | def                                      |
| delete               | del                                      |
| document             | doc                                      |
| error                | err                                      |
| escape               | esc                                      |
| increment            | inc                                      |
| infomation           | info                                     |
| initial              | init                                     |
| image                | img                                      |
| Internationalization | I18N                                     |
| length               | len                                      |
| library              | lib                                      |
| message              | msg                                      |
| password             | pwd                                      |
| position             | pos                                      |
| server               | srv                                      |
| string               | str                                      |
| temp                 | tmp                                      |
| window               | wnd(win)                                 |

程序中使用单词缩写原则：不要用缩写，除非该缩写是约定俗成的。


## 参考

[安卓开发规范(updating)][安卓开发规范(updating)]

[Android包命名规范][Android包命名规范]

[Android 开发最佳实践][Android 开发最佳实践]

[Android 编码规范][Android 编码规范]

[阿里巴巴Java开发手册][阿里巴巴Java开发手册]

[Google Java编程风格指南][Google Java编程风格指南]

[小细节，大用途，35 个 Java 代码性能优化总结！][小细节，大用途，35 个 Java 代码性能优化总结！]


## 版本日志

* 17/06/29: 整理初版；

[Package by features, not layers]: https://medium.com/@cesarmcferreira/package-by-features-not-layers-2d076df1964d#.mp782izhh
[iosched]: https://github.com/google/iosched/tree/master/android/src/main/java/com/google/samples/apps/iosched
[安卓开发规范(updating)]: https://github.com/Blankj/AndroidStandardDevelop
[AS常用开发插件]: http://www.jianshu.com/p/c76b0d8a642d
[Android Studio下对资源进行分包]: http://www.jianshu.com/p/8e893581b9c7
[Android开发之版本统一规范]: http://www.jianshu.com/p/db6ef4cfa5d1
[Android 流行框架查速表]: http://www.ctolib.com/cheatsheets-Android-ch.html
[Android开发人员不得不收集的代码]: https://github.com/Blankj/AndroidUtilCode
[Retrofit]: https://github.com/square/retrofit
[RxAndroid]: https://github.com/ReactiveX/RxAndroid
[OkHttp]: https://github.com/square/okhttp
[Glide]: https://github.com/bumptech/glide
[Fresco]: https://github.com/facebook/fresco
[Gson]: https://github.com/google/gson
[Fastjson]: https://github.com/alibaba/fastjson
[EventBus]: https://github.com/greenrobot/EventBus
[AndroidEventBus]: https://github.com/bboyfeiyu/AndroidEventBus
[GreenDao]: https://github.com/greenrobot/greenDAO
[Dagger2]: https://github.com/google/dagger
[Tinker]: https://github.com/Tencent/tinker
[Android包命名规范]: http://www.ayqy.net/blog/android%E5%8C%85%E5%91%BD%E5%90%8D%E8%A7%84%E8%8C%83/
[Android 开发最佳实践]: https://github.com/futurice/android-best-practices/blob/master/translations/Chinese/README.cn.md
[Android 编码规范]: http://www.jianshu.com/p/0a984f999592
[AOSP Code Style for Contributors]: https://source.android.com/source/code-style.html
[阿里巴巴Java开发手册]: https://102.alibaba.com/newsInfo.htm?newsId=6
[Google Java编程风格指南]: http://www.hawstein.com/posts/google-java-style.html
[小细节，大用途，35 个 Java 代码性能优化总结！]: http://www.jianshu.com/p/436943216526


