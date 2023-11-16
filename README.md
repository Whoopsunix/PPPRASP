# PPPRASP

By. Whoopsunix

发现 [jvm-sandbox](https://github.com/alibaba/jvm-sandbox) 从 1.4.0 开始实现了 Native 的增强，正好P写一个简单的 RASP Demo 来熟悉这个 AOP 框架（~~其实是懒得用 ASM~~）

RASP 真的很适合用来学 Java，复现分析+防护的漏洞学习模式会加深漏洞的理解能力

why jvm-sandbox？

+ AOP 框架、沙箱类隔离等架构优点，很难拒绝
+ 基层基于 ASM 实现，框架比较熟悉，后续有更复杂的需求时可以改源码方便
+ 虽然没有一个很详细的文档，不过好在源代码注释非常多，并且给出了 [Module 编写例子](https://github.com/oldmanpushcart/sandbox-module-example/blob/master/README.md)，在 [sandbox-debug-module](https://github.com/alibaba/jvm-sandbox/blob/1.4.0/sandbox-debug-module) 中提供了很多工具类代码

🚩 同步 [JavaRce](https://github.com/Whoopsunix/JavaRce) 项目实现基础漏洞的 HOOK，同步 [PPPVULNS](https://github.com/Whoopsunix/PPPVULNS) 项目实现部分 CVE 触发的识别

⭐️ 只会拦截来自 http 请求的 HOOK 点触发，可以使用项目配套测试环境进行测试 [vulEnv](vulEnv)，配套 [postman api 文件](vulEnv/vulEnv.postman_collection.json)

---------------

# 0x00 Start

1. 打包 ppprasp-agent 

pom.xml 报错是正常的，不影响打包

```
mvn clean package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
```

2. 下载 [jvm-sandbox 二进制包](https://github.com/alibaba/jvm-sandbox/releases)
3. 将打包好的项目移动到 sandbox/sandbox-module 文件夹下
4. 启动

-javaagent

```
-javaagent:sandbox/lib/sandbox-agent.jar
```

或者 attach

```
# 进入沙箱执行脚本
cd sandbox/bin

# 挂载 目标JVM进程33342
./sandbox.sh -p 33342

# 卸载
./sandbox.sh -p 33342 -S
```

## sandbox 其他

-d 可以向 Module 发送命令，eg. `-d 'sandbox-info/version'`

-P 开启 Jetty 接口，`./sandbox.sh -p 80258 -P 1234`

接口开启后可以通过 http 发送 -d 中的命令，eg. `/sandbox/default/module/http/sandbox-info/version`

这部分内容没有文档可以参考 [sandbox-mgr-module](https://github.com/alibaba/jvm-sandbox/blob/c01c28ab5d7d97a64071a2aca261804c47a5347e/sandbox-mgr-module/src/main/java/com/alibaba/jvm/sandbox/module/mgr/ModuleMgrModule.java) 来自行构建

# 0x01 基本漏洞检测类型 ing

通过 [rasp.yml](ppprasp-agent/src/main/resources/rasp.yml) 配置文件来开启漏洞检测

## 命令执行

- [x] 参考 [jrasp](https://github.com/jvm-rasp/jrasp-agent) 实现了线程注入的拦截
- [x] Jvm-sandbox 1.4.0 实现了 [native 方法的 hook](https://github.com/alibaba/jvm-sandbox/blob/c01c28ab5d7d97a64071a2aca261804c47a5347e/sandbox-core/src/main/java/com/alibaba/jvm/sandbox/core/enhance/weaver/asm/EventWeaver.java) ，因此支持拦截 `forkAndExec()`

## 反序列化

- [x] 黑名单拦截，写了个 `com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl` 意思意思

## sql注入

- [x] `com.mysql.cj.jdbc.StatementImpl` 类下 sql 执行语句全拦截，没加语义词义分析

## 表达式注入

SPEL、OGNL

- [x] 黑名单拦截

## JNI 注入

- [x] hook 来自外部输入的 `java.lang.ClassLoader.loadLibrary0()` 调用

# 0x02 CVE漏洞检测

CVE 漏洞分成两类

+ 一类是在基础漏洞上的触发比如 SPEL ，不需要额外 HOOK，调用栈遍历时添加一个类匹配就能检查是否由 CVE 触发，所以分析过的漏洞就顺便加上了
+ 另一类就是框架本身的问题，这部分需要额外工作量暂时就不考虑引入了

## 支持漏洞

| 基本漏洞类型 | 组件             | CVE                          |
| ------------ | ---------------- | ---------------------------- |
| SPEL         | Spring-messaging | CVE-2018-1270, CVE-2018-1275 |
| Deserialize  | Apache Dubbo     | CVE-2019-17564               |
