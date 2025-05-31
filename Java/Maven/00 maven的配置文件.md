---
create: '2025-05-25'
modified: '2025-05-25'
---

# maven的配置文件

* /conf/settings.xml：maven的配置文件

## settings.xml说明

* localRepository：本地仓库（默认的依赖下载后的缓存地址）位置，默认在`${user_name}/.m2/respository`下

    * ```xml
        <localRepository>/path/to/local/repo</localRepository>
        ```

* mirrors：镜像配置

    * ```xml
          <mirrors>
            <!-- mirror
            <mirror>
              <id>mirrorId</id>					镜像标识
              <mirrorOf>repositoryId</mirrorOf>	哪些仓库会使用该镜像，默认全选：*
              <name>Human Readable Name for this Mirror.</name>	镜像名字
              <url>http://my.repository.com/repo/path</url>	镜像地址
            </mirror>
             -->
            <mirror>
              <id>aliyunmaven</id>
              <mirrorOf>*</mirrorOf>
              <name>阿里云公共仓库</name>
              <url>https://maven.aliyun.com/repository/public</url>
            </mirror>
              
            <mirror>
              <id>maven-default-http-blocker</id>
              <mirrorOf>external:http:*</mirrorOf>
              <name>Pseudo repository to mirror external repositories initially using HTTP.</name>
              <url>http://0.0.0.0/</url>
              <blocked>true</blocked>
            </mirror>
          </mirrors>
        ```

* profile：配置jdk版本

    * ```xml
        <profiles>
            <profile>
              <id>jdk-22</id>
        
              <activation>
                <activeByDefault>true</activeByDefault>
        		<jdk>1.4</jdk>
              </activation>
        
              <properties>
                <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                <maven.compiler.source>22</maven.compiler.source>
                <maven.compiler.target>22</maven.compiler.target>					<maven.compiler.compilerVersion>22</maven.compiler.compilerVersion>
              </properties>
            </profile>
            
          </profiles>
        ```