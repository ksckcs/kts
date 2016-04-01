## BatchGetRow
批量读取一个或者多个表中的若干行数据。BatchGetRow请求可以视为多个GetRow操
作集合，各个操作独立执行，独立返回结果，独立计算服务能力消耗单位。与执行大量的GetRow操作相比，使用BatchGetRow操作可以有效减少请求的响应时间，提高数据的读取速率。<br>
一次BatchGetRow操作最多能够获取16MB的数据(最大100行数据)。

**Request协议**

```
message BatchGetRowRequest {
  required string table_name;
  repeated String columns_to_get;
  optional bool is_strong_consistent_read;
  required repeated GetRowsRequest rows;
}
```

**Request参数**
* **table_name**<br>
类型: string<br>
描述: 该表的表名<br>
* **columns_to_get**<br>
说明：待查询的列名。<br>
类型：String数组。<br>
长度限制：[0, 256]，如果长度为0，则返回该行的所有列；如果指定的列不存	在，则不会返回该列的数据。如果给了重复的列名，返回结果只会包含一次该列。<br>
Required：否。
* **is_strong_consistent_read**<br>
说明：用于设置是否执行强一致性读操作。<br>
类型：boolean。<br>
Required：否。<br>
* **rows**<br>
说明：批量的GetRow请求。<br>
类型：RowInBatchGetRowRequest数组。<br>
长度限制：[1, 100]。<br>
Required：是。<br>
若请求中出现了下述情况，则操作整体失败，返回错误。<br>
  * 任一行未指定主键、主键名称不符合规范或者主键类型不正确。<br>
  * 任两行包含主键完全相同的行。<br>
  * RowInBatchGetRowRequest总个数超过100个。<br>
  * 不包含任何RowInBatchGetRowRequest。<br>

**Response协议**

```
message BatchGetRowResponse {
    required string table_name;
    required repeated RowInBatchGetRowResponse rows；
}
```

**Response参数**

* **table_name**<br>
类型: string<br>
描述: 该表的表名。<br>
* **rows**<br>
说明：读取到的全部数据<br>
类型：RowInBatchGetRowResponse数组。<br>
长度限制：[1, 100]。<br>
Required：是<br>
* **Errors错误码**<br>
**kInternalServerError**<br>
Server端发生错误，Http Status Code：500。<br>
**kProvisionedThroughputExceededException**<br>
请求量太大，超过预配置吞吐设置，SDK内部会自动负责重试，Http Status Code：
400。<br>
**kResourceNotFoundException**<br>
表不存在，或者表不是ACTIVE状态，Http Status Code: 400。<br>
**kAccessDeniedException**<br>
请求未包含Authorization信息或者信息不正确，Http Status Code：400。<br>
**kThrottlingException**<br>
服务器繁忙，无法响应请求，Http Status Code：400。

