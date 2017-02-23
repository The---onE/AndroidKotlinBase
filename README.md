# AndroidKotlinBase 安卓Kotlin基础框架
### 本程序是基于Android Studio 2搭建的，致力于快速开发使用Kotlin语言编写的Android应用的基础框架。
##### 本程序数据管理及用户管理功能基于LeanCloud提供的数据存储功能，在开发程序前需要在云端创建应用，并创建数据表将对应信息初始化到程序中
本框架修改于AS Navigation Drawer Activity模版，添加了一些常用工具类，用于快速实现开发：
- 添加Activity基类、Fragment基类、Service基类、适配器基类等类集成常用功能用于快速搭建页面
- 添加用户管理器和示例页面可快速实现用户的登录、注册、自动登录、检验登录等功能
- 添加三种数据实体接口和对应管理器，实现对SQLite数据库、LeanCloud数据库、本地云端数据同步的快速管理，通过将数据转化为实体，可以方便地展示在ListView等控件中
- 更多常用工具类持续更新中……

## 目录结构
### java
#### base 应用核心组件基类
- 包括Activity基类、Fragment基类、Service基类等
- 开发应用时无需对本目录文件进行修改，根据需求继承相关类即可

#### common 应用基本功能模块
- 包括数据管理、用户管理、日志管理等基本功能
- 开发应用时无需对本目录文件进行修改，对与不需要的功能模块可以进行清理

#### core 应用核心内容
- 包括自定义Application、主Activity及相关Fragment、异常处理器等核心内容
- 在MyApplication.kt中初始化各插件
- 在CoreConstants.kt设置应用基本常量
- 在activity/MainActivity.kt中设置主页面显示的Fragment及其标题，配置侧滑菜单、设置菜单等属性
- 在fragment目录中根据需求创建对应的Fragment，用于测试各功能模块的Fragment可以删除

#### module 应用功能模块
- 用于演示各功能模块的使用方法，包含详细注释，在实际开发中根据需求替换为各功能的实际代码
- 暂时完成数据管理、用户管理、IM、操作日志、前台服务、内嵌网页(有BUG)等功能模块

#### utils 工具类
- 包含各种用于方便操作的工具类，在实际开发中可以直接使用对象调用其中的方法

### assets
- 保存应用中的资源文件

### res
- 保存应用中用于布局的文件

## 开发准备
#### 复制文件
- 在Android Studio中打开本框架，若成功创建名为“基础Kotlin框架”的应用则已成功配置
- 创建本框架的副本，作为新应用的基础文件、修改根目录文件夹名为自定义名称，在AS中打开副本文件夹，即可开始进行项目的开发

#### 修改包名
- 参考[修改包名](http://www.jianshu.com/p/557e1906db1a)中的方法将com.xmx.androidkotlinbase修改为新应用的包名
- 修改包名后即可尝试打包生成新应用，开始开发调试

#### 修改应用名
- 打开res/values/strings.xml文件，其中包含了一些常用的提示语等字符串，修改app_name的值即可修改应用名

####修改启动界面
- 修改res/values/splash.png为自定义图片，即可在打开APP时看到启动启动界面

#### 云端初始化
- 在[LeanCloud](https://leancloud.cn/)注册帐号，创建一个新应用
- 在云端控制台设置中，在应用Key页面查看应用信息，将对应信息保存在java/core/Constants类中的APP_ID和APP_KEY常量中

## 自定义页面
- 本框架主体全部运行在core/activity/MainActivity中，通过ViewPager对自定义页面进行管理
- MainActivity的initView方法fragments和titles对应保存着要显示的Fragment和其标题。
- 框架中已添加用于测试各功能模块的Fragment，开发时根据需求添加修改页面
- 要添加自定义页面，只需创建好Fragment，之后将其添加到对应的列表即可，添加顺序即为滑动显示顺序

## 用户管理器
- 本框架使用common/user/userManager单例对象对用户登录注册等进行管理，用户信息保存在LeanCloud上的用户帐号密码信息表、用户基本数据表中
- 开发新应用时将，将各表表名分别保存在common/user/UserConstants类中的常量中
- 成功登录后可以通过保存在SharedPreferences中的校验码实现自动登录，通过校验码机制可以实现不保存密码，并且当异地登陆时提示重新登录的自动登录功能
- 用户帐号密码信息表(USER_INFO_TABLE)：保存用户用户名、密码、状态等，在登录时进行校验
- 用户基本数据表(USER_DATA_TABLE)：保存用户用户名、昵称、校验码等信息，可在后期进行拓展，通过该表可进行自动登录
- 用户登录日志表(LOGIN_LOG_TABLE)：保存用户每次登录时间等信息，只可添加查询
