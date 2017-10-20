WebFun
========
为 Kotlin 设计的极简 Web 框架

快速开始
--------
```kotlin
fun main(args: Array<String>) {
    val app = Application.create(currentClassName())
    app.run(8080)
}

@Route("/")
fun index(name: String = "Tom", req: Request): String {
    return "hello, $name, method: ${req.method}"
}
```



TODO
-----------------------
1. req & resp 类设计（不可变类）
1. cookie 支持
1. 简单路由
1. 简单路由参数
1. 错误路由（@ErrorHandler 注解）
1. 中间件
1. 日志集成
1. 中间件依赖注入
1. 环境变量
1. 启动参数
1. 静态文件支持
1. 模板引擎
1. session
1. 输出扩展（可以扩展输出格式）
1. 输入参数扩展
1. 文件上传
1. 静态文件支持
1. spring
1. 辅助response函数：abort， redirect
1. 编译期反射，去除运行时反射
1. 错误页
1. request files
1. json 支持
1. 切换 undertow.io


外观（API）
-----------------------
1. 路由（参数路由，路由参数注入）
1. url_for 反向生成路由url （http://flask.pocoo.org/docs/0.12/quickstart/#url-building）
1. req
1. resp
1. 静态文件
1. 模板 （http://flask.pocoo.org/docs/0.12/quickstart/#rendering-templates）
1. 配置文件 （https://github.com/npryce/konfig）
1. 模块支持
1. 调试模式
1. 测试支持


路由设计
-----------------------
1. 路由扫描（支持类扫描、手动添加两种） OK
1. 路由匹配 OK
1. 路由参数 OK
1. 参数注入 OK
1. 路由参数转换器
1. 反向生成路由
