# wsl开机自启动脚本

## windows开机启动wsl

步骤：

* win+R
* 输入shell:startup
* 创建脚本init.vbs

```vbscript
set ws=wscript.CreateObject("wscript.shell")
ws.run "wsl -d Ubuntu", 0
```

## wsl开机启动自定义脚本

### 创建脚本，并移动到/etc/init.d/下

```shell
sudo mv test.sh /etc/init.d/
```

### 添加权限

```shell
cd /etc/init.d/
sudo chmod 777 test.sh
```

### 添加到启动脚本中

```shell
sudo update-rc.d test.sh defaults
```

