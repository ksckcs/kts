## KTS HBase Client 使用手册

虽然KTS HBase client是基于官方HBase client定制开发的，并且接口与官方HBase API完全一致，但是由于KTS不支持多版本以及数据过期失效(TTL)，因此，KTS HBase client和官方HBase client存在部分差异。

**环境准备**
1. 配置Java 7以上开发环境；
2. 下载KTS HBase client：
     http://kingsoft-table-service.ks3-cn-beijing.ksyun.com/sdk/ksyun-kts-hbase-client-0.9.2.zip<br>
或者添加Maven依赖：
```xml
<dependency>
<groupId>com.kingsoft.services.table</groupId>
<artifactId>kts-hbase-client</artifactId>
<version>0.9.1</version>
</dependency>
```

**获取密钥**

1. 开通KTS服务；
2. 获取AK/SK。进入控制台：http://ks3.ksyun.com/console.html#/， 点击页面左侧”账号设置”，获取AccessKey、SecretKey。

**表操作**

所有表操作都可以在控制台执行，HBase Client暂不支持。

**数据操作**

使用HBase Client进行数据操作一般遵循以下几个步骤：
1. 创建Configuration对象，并设置hbase.client.connection.impl、kingsoft.services.table.endpoint、kingsoft.services.table.accessKey、kingsoft.services.table.secretKey；
2. 根据Configuration参数创建Connection对象；
3. 构造请求对象；
4. 获取Table对象，调用Table对象相关接口发送请求。


