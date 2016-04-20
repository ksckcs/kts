## Scan
根据指定主键的范围遍历整个table，同时也支持设置condition来条件过滤数据。如果扫描的数据超过1MB，则Scan停止扫描，返回已经扫描得到的结果（有可能超过1MB）以及last_evaluated_key。Scan操作是最终一致性读操作。

**Request协议**

```
message ScanRequest {
    required string table_name;
    string columns_to_get;
    repeated Column exclusive_start_key;
    repeated Column inclusive_end_key;
    int32 limit;
    Condition condition;
}
```
**Request参数**

* **table_name**<br>
说明: 更新操作的表名。<br>
类型：String。<br>
长度限制：[3, 255]。<br>
Required：是。<br>
* **columns_to_get**<br>
说明：本次范围查询的列名。<br>
类型：String数组。<br>
长度限制：[1, 256]。<br>
Required：否。<br>
* **inclusive_start_key**<br>
说明：本次范围查询的起始主键，查询的结果可能包含这个主键对应的记录。<br>
类型：Column数组。<br>
Required：否。<br>
* **exclusive_end_key**<br>
说明：本次范围查询的终止主键，查询结果一定不包含这个主键对应的记录。<br>
类型：Column数组。<br>
Required：否。<br>
* **limit**<br>
说明：本次范围查询最大返回行数。
类型：int32, 必须大于0。
Required：否。
* **condition**<br>
说明：本次范围查询的过滤条件。<br>
类型：Condition。<br>
Required：否。<br>

**Response协议**

```
message ScanResponse {
    repeated Row rows;
    repeated Column next_start_key;
    required ConsumedCapacity consumed_capacity;
}
```

**Response参数**

* **rows**<br>
说明：本次范围查询结果集,按照行组织。<br>
类型：Row数组。<br>
Required：否。<br>
* **last_evaluated_key**<br>
说明：本次范围查询操作的最后一个主键。<br>
类型：Column。<br>
Required：否。<br>
* **consumed_capacity**<br>
说明：本次范围查询所消耗的服务能力单元。<br>
类型：ConsumedCapacity。<br>
Required：是。<br>
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


