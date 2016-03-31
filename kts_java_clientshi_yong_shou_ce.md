## KTS Java Client使用手册


**环境准备**

1. 配置Java 7以上开发环境；
2. 下载KTS Java Client：
 http://kingsoft-table-service.ks3-cn-beijing.ksyun.com/sdk/ksyun-kts-client-0.9.2.zip <br>或者添加Maven依赖：
```
<dependency>
<groupId>com.kingsoft.services.table</groupId>
	<artifactId>kts-client</artifactId>
	<version>0.9.1</version>
</dependency>
```

**获取密钥**

1. 开通KTS服务；
2. 获取AK/SK。进入控制台：http://ks3.ksyun.com/console.html#/， 点击页面左侧”账号设置”，获取AccessKey、SecretKey。

**表操作**

**CreateTable**

通过CreateTable操作，您可以向账户内添加一个新表。对每个账户而言，表名在region内必须唯一，也就是说您可以在不同的region内创建相同名字的表。创建表时需指定表的partition-key和row-key（可以没有）的数据类型，一旦表创建后，其类型不可再更改。<br>
CreateTable是一个异步操作，正常情况下，当Table系统收到CreateTable请求时，会立即返回一个CREATING的状态结果。当表创建完成后，系统会设置表状态为ACTIVE。您只能对ACTIVE的表发起读写操作。您可以通过DescribeTable操作来检查表的状态信息。
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
//Create table
CreateTableRequest request = new CreateTableRequest();
request.setTableName(tableName);
request.setPartitionKeyType(ColumnType.STRING);
request.setRowKeyType(null);
request.setProvisionedThroughput(new ProvisionedThroughput(4000, 4000));
CreateTableResult result = ktsClient.createTable(request);
```

**DeleteTable**

通过DeleteTable操作能够删除指定的table及其数据。<br>
删除操作是一个异步操作，正常情况下，当Table系统收到DeleteTable请求后，会立即返回一个DELETING的状态信息。<br>
只有当table是ACTIVE状态时，才能成功执行DeleteTable请求。当table是CREATING或者UPDATING状态时，返回ResourceInUseException。当请求的table不存在时，返回ResourceNotFoundException。当table已经是DELETING状态时，返回成功。<br>
需要注意的是，当table是DELETING状态时，系统可能还会接受读写请求，直到删除操作彻底完成。
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
//Delete table
DeleteTableResult result = ktsClient.deleteTable(tableName);
```

**ListTables**

通过ListTables操作能够获取当前账户在当前Endpoint下的所有表的信息。
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
//list table
List<TableInfo> infos = ktsClient.listTables();
```

**UpdateTable**

通过UpdateTable操作能够为指定table修改预配置的吞吐量，其中吞吐量的限制参见预配置吞吐章节。UpdateTable操作是一个异步操作，当table处于UPDATING状态时，仍然使用修改之前的吞吐量配置，操作完成后，table变为ACTIVE状态。UpdateTable只有对ACTIVE状态的table操作才会成功。
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
//Update Table
UpdateTableRequest request = new UpdateTableRequest();
request.setTableName(tableName);
request.setProvisionedThroughput(new ProvisionedThroughput(1000, 1000));
UpdateTableResult result = ktsClient.updateTable(request);
```
**DescribeTable**

通过DescribeTable操作能够检索指定table的基本信息：状态、创建时间、主键类型和预配置吞吐量信息。
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
//describe table
DescribeTableResult result = ktsClient.describeTable(tableName);
```

**数据操作**

**PutRow**

写入一行新数据，或者覆盖原有行。如果已经存在，则覆盖指定的列数据。<br>
单行数据不能超过<font color='#f00'>400KB</font>，请求的行数据不能为空。


