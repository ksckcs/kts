## API

**CreateTable**

通过CreateTable操作，您可以向账户内添加一个新表，在账户体系内，表名在每个region内必须唯一，也就是说您可以在不同的region内创建相同名字的表。CreateTable是一个异步操作，正常情况下，当Table系统收到CreateTable请求时，会立即返回一个CREATING的状态结果。当表创建完成后，系统会设置表状态为ACTIVE，您只能对ACTIVE的表发起读写操作。您可以通过DescribeTable操作来检查表的状态信息。
* Request协议

```
message CreateTableRequest {
  required string table_name;
  required repeated ColumnSchema primary_key;
  required ProvisionedThroughput provisioned_throughtput;
}
```


