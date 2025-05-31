---
create: '2025-05-25'
modified: '2025-05-25'
---

# maven命令行创建项目

```shell
mvn -B archetype:generate -DgroupId=com.example -DartifactId=maven-sample -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4
```

- `-B`：批处理模式，自动接受默认选项。
- `archetype:generate`：生成新 Maven 项目。
- `-DgroupId=com.example`：项目 Group ID。
- `-DartifactId=maven-sample`：项目 Artifact ID，一般用项目名。
- `-DarchetypeArtifactId=maven-archetype-quickstart`：使用的 Archetype 模板创建项目。
- `-DarchetypeVersion=1.4`：使用的 Archetype 版本。