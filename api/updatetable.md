## UpdateTable

通过UpdateTable操作能够为指定table修改预配置的吞吐量，其中吞吐量的限制参见Table开发者文档。UpdateTable操作是一个异步操作，当table处于UPDATING状态时，仍然使用修改之前的吞吐量配置，操作完成后，table变为ACTIVE状态。UpdateTable只有对ACTIVE状态的table操作才会成功。

**Request协议**
```
message UpdateTableRequest {
    required string table_name;
    required ProvisionedThroughput provisioned_throughtput;
}
```
**Request参数**

* table_name

说明：表名。<br>
类型：String。<br>
长度限制：[3, 255]。<br>
Required：是。
* provisioned_throughtput

说明：代表预配置的读和写吞吐量，根据设计，能够不受限制地扩展。然而预
配置吞吐量的初始大小是有一定限制的，详情见Table系统开发者文档。<br>
类型：ProvisionedThroughput<br>
Required：是<br>
Response协议


