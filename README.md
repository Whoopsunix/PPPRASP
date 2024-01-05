# PPPRASP

By. Whoopsunix

## why jvm-sandbox？

发现 [jvm-sandbox](https://github.com/alibaba/jvm-sandbox) 从 1.4.0 开始支持 Native 的增强，正好写一个简单的 RASP Demo 来熟悉这个 AOP 框架（~~其实是懒得用从头用 ASM 写~~）。

+ AOP 框架、沙箱类隔离等架构优点，很难拒绝
+ 基层基于 ASM 实现，框架比较熟悉，后续有更复杂的需求时可以改源码方便
+ 虽然没有一个很详细的文档，不过好在源代码注释非常多，并且给出了 [Module 编写例子](https://github.com/oldmanpushcart/sandbox-module-example/blob/master/README.md)，在 [sandbox-debug-module](https://github.com/alibaba/jvm-sandbox/blob/1.4.0/sandbox-debug-module) 中提供了很多工具类代码

RASP 真的很适合用来学 Java，复现、分析、防护，连贯性的学习会加深对 Java 的理解。

## 关于项目

🚩 陆续同步 [JavaRce](https://github.com/Whoopsunix/JavaRce) 项目实现基础漏洞的 HOOK，已实现的 CVE 触发检测可从 [PPPVULNS](https://github.com/Whoopsunix/PPPVULNS) 项目获取靶场

⭐️ 只有来自外部的请求才会进入 HOOK 点检测

+ 可以使用项目配套测试环境 [SecVulns](SecVulns) 进行测试
+ [vulnsCore](SecVulns/vulnsCore) 为漏洞代码，不同的组件引入后可以直接运行
+ [SecVulnsREST](SecVulns/SecVulnsREST) 为 Rest Client 文件可直接发送

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

# 0x01 基本漏洞检测类型

暂时通过 [rasp.yml](ppprasp-agent/src/main/resources/rasp.yml) 配置文件来配置漏洞检测

## 反序列化

- [x] 接入黑名单

| Hook 点                                  | REST API               | 备注 |
| ---------------------------------------- | ---------------------- | ---- |
| java.io.ObjectInputStream#resolveClass() | /deserialization/case1 |      |

## 表达式注入

- [x] 接入黑名单

| Hook 点                                                      | REST API    | 备注 |
| ------------------------------------------------------------ | ----------- | ---- |
| org.springframework.expression.spel.standard.SpelExpression#getValue() | /spel/case1 |      |
| 同 case1 使用 StandardEvaluationContext                      | /spel/case2 |      |
| 同 case1 使用 MethodBasedEvaluationContext                   | /spel/case3 |      |
| ognl.Ognl#getValue()                                         | /ognl/case1 |      |
| ognl.Ognl#setValue()                                         | /ognl/case2 |      |

## JNDI 注入

| Hook 点                       | REST API    | 备注 |
| ----------------------------- | ----------- | ---- |
| javax.naming.Context#lookup() | /jndi/case1 |      |

## JNI 注入

| Hook 点                 | REST API   | 备注 |
| ----------------------- | ---------- | ---- |
| java.lang.System#load() | /jni/case1 |      |

## 命令执行

- [x] 参考 [jrasp](https://github.com/jvm-rasp/jrasp-agent) 实现了线程注入的拦截
- [x] Jvm-sandbox 1.4.0 实现了 [native 方法的 hook](https://github.com/alibaba/jvm-sandbox/blob/c01c28ab5d7d97a64071a2aca261804c47a5347e/sandbox-core/src/main/java/com/alibaba/jvm/sandbox/core/enhance/weaver/asm/EventWeaver.java) ，因此支持拦截 `forkAndExec()`

| Hook 点                             | REST API    | 备注                                 |
| ----------------------------------- | ----------- | ------------------------------------ |
| java.lang.ProcessBuilder.start()    | /exec/case1 | Runtime                              |
| java.lang.ProcessBuilder.start()    | /exec/case4 | processBuilder                       |
| 线程注入                            | /exec/case2 | 参考 jrasp 实现                      |
| java.lang.UNIXProcess.forkAndExec() | /exec/case3 | processImpl                          |
| java.lang.UNIXProcess.forkAndExec() | /exec/case5 | processImplUnixProcess               |
| java.lang.UNIXProcess.forkAndExec() | /exec/case6 | processImplUnixProcessByUnsafeNative |

## SQL注入

- [ ] 没有语义词义分析

| Hook 点                                    | REST API         | 备注 |
| ------------------------------------------ | ---------------- | ---- |
| com.mysql.cj.jdbc.StatementImpl 类查询语句 | /sql/mysql/case1 |      |

# 0x02 CVE漏洞触发检测

CVE 漏洞分成两类

+ 一类是在基础漏洞上的触发比如 SPEL ，不需要额外 HOOK，遍历调用栈匹配漏洞触发类就能确定是否由 CVE 触发，所以之后分析漏洞时都会添加
+ 另一类就是框架本身的问题，比如权限绕过这种不好直接归类到具体的漏洞类别中，目前暂时不处理

## 支持漏洞

反序列化漏洞需要准确识别到调用了相关黑名单函数才会拦截，目前没有针对此补全，所以实际 CVE 检测时可能上报其他漏洞检测类型。

| 基本漏洞类型    | 组件             | CVE                          |
| --------------- | ---------------- | ---------------------------- |
| SPEL            | Spring-messaging | CVE-2018-1270, CVE-2018-1275 |
| Deserialization | Apache Dubbo     | CVE-2019-17564               |
| Deserialization | Apache Dubbo     | CVE-2020-1948                |
