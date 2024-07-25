# plugin-injection

基于Halo 2.0 插件开发快速开始模板开发的代码注入管理插件。
用户可以添加、编辑、删除和启用/禁用HTML代码片段，并通过特定规则匹配页面和注入位置。

## 开发环境

所需环境：

1. Java 17
2. Node 20
3. pnpm 9
4. Docker (可选)

克隆项目：

```bash
git clone git@github.com:halo-sigs/plugin-injection.git

# 或者当你 fork 之后

git clone git@github.com:{your_github_id}/plugin-injection.git
```

```bash
cd path/to/plugin-injection
```

```bash
# macOS / Linux
./gradlew build

# Windows
./gradlew.bat build
```
## 使用方式 

1. 在 Releases 下载最新的 JAR 文件。
2. 在 Halo 后台的插件管理上传 JAR 文件进行安装。
3. ...
