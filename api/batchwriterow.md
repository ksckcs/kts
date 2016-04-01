## BatchWriteRow

批量插入，修改或删除一个或多个表中的若干行数据。BatchWriteRow 操作可视为多个 PutRow、UpdateRow、DeleteRow 操作的集合，各个操作独 立执行，独立返回结果，独立计算服务能力单元。与执行大量的单行写操作相比，使用 BatchWriteRow操作可以有效减少请求的响应时间，提高数据的写入速率。

**Request协议**

```
message BatchWriteRowRequest {
    required string table_name;
    repeated PutRowInBatchWriteRowRequest put_rows;
    repeated UpdateRowInBatchWriteRowRequest update_rows;
    repeated DeleteRowInBatchWriteRowRequest delete_rows;
}
```

**Request参数**
* **table_name**<br>
类型: string<br>
描述: 该表的表名。<br>
* **put_rows**<br>
类型: repeated PutRowInBatchWriteRowRequest<br>
描述: 该表中请求插入的行信息。<br>
* **update_rows**<br>
类型: repeated UpdateRowInBatchWriteRowRequest<br>
描述: 该表中请求更新的行信息。<br>
* **delete_rows**<br>
类型: repeated DeleteRowInBatchWriteRowRequest<br>
描述: 该表中请求删除的行信息。<br>
	若put_rows、update_rows进而delete_rows中出现下情况都会返回整体错误:<br>
任一行操作未指定主键、主键列名称不符合规范或者主键列类型不正确。<br>
任一属性列名称不符合列名命名规范。<br>
任一行操作存在与主键列同名的属性列。<br>
任一主键列或者属性列的值大小超过上限。<br>
任意两行主键完全相同。<br>
行操作个数超过25个，或者其含有的总数据大小超过1M。<br>
没有包含行操作,则返回 这个返回值不正确。<br>
	Response协议
message BatchWriteRowResponse {
required string table_name;
repeated RowInBatchWriteRowResponse put_rows;
repeated RowInBatchWriteRowResponse update_rows;
    repeated RowInBatchWriteRowResponse delete_rows;
}
	Response参数
	table_name
类型: string
描述: 该表的表名
	put_rows
类型: RowInBatchWriteRowResponse
描述: 该表中PutRow操作的结果
	update_rows
类型: RowInBatchWriteRowResponse
描述: 该表中UpdateRow操作的结果
	delete_rows
类型: RowInBatchWriteRowResponse
描述: 该表中DeleteRow操作的结果
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
