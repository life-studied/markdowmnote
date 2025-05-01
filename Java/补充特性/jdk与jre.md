---
create: '2025-05-02'
modified: '2025-05-02'
---

# jdk与jre

## 1. **JRE（Java Runtime Environment）**

**定义**：
JRE 是 Java 运行时环境，它是运行 Java 程序所必需的环境。它包含了 Java 虚拟机（JVM）、Java 核心类库（如 `java.lang`、`java.util` 等）以及运行 Java 程序所需的其他资源。

**功能**：

- **运行 Java 程序**：JRE 提供了运行 Java 应用程序所需的基础设施。当你运行一个 Java 程序时（例如通过 `java -jar myapp.jar` 命令），实际上是 JRE 中的 JVM 在执行程序。
- **核心类库**：JRE 包含了 Java 标准库，这些库提供了基本的数据结构、集合、I/O 操作、网络功能等，是 Java 程序运行的基础。

**组成**：

- **Java 虚拟机（JVM）**：JVM 是 Java 程序的运行引擎。它负责将 Java 字节码（.class 文件）转换为机器码，并在计算机上执行。
- **Java 核心类库**：这些类库提供了 Java 程序运行所需的各种功能。
- **其他支持文件**：如配置文件、国际化支持等。

**应用场景**：
如果你只需要运行一个 Java 程序，那么安装 JRE 就足够了。

## 2. **JDK（Java Development Kit）**

**定义**：
JDK 是 Java 开发工具包，它是一个完整的 Java 开发环境，包含了 JRE 和开发工具。

**功能**：JDK 提供了编译、调试、打包等一系列开发工具。

- **`javac`**：Java 编译器，用于将 Java 源代码（.java 文件）编译成字节码（.class 文件）。
- **`java`**：Java 运行工具，用于启动 Java 应用程序。
- **`jar`**：用于创建和管理 Java 归档文件（.jar 文件）。
- **`javadoc`**：用于生成 Java 文档。
- **`jdb`**：Java 调试器。

- **`JRE`**：JDK 包含了 JRE，因此它既可以编译 Java 程序，也可以运行 Java 程序。
- 其他工具：如 `jps`（查看 Java 进程）、`jstat`（监控 JVM 性能）等。

**应用场景**：
如果你需要开发 Java 程序，那么需要安装 JDK。JDK 是 Java 开发人员的必备工具。

## 3. **JDK 和 JRE 的关系**

- JDK 包含了 JRE，因此安装 JDK 后，你同时拥有了开发和运行 Java 程序的能力。
- JRE 仅用于运行 Java 程序，而 JDK 用于开发 Java 程序。