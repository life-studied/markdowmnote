---
create: '2025-05-22'
modified: '2025-05-22'
---

# 核心pom.xml

`pom.xml`是Maven项目的核心配置文件，它定义了项目的元数据、依赖、插件、构建规则等信息。

### 最小的`pom.xml`示例

以下是一个最基础的`pom.xml`文件示例，它只包含了项目的基本元数据：

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion> <!-- Maven模型版本 -->

    <groupId>com.example</groupId> <!-- 项目所属的组织 -->
    <artifactId>my-java-project</artifactId> <!-- 项目的唯一标识 -->
    <version>1.0-SNAPSHOT</version> <!-- 项目的版本号 -->
</project>
```

### **基础内容解释**

1. **`<modelVersion>`**
    - 指定Maven模型的版本，固定为`4.0.0`，表示使用Maven 2.x或更高版本的POM模型。
2. **`<groupId>`**
    - 项目所属的组织或公司名称，通常是一个反向域名，如`com.example`。它用于区分不同组织的项目。
3. **`<artifactId>`**
    - 项目的唯一标识，通常是一个项目名称，如`my-java-project`。它与`groupId`一起唯一标识一个项目。
4. **`<version>`**
    - 项目的版本号，如`1.0-SNAPSHOT`。`SNAPSHOT`表示这是一个开发中的版本，尚未正式发布。