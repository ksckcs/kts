## CreateTable
****

通过CreateTable操作，您可以向账户内添加一个新表，在账户体系内，表名在每个region内必须唯一，也就是说您可以在不同的region内创建相同名字的表。CreateTable是一个异步操作，正常情况下，当Table系统收到CreateTable请求时，会立即返回一个CREATING的状态结果。当表创建完成后，系统会设置表状态为ACTIVE，您只能对ACTIVE的表发起读写操作。您可以通过DescribeTable操作来检查表的状态信息。

**Request协议**

```
message CreateTableRequest {
    required string table_name;
    required repeated ColumnSchema primary_key;
    required ProvisionedThroughput provisioned_throughtput;
}
```

**Request参数**

* **table_name**

说明：表名。<br>
类型：String。<br>
长度限制：[3, 255]。<br>
Required：是。<br>

* **primary_key**

说明：表的主键，由一个必需的partition key和一个可选的row key组成。<br>
数组中第一个值为partition key，第二个为row key（可选）<br>
类型：ColumnDescription的数组。
长度限制：[1, 2]。<br>
Required：是。<br>

* **provisioned_throughtput**

说明：代表预配置的读和写吞吐量，根据设计，能够不受限制地扩展。然而预
配置吞吐量的初始大小是有一定限制的，详情见Table系统开发者文档。并且
能够通过UpdateTable操作来修改。<br>
类型：ProvisionedThroughput。<br>
Required：是。<br>

**Response协议**
```
message CreateTableResponse {
    optional TableDescription table_description;
}
```

**Response参数**

* **table_description**

说明：代表table的属性信息。<br>
类型：TableDescription。<br>
Required：否。<br>

* **Errors错误码**

  1. kInternalServerError<br>
  服务端发生了错误，Http Status Code：500。
  2. kLimitExceededException<br>
  并发的table操作请求超限，Http Status Code：400。
  3. kResourceInUseException<br>
  table的并发访问冲突，比如创建已经存在的表，Http Status Code：400。
  4. kAccessDeniedException<br>
  请求未包含Authorization信息或者信息不正确，Http Status Code：400。


