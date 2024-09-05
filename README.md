# plugin-injection

基于 Halo 2.0 插件开发快速开始模板开发的代码注入管理插件，可以实现对 HTML 代码片段的动态管理和注入。  
用户可以灵活进行查询、创建、编辑、删除、启用和禁用操作。插件支持配置注入规则，根据页面路径和位置（头部或尾部）动态将代码注入目标页面。

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
3. 在工具菜单中打开`代码注入管理界面`，进入代码注入管理列表。
4. 配置`代码名称`，`代码描述`，`代码片段`，`注入位置`，`页面匹配规则`以及`代码片段`信息。
5. 保存并启用或禁用注入代码。
