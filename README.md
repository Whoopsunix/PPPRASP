# PPPRASP

By. Whoopsunix

发现 [jvm-sandbox](https://github.com/alibaba/jvm-sandbox) 从 1.4.0 开始实现了 Native 的增强，正好写一个简单的 RASP Demo 来熟悉框架（~~其实是懒得用 ASM~~）

jvm-sandbox 没有一个很详细的文档，不过好在源代码注释非常多，并且给出了 [Module 编写例子](https://github.com/oldmanpushcart/sandbox-module-example/blob/master/README.md) ，并且在 [sandbox-debug-module](https://github.com/alibaba/jvm-sandbox/blob/1.4.0/sandbox-debug-module) 中提供了很多工具类代码，真不错啊真不错

🚩 后续同步 [JavaRce](https://github.com/Whoopsunix/JavaRce) 中的例子来实现 RASP 的 HOOK

⭐️ 只会拦截来自 http 请求的 HOOK 点触发

---------------

# 0x00 Start

1. 打包 ppprasp-agent 

pom.xml 报错是正常的，不影响打包

```
mvn clean package
```

2. 下载 [jvm-sandbox 二进制包](https://github.com/alibaba/jvm-sandbox/releases)
3. 将打包好的项目移动到 sandbox/sandbox-module 文件夹下
4. 启动

-javaagent

```
-javaagent:sandbox/lib/sandbox-agent.jar
```

attach

```
# 进入沙箱执行脚本
cd sandbox/bin

# 挂载 目标JVM进程33342
./sandbox.sh -p 33342

# 卸载
./sandbox.sh -p 33342 -S
```

# 0x01 漏洞检测类型

## 命令执行

- [x] 参考 [jrasp](https://github.com/jvm-rasp/jrasp-agent) 实现了线程注入的拦截
- [x] Jvm-sandbox 1.4.0 实现 native 方法拦截

## 反序列化

- [x] 黑名单拦截，写了个 `com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl` 意思意思



