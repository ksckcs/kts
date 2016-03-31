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
