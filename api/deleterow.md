## DeleteRow
	Request协议
```
message DeleteRowRequest {
required string table_name;
repeated Column primary_key;
optional Condition condition;
}
```
	Request参数
	table_name
说明: 更新操作的表名。
类型：String。
长度限制：[3, 255]。
Required：是。
	primary_key
说明：更新行的主键。
类型：Column数组。
长度限制：必须包分片键和行键。
Required：是。
	condition
	说明：删除数据的条件。
	类型：Condition。
	Required：是。
	Response协议
```
message UpdateRowResponse {
required ConsumedCapacity consumed_capacity;
}
```
	Response参数
	consumed_capacity
	说明：本次操作消耗的服务能力单元。
	类型：ConsumedCapacity。
	Required：是。
	Errors错误码
	kInternalServerError
Server端发生错误，Http Status Code：500。
	kConditionalCheckFailedException
请求condition参数不能满足，Http Status Code：400。
	kProvisionedThroughputExceededException
请求量太大，超过预配置吞吐设置，SDK内部会自动负责重试，Http Status Code：
400。
	kResourceNotFoundException
表不存在，或者表不是ACTIVE状态，Http Status Code: 400。
	kAccessDeniedException
请求未包含Authorization信息或者信息不正确，Http Status Code：400。
	kThrottlingException
服务器繁忙，无法响应请求，Http Status Code：400。

