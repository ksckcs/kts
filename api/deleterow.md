## DeleteRow
**Request协议**

```
message DeleteRowRequest {
    required string table_name;
    repeated Column primary_key;
    optional Condition condition;
}
```
**Request参数**

* **table_name**<br>
说明: 更新操作的表名。<br>
类型：String。<br>
长度限制：[3, 255]。<br>
Required：是。<br>
* **primary_key**<br>
说明：更新行的主键。<br>
类型：Column数组。<br>
长度限制：必须包分片键和行键。<br>
Required：是。
* **condition**<br>
说明：删除数据的条件。<br>
类型：Condition。<br>
Required：是。
    
**Response协议**

```
message UpdateRowResponse {
    required ConsumedCapacity consumed_capacity;
}
```

**Response参数**

* **consumed_capacity**<br>
说明：本次操作消耗的服务能力单元。<br>
类型：ConsumedCapacity。<br>
Required：是。
* **Errors错误码**<br>
**kInternalServerError**<br>
Server端发生错误，Http Status Code：500。<br>
**kConditionalCheckFailedException**<br>
请求condition参数不能满足，Http Status Code：400。<br>
**kProvisionedThroughputExceededException**<br>
请求量太大，超过预配置吞吐设置，SDK内部会自动负责重试，Http Status Code：
400。<br>
**kResourceNotFoundException**<br>
表不存在，或者表不是ACTIVE状态，Http Status Code: 400。<br>
**kAccessDeniedException**<br>
请求未包含Authorization信息或者信息不正确，Http Status Code：400。<br>
**kThrottlingException**<br>
服务器繁忙，无法响应请求，Http Status Code：400。

