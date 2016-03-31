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
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
//Put row
Row row = new Row();
row.setPrimaryKey(new PrimaryKey("row-pk-1", null));
row.addColumn("colunn-1", ColumnValue.fromInt(1));
row.addColumn("colunn-2", ColumnValue.fromString("value2"));
PutRowRequest request = new PutRowRequest(tableName, row);
PutRowResult result = ktsClient.putRow(request);
```

**UpdateRow**

更新指定行的数据，如果该行不存在，则新增一行；若该行存在，则根据请求的内容在这一行中新增、修改或者删除指定的列数据。不允许对同一列执行多种更新操作。<br>
单行数据不能超过<font color='#f00'>400KB</font>，请求的行数据不能为空。
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
//Update row
UpdateRowRequest request = new UpdateRowRequest();
request.setTableName(tableName);
request.setPrimaryKey(new PrimaryKey("primary-key-1", null));
//delete column-1
request.deleteColumn("column-1");
//put a value to column-2
request.addColumn("column-2", ColumnValue.fromInt(1));
UpdateRowResult result = ktsClient.updateRow(request);
```

**DeleteRow**

删除指定行的数据。
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
//Delete row
DeleteRowRequest request = new DeleteRowRequest();
request.setTableName(tableName);
request.setPrimaryKey(new PrimaryKey("primary-key-1", null));
DeleteRowResult result = ktsClient.deleteRow(request);
```

**GetRow**

根据给定的主键读取单行的数据。<br>
可指定是否强一致性读取，强一致性读操作总是返回最近更新的value。默认情况下为否，即采用强一致性读。<br>
只返回存在的各列数据，如果该行不存在，row对象中column集合大小为0。
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
//Get row
GetRowRequest request = new GetRowRequest();
request.setTableName(tableName);
request.setPrimaryKey(new PrimaryKey("primary-key-1", null));
//指定需要获取哪些列，不指定的情况下默认获取所有列
request.addColumnName("column-1");
request.addColumnName("column-2");		  
//设置是否强一致性获取
request.setStrongConsistent(true);	  
GetRowResult result = ktsClient.getRow(request);		  
if (result.getRow().getColumns().size() > 0) { //如果行确实存在
ColumnValue v = result.getRow().getColumn("column-1");
if (v != null) {
System.out.println("column-1 value:" + v.asInt());
} else {
System.out.println("column-1 not exist");
}
} else { //这一行数据不存在
System.out.println("row primary-key-1 not exist");
}
```

**BatchWriteRow**

批量插入，修改或删除一个或多个表中的若干行数据。BatchWriteRow 操作可视为多个 PutRow、UpdateRow、DeleteRow 操作的集合，各个操作独 立执行，独立返回结果，独立计算服务能力单元。与执行大量的单行写操作相比，使用 BatchWriteRow操作可以有效减少请求的响应时间，提高数据的写入速率。<br>
若putRows、updateRows进而deleteRows中出现下情况都会返回整体错误:
1. 任意一个表名不合法。
2. 任意一列名称不符合列名命名规范。
3. 任意一行操作未指定主键、主键列的名称不符合规范或者主键列的类型不正确。
4. 任一主键列或者属性列的值大小超过上限。
5. 行操作个数超过100个，或者为0，或者其含有的总数据大小超过1M。
6. 同一行存在重复列。
7. 任意一个table不存在。
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
BatchWriteRowRequest request = new BatchWriteRowRequest();
// table 1
WriteRequestsInTable table1Changes = new WriteRequestsInTable("table-1");
// delete row-1
PrimaryKey key1 = new PrimaryKey("primary-key-1", null);
      table1Changes.addDeleteChange(key1);
// put row-2
Row row = new Row();
row.setPrimaryKey(new PrimaryKey("primary-key-2", null));
row.addColumn("column-1", 2);
table1Changes.addPutChange(row);
 request.addRequestsInTable(table1Changes);
 // you can also add request in table 2, 3
      // ...
  BatchWriteRowResult result = ktsClient.batchWriteRow(request);
 // 检查批处理里面的所有请求是否全部成功执行
assert (result.getUnprocessedRows() == null);
```

**BatchGetRow**

批量读取一个或者多个表中的若干行数据。BatchGetRow请求可以视为多个GetRow操
作集合，各个操作独立执行，独立返回结果，独立计算服务能力消耗单位。与执行大量的GetRow操作相比，使用BatchGetRow操作可以有效减少请求的响应时间，提高数据的读取速率。<br>
一次BatchGetRow操作最多请求100行数据。若请求中出现了下述情况，则操作整体失败:
1. 任意一行未指定主键、主键名称不符合规范或者主键类型不正确。
2. get_rows请求总行数超过100或者等于0。
3. 只要有一个table不存在，就整体失败。
4. 存在多个同一行同一列的请求。
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
BatchGetRowRequest batchGetRequest = new BatchGetRowRequest();
GetRequestsInTable table1Request = new GetRequestsInTable();
table1Request.setTableName(tableName);
table1Request.setStrongConsistent(true);
table1Request.addPrimaryKey(new PrimaryKey("primary-key-1", null));
table1Request.addPrimaryKey(new PrimaryKey("primary-key-2", null));
batchGetRequest.addRequestRowsEntry(table1Request);
// you can also add request in table 2, 3
// ...
BatchGetRowResult result = ktsClient.batchGetRow(batchGetRequest);
// read rows in table-1
GetResultsInTable rows = result.getTableResponseRows(tableName);
for (Row row : rows.getRows()) {
 System.out.println("row value:" + row.toString());
}
```
**Scan**

根据指定主键的范围遍历整个table。如果扫描的数据超过1MB，则Scan停止扫描，返回已经扫描得到的结果（有可能超过1MB）以及nextStartKey。<br>
Scan操作默认是最终一致性读操作。如果不提供列名，则返回该行所有列的数据。
```
String ktsEndpoint = "http://101.71.19.7";
String ktsAccessKey = "myAK";
String ktsSecretKey = "mySK";
String tableName = "table-1";
//Initialize client obj
KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey, ktsSecretKey);
ClientOptions options = new ClientOptions();
KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);
//Scan
ScanRequest request = new ScanRequest();
request.setTableName(tableName);     
//设置scan起始点和终点(可选)
request.setInclusiveStartKey(new PrimaryKey("start-key", null));
request.setExclusiveEndKey(new PrimaryKey("end-key", null));
request.setLimit(1000);//最多返回1000行
request.setStrongConsistent(false);     
while(true) {
ScanResult result = ktsClient.scan(request);
  System.out.println("rows data:" + result.getRows());
  if (result.getNextStartKey() != null) {//继续Scan
request.setInclusiveStartKey(result.getNextStartKey());
continue;
  } else {//完成Scan
break;
}
}
```






