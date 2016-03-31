## KTS HBase Client 使用手册

应用程序除了使用java client，还可以通过HBase client访问KTS。KTS HBase client是基于官方HBase client 1.1.2版本定制开发的，接口与官方HBase API完全一致，从而实现KTS与HBase的兼容。<br>
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

**Put**

由于KTS不支持多版本以及数据过期失效(TTL)，因此org.apache.hadoop.hbase.client.Put不支持以下函数：
```
Put(byte[] row, long ts)
Put(ByteBuffer row, long ts)
Put(byte [] rowArray, int rowOffset, int rowLength, long ts)
Put add(byte [] family, byte [] qualifier, long ts, byte [] value)
Put addColumn(byte [] family, byte [] qualifier, long ts, byte [] value)
Put addImmutable(byte [] family, byte [] qualifier, long ts, byte [] value)
Put addImmutable(byte[] family, byte[] qualifier, long ts, byte[] value, Tag[] tag)
Put addImmutable(byte[] family, ByteBuffer qualifier, long ts, ByteBuffer value, Tag[] tag)
Put add(byte[] family, ByteBuffer qualifier, long ts, ByteBuffer value)
Put addColumn(byte[] family, ByteBuffer qualifier, long ts, ByteBuffer value)
Put addImmutable(byte[] family, ByteBuffer qualifier, long ts, ByteBuffer value)
Put setTTL(long ttl)
```
下面的代码向表中添加了一行数据，包含1个属性列。
```
Configuration conf = HBaseConfiguration.create();
conf.set("hbase.client.connection.impl", "org.apache.hadoop.hbase.client.KTSConnection");  
conf.set("kingsoft.services.table.endpoint", "http://ip:port");
conf.set("kingsoft.services.table.accessKey", "accessKey");
conf.set("kingsoft.services.table.secretKey", " secretKey");
try {
Connection connection  = ConnectionFactory.createConnection(conf);
byte[] rowKey = Bytes.toBytes("test_row");
byte[] family = Bytes.toBytes("test_family");
byte[] qualifier = Bytes.toBytes("test_qualifier");
byte[] value = Bytes.toBytes("test_value");
Put put = new Put(rowKey);
 put.addColumn(family, qualifier, value);
 Table table = connection.getTable(TableName.valueOf("hbase_test_table"));
 table.put(put);
System.out.println(String.format(
"put row succ, key:%s, family:%s, qualifier:%s, value:%s",
new String(rowKey), new String(family), new String(qualifier),
new String(value)));
  } catch (IOException e) {
// TODO Auto-generated catch block
 e.printStackTrace();
}
```

**Delete**

由于KTS不支持多版本以及数据过期失效(TTL)，因此org.apache.hadoop.hbase.client.Delete不支持以下函数：
```
Delete(byte [] row, long timestamp)
Delete(final byte [] rowArray, final int rowOffset, final int rowLength, long ts)
Delete deleteFamily(byte [] family)
Delete addFamily(final byte [] family)
Delete deleteFamily(byte [] family, long timestamp)
Delete addFamily(final byte [] family, final long timestamp)
Delete deleteFamilyVersion(byte [] family, long timestamp)
Delete addFamilyVersion(final byte [] family, final long timestamp)
Delete deleteColumns(byte [] family, byte [] qualifier, long timestamp)
Delete addColumns(final byte [] family, final byte [] qualifier, final long timestamp)
Delete deleteColumn(byte [] family, byte [] qualifier, long timestamp)
Delete addColumn(byte [] family, byte [] qualifier, long timestamp)
Delete setTimestamp(long timestamp)
Delete setTTL(long ttl)
```
下面的代码删除了一行数据中的指定列。
```
Configuration conf = HBaseConfiguration.create();
conf.set("hbase.client.connection.impl", "org.apache.hadoop.hbase.client.KTSConnection");  
conf.set("kingsoft.services.table.endpoint", "http://ip:port");
conf.set("kingsoft.services.table.accessKey", "accessKey");
conf.set("kingsoft.services.table.secretKey", " secretKey ");
try {
Connection connection  = ConnectionFactory.createConnection(conf);
 byte[] rowKey = Bytes.toBytes("test_row");
  byte[] family = Bytes.toBytes("test_family");
  byte[] qualifier = Bytes.toBytes("test_qualifier");
  Delete delete = new Delete(rowKey);
  delete.addColumn(family, qualifier);
  Table table = connection.getTable(TableName.valueOf("hbase_test_table"));
  table.delete(delete);
  System.out.println(String.format(
	  "delete column succ, key:%s, family:%s, qualifier:%s",
	  new String(rowKey), new String(family), new String(qualifier)));  
} catch (IOException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
}
```

**MutateRow**

传入参数org.apache.hadoop.hbase.client.Put、org.apache.hadoop.hbase.client.Delete的相关限制，请分别参考Put、Delete中的说明。<br>
下面的代码演示了向表中的一行执行更新操作。
```
Configuration conf = HBaseConfiguration.create();
conf.set("hbase.client.connection.impl", "org.apache.hadoop.hbase.client.KTSConnection");  
conf.set("kingsoft.services.table.endpoint", "http://ip:port");
conf.set("kingsoft.services.table.accessKey", "accessKey");
conf.set("kingsoft.services.table.secretKey", "secretKey");
try {
  Connection connection = ConnectionFactory.createConnection(conf);
  byte[] rowKey = Bytes.toBytes("test_row");
  byte[] family = Bytes.toBytes("test_family");
  byte[] qualifier = Bytes.toBytes("test_qualifier");
  byte[] value = Bytes.toBytes("test_value");
  Put put = new Put(rowKey);
  put.addColumn(family, qualifier, value);
  RowMutations rm = new RowMutations(rowKey);
  rm.add(put);
    Table table = connection.getTable(TableName.valueOf("hbase_test_table"));      
  table.mutateRow(rm);
    System.out.println(String.format(
	  "mutate row succ, key:%s, family:%s, qualifier:%s, value:%s",
	  new String(rowKey), new String(family), new String(qualifier),
	  new String(value)));
} catch (IOException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
}
```

**Append**

由于KTS不支持多版本以及数据过期失效(TTL)，因此org.apache.hadoop.hbase.client.Append不支持以下函数：
```
Append setTTL(long ttl)
```
下面的代码向一行的指定列执行append操作。
```
Configuration conf = HBaseConfiguration.create();
conf.set("hbase.client.connection.impl", "org.apache.hadoop.hbase.client.KTSConnection");  
conf.set("kingsoft.services.table.endpoint", "http://ip:port");
conf.set("kingsoft.services.table.accessKey", "accessKey");
conf.set("kingsoft.services.table.secretKey", " secretKey");
try {
  Connection connection  = ConnectionFactory.createConnection(conf);
  byte[] rowKey = Bytes.toBytes("test_row");
  byte[] family = Bytes.toBytes("test_family");
  byte[] qualifier = Bytes.toBytes("test_qualifier");
  byte[] value = Bytes.toBytes("test_value");
  Append append = new Append(rowKey);
  append.add(family, qualifier, value);
  Table table = connection.getTable(TableName.valueOf("hbase_test_table"));
  Result result = table.append(append);
  System.out.println(String.format(
	  "append row succ, key:%s, family:%s, qualifier:%s, value:%s",
	  new String(rowKey), new String(family), new String(qualifier),
	  new String(value)));
} catch (IOException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
}
```

**Get**

由于KTS暂不支持多版本以及数据过期失效(TTL)，因此org.apache.hadoop.hbase.client.Get不支持以下函数，若被使用，则抛出异常：
```
Get setCheckExistenceOnly(boolean checkExistenceOnly)
Get setClosestRowBefore(boolean closestRowBefore)
Get addFamily(byte [] family)
Get setTimeRange(long minStamp, long maxStamp)
Get setTimeStamp(long timestamp)
Get setMaxVersions()
Get setMaxVersions(int maxVersions)d
Get setMaxResultsPerColumnFamily(int limit)
Get setRowOffsetPerColumnFamily(int offset)
Get setFilter(Filter filter)
```
对于以下函数，org.apache.hadoop.hbase.client.Delete同样不支持，但是不会抛异常，而是选择忽略该函数的行为：
```
Get setCacheBlocks(boolean cacheBlocks)
Get setReplicaId(int Id)
```
下面的代码向表中的一行数据执行查询操作。
```
Configuration conf = HBaseConfiguration.create();
conf.set("hbase.client.connection.impl", "org.apache.hadoop.hbase.client.KTSConnection");  
conf.set("kingsoft.services.table.endpoint", "http://ip:port");
conf.set("kingsoft.services.table.accessKey", "accessKey");
conf.set("kingsoft.services.table.secretKey", " secretKey ");
try {
  Connection connection  = ConnectionFactory.createConnection(conf);
  byte[] rowKey = Bytes.toBytes("test_row");
  byte[] family = Bytes.toBytes("test_family");
  byte[] qualifier = Bytes.toBytes("test_qualifier");
  Get get = new Get(rowKey);
  Table table = connection.getTable(TableName.valueOf("hbase_test_table"));
  Result result = table.get(get);
  result.getValue(family, qualifier);
  System.out.println(String.format(
	  "get row succ, key:%s, family:%s, qualifier:%s, value:%s",
	  new String(rowKey), new String(family), new String(qualifier),
	  new String(result.getValue(family, qualifier))));
} catch (IOException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
}
```

**Exists**

传入参数org.apache.hadoop.hbase.client.Get的相关限制，请参考3.4.5节。
下面的代码演示了判断表中的一行数据是否存在。
```
Configuration conf = HBaseConfiguration.create();
conf.set("hbase.client.connection.impl", "org.apache.hadoop.hbase.client.KTSConnection");  
conf.set("kingsoft.services.table.endpoint", "http://ip:port");
conf.set("kingsoft.services.table.accessKey", "accessKey");
conf.set("kingsoft.services.table.secretKey", " secretKey");
try {
  Connection connection  = ConnectionFactory.createConnection(conf);
  byte[] rowKey = Bytes.toBytes("test_row");
  byte[] family = Bytes.toBytes("test_family");
  byte[] qualifier = Bytes.toBytes("test_qualifier");
  Get get = new Get(rowKey);
  Table table = connection.getTable(TableName.valueOf("hbase_test_table"));
  boolean exists = table.exists(get);
  System.out.println(String.format(
	  "key:%s, family:%s, qualifier:%s, exists:%s",
	  new String(rowKey), new String(family), new String(qualifier), exists));
} catch (IOException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
}
```

**GetScanner**

由于KTS不支持多版本以及数据过期失效(TTL)，因此org.apache.hadoop.hbase.client.Scan不支持以下函数：
```
Scan(byte [] startRow, Filter filter)
boolean isGetScan()
Scan addFamily(byte [] family)
Scan setTimeRange(long minStamp, long maxStamp)
Scan setTimeStamp(long timestamp)
Scan setRowPrefixFilter(byte[] rowPrefix)
Scan setMaxVersions()
Scan setMaxVersions(int maxVersions)
Scan setMaxResultsPerColumnFamily(int limit)
Scan setRowOffsetPerColumnFamily(int offset)
Scan setFilter(Filter filter)
Scan setReversed(boolean reversed)
Scan setAllowPartialResults(final boolean allowPartialResults)
Scan setRaw(boolean raw) 
```
对于以下函数，org.apache.hadoop.hbase.client.Scan同样不支持，但是不会抛异常，而是选择忽略该函数的行为：
```
Scan setMaxResultSize(long maxResultSize)
Scan setLoadColumnFamiliesOnDemand(boolean value)
Scan setCacheBlocks(boolean cacheBlocks)
Scan setCaching(int caching)
Scan setSmall(boolean small)
Scan setReplicaId(int Id)
Scan setScanMetricsEnabled(final boolean enabled)
```
下面的代码演示了如何扫描表中的数据。
```
Configuration conf = HBaseConfiguration.create();
conf.set("hbase.client.connection.impl", "org.apache.hadoop.hbase.client.KTSConnection");  
conf.set("kingsoft.services.table.endpoint", "http://ip:port");
conf.set("kingsoft.services.table.accessKey", "accessKey");
conf.set("kingsoft.services.table.secretKey", " secretKey");
try {
  Connection connection = ConnectionFactory.createConnection(conf);
  byte[] rowKey = Bytes.toBytes("test_row");
  byte[] family = Bytes.toBytes("test_family");
  byte[] qualifier = Bytes.toBytes("test_qualifier");
  Scan scan = new Scan(rowKey);
  scan.addColumn(family, qualifier);
  Table table = connection.getTable(TableName.valueOf("hbase_test_table"));
  ResultScanner resultScanner = table.getScanner(scan);
  for (Result result : resultScanner) {
	   System.out.println(String.format(
		"key:%s, family:%s, qualifier:%s, value:%s",
		new String(rowKey), new String(family), new String(qualifier),
		new String(result.getValue(family, qualifier))));
  }
} catch (IOException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
}
```

**CheckAndDelete**

下面的代码演示了判断表中的数据满足条件后，再执行删除操作。
```
Configuration conf = HBaseConfiguration.create();
conf.set("hbase.client.connection.impl", "org.apache.hadoop.hbase.client.KTSConnection");  
conf.set("kingsoft.services.table.endpoint", "http://ip:port");
conf.set("kingsoft.services.table.accessKey", "accessKey");
conf.set("kingsoft.services.table.secretKey", " secretKey");
try {
  Connection connection = ConnectionFactory.createConnection(conf);
  byte[] rowKey = Bytes.toBytes("test_row");
  byte[] family = Bytes.toBytes("test_family");
  byte[] qualifier = Bytes.toBytes("test_qualifier");
  byte[] value = Bytes.toBytes("test_value");
  Delete delete = new Delete(rowKey);
  delete.addColumn(family, qualifier);
  Table table = connection.getTable(TableName.valueOf("hbase_test_table"));
  boolean result = table.checkAndDelete(rowKey, family, qualifier, value, delete);
  System.out.println(String.format(
	  "check and delete column succ, key:%s, family:%s, qualifier:%s",
	  new String(rowKey), new String(family), new String(qualifier)));
} catch (IOException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
}
```

**CheckAndMutate**

下面的代码演示了判断表中的数据满足条件后，再执行更新操作。
```
Configuration conf = HBaseConfiguration.create();
conf.set("hbase.client.connection.impl", "org.apache.hadoop.hbase.client.KTSConnection");  
conf.set("kingsoft.services.table.endpoint", "http://ip:port");
conf.set("kingsoft.services.table.accessKey", "accessKey");
conf.set("kingsoft.services.table.secretKey", " secretKey");
try {
  Connection connection = ConnectionFactory.createConnection(conf);
  byte[] rowKey = Bytes.toBytes("test_row");
  byte[] family = Bytes.toBytes("test_family");
  byte[] qualifier = Bytes.toBytes("test_qualifier");
  byte[] value1 = Bytes.toBytes("test_value");
  byte[] value2 = Bytes.toBytes("test_value2");
  Put put = new Put(rowKey);
  put.addColumn(family, qualifier, value2);
  RowMutations rm = new RowMutations(rowKey);
  rm.add(put);
  Table table = connection.getTable(TableName.valueOf("hbase_test_table"));      
  table.checkAndMutate(rowKey, family, qualifier, CompareFilter.CompareOp.EQUAL, value1, rm);
  System.out.println(String.format(
	  "check and mutate row succ, key:%s, family:%s, qualifier:%s, value:%s",
	  new String(rowKey), new String(family), new String(qualifier),
	  new String(value2)));
} catch (IOException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
}
```

**CheckAndPut**

下面的代码演示了判断表中的数据满足条件后，再执行写入操作。
```
Configuration conf = HBaseConfiguration.create();
conf.set("hbase.client.connection.impl", "org.apache.hadoop.hbase.client.KTSConnection");  
conf.set("kingsoft.services.table.endpoint", "http://ip:port");
conf.set("kingsoft.services.table.accessKey", "accessKey");
conf.set("kingsoft.services.table.secretKey", " secretKey");
try {
  Connection connection = ConnectionFactory.createConnection(conf);
  byte[] rowKey = Bytes.toBytes("test_row");
  byte[] family = Bytes.toBytes("test_family");
  byte[] qualifier = Bytes.toBytes("test_qualifier");
  byte[] value1 = Bytes.toBytes("test_value");
  byte[] value2 = Bytes.toBytes("test_value2");
  Put put = new Put(rowKey);
  put.addColumn(family, qualifier, value2);
  Table table = connection.getTable(TableName.valueOf("hbase_test_table"));      
  table.checkAndPut(rowKey, family, qualifier, value1, put);
  System.out.println(String.format(
	  "check and put row succ, key:%s, family:%s, qualifier:%s, value:%s",
	  new String(rowKey), new String(family), new String(qualifier),
	  new String(value2)));
  Get get = new Get(rowKey);
  Result result = table.get(get);
  result.getValue(family, qualifier);
  System.out.println(String.format(
	  "get row succ, key:%s, family:%s, qualifier:%s, value:%s",
	  new String(rowKey), new String(family), new String(qualifier),
	  new String(result.getValue(family, qualifier))));
} catch (IOException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
}
```

**CoprocessorService**

不支持，抛出异常UnsupportedOperationException。

**Batch**

不支持，抛出异常UnsupportedOperationException。

**BatchCallback**

不支持，抛出异常UnsupportedOperationException。

**BatchCoprocessorService**

不支持，抛出异常UnsupportedOperationException。

**GetTableDescriptor**

不支持，抛出异常UnsupportedOperationException。

**GetWriteBufferSize**

不支持，抛出异常UnsupportedOperationException。

**Increment**

不支持，抛出异常UnsupportedOperationException。

**IncrementColumnValue**

不支持，抛出异常UnsupportedOperationException。

**SetWriteBufferSize**

不支持，抛出异常UnsupportedOperationException。

**Filters**

对于所有filter，目前暂不支持


