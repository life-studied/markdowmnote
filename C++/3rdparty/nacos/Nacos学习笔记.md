# Nacos学习笔记

## 1.为什么需要Nacos

​	Nacos是一个服务注册中心和配置中心。什么是服务注册中心详见：[微服务介绍](https://blog.csdn.net/weixin_45203607/article/details/121886232)

​	配置中心的好处在于，如果在代码中直接写死其它服务的地址，如果那个服务挂掉就不方便更改。但是使用Nacos可以动态配置服务地址等配置信息，保证能动态获取到服务的地址等作用。

## 2.Nacos常见功能

* 注册服务
* 反注册服务（下线服务）
* 服务发现
* 心跳机制

实现原理详见：[Nacos服务注册中心原理总结](https://blog.csdn.net/qq_34700865/article/details/118760601)

## 3.C++ Nacos SDK及教程

见Nacos的官方Github：[nacos-sdk-cpp/README_zh_CN.md at master · nacos-group/nacos-sdk-cpp (github.com)](https://github.com/nacos-group/nacos-sdk-cpp/blob/master/README_zh_CN.md)

## 4.项目封装的Nacos接口（NacosClient.h）	

​	注意：Nacos不支持Windows。因此在服务从Windows开发转移到Linux运行时，Nacos会需要特别关注可能出现的问题。

```C++
/**
 * 定义一个Nacos客户端，用于封装Nacos常用操作
 * 注意：Nacos目前不支持Windows版本，
 * 所以在Windows下面使用Nacos配置其实就是读取本地配置文件，并且不支持Windows服务注册功能
 */
class NacosClient
{
private:
	//服务器地址
	string m_address;
	//命名空间
	string m_namespace;

#ifdef LINUX
	//当前命名服务
	NamingService* namingSvc;
	//注册实例ID
	std::string instanceId;
#endif
public:
	//************************************
	// Method:    NacosClient
	// FullName:  NacosClient::NacosClient
	// Access:    public 
	// Returns:   
	// Qualifier: 构造初始化
	// Parameter: const string& address 服务器地址，如：39.99.114.126:8848
	// Parameter: const string& namespaceId 命名空间，如：1653f775-4782-46ad-9cd2-b60155a574c6
	//************************************
	NacosClient(const string& address, const string& namespaceId);
	
	// 析构释放资源
	~NacosClient();

	//************************************
	// Method:    getConfig
	// FullName:  NacosClient::getConfig
	// Access:    public 
	// Returns:   std::string
	// Qualifier: 获取yaml配置
	// Parameter: const string& dataId 配置ID
	// Parameter: const string& groupName 分组名称，默认值：DEFAULT_GROUP
	//************************************
	YAML::Node getConfig(const string& dataId, const string& groupName = "DEFAULT_GROUP");

	//************************************
	// Method:    getConfigText
	// FullName:  NacosClient::getConfigText
	// Access:    public 
	// Returns:   std::string
	// Qualifier: 获取文本配置
	// Parameter: const string & dataId 配置ID
	// Parameter: const string & groupName 分组名称，默认值：DEFAULT_GROUP
	//************************************
	std::string getConfigText(const string& dataId, const string& groupName = "DEFAULT_GROUP");

	//************************************
	// Method:    registerInstance
	// FullName:  NacosClient::registerInstance
	// Access:    public 
	// Returns:   bool 注册成功返回true
	// Qualifier: 注册服务
	// Parameter: const string & ip 注册服务IP地址
	// Parameter: int port 注册服务端口
	// Parameter: const string & serviceName 注册服务名称
	// Parameter: const string & groupName 分组名称，默认值：DEFAULT_GROUP
	// Parameter: const string & clusterName 集群名称，默认值：DEFAULT
	//************************************
	bool registerInstance(const string& ip, int port, const string& serviceName, const string& groupName = "DEFAULT_GROUP", const string& clusterName = "DEFAULT");
	
	//************************************
	// Method:    deregisterInstance
	// FullName:  NacosClient::deregisterInstance
	// Access:    public 
	// Returns:   bool 反注册成功返回true
	// Qualifier: 反注册服务
	// Parameter: const string & ip 注册服务IP地址
	// Parameter: int port 注册服务端口
	// Parameter: const string & serviceName 注册服务名称
	// Parameter: const string & groupName 分组名称，默认值：DEFAULT_GROUP
	// Parameter: const string & clusterName 集群名称，默认值：DEFAULT
	//************************************
	bool deregisterInstance(const string& ip, int port, const string& serviceName, const string& groupName = "DEFAULT_GROUP", const string& clusterName = "DEFAULT");
};
#endif // _NACOSCLIENT_H_

```

