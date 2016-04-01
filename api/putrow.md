## PutRow
写入一行新数据，或者覆盖原有行。如果已经存在，则覆盖指定的列数据。另外您还可以执行条件写入操作。

**Request协议**

```
message PutRowRequest {
    required string table_name;
    required Row row;
    optional Condition condition;
}
```
**Request参数**
* table_name<br>
	说明：写入数据的表名。<br>
	类型：String。<br>
	长度限制：[3, 255]。<br>
	Required：是。<br>
* row<br>
	说明：写入的行数据。<br>
	类型：Row<br>
	长度限制：必须包含主键列。<br>
	Required：是。<br>
* condition<br>
	说明：写入数据的条件。<br>
	类型：Condition。<br>
	Required：否。<br>
**Response协议**
```
message PutRowResponse {
  required ConsumedCapacity consumed_capacity;
}
```
**Response参数**
* consumed_capacity<br>
	说明：本次操作消耗的服务能力单元。<br>
	类型：ConsumedCapacity。<br>
	Required：是。<br>
* Errors错误码<br>
  1. kInternalServerError<br>
Server端发生错误，Http Status Code：500。<br>
  2. kConditionalCheckFailedException<br>
请求condition参数不能满足，Http Status Code：400。<br>
  3. kProvisionedThroughputExceededException<br>
请求量太大，超过预配置吞吐设置，SDK内部会自动负责重试，Http Status Code：
400。<br>
  4. kResourceNotFoundException<br>
表不存在，或者表不是ACTIVE状态，Http Status Code: 400。
  5. kAccessDeniedException<br>
请求未包含Authorization信息或者信息不正确，Http Status Code：400。
  6. kThrottlingException<br>
服务器繁忙，无法响应请求，Http Status Code：400。

