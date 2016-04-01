## GetRow
根据给定的主键读取单行的数据。默认情况下执行最终一致性读，可以通过请求参数指定强一致性读，强一致性读操作总是返回最近更新的value。

**Request协议**

```
message GetRowRequest {
    required string table_name;
    repeated Column primary_key;
    repeated String columns_to_get;
    optional bool is_strong_consistent_read;
}
```
**Request参数**

* table_name

说明：写入数据的表名。<br>
类型：String。<br>
长度限制：[3, 255]。<br>
Required：是。

* primary_key

说明：待查询的单行数据的主键。<br>
类型：Column数组。<br>
长度限制：必须包含分片键和行键。<br>
Required：是。

* columns_to_get

说明：待查询的列名。<br>
类型：String数组。<br>
长度限制：[0, 256]，如果长度为0，则返回该行的所有列；如果指定的列不存
在，则不会返回该列的数据。如果给了重复的列名，返回结果只会包含一次该
列。<br>
Required：否。<br>

* is_strong_consistent_read

说明：用于设置是否执行强一致性读操作。<br>
类型：boolean。<br>
Required：否。<br>

**Response协议**

```
message GetRowResponse {
    required Row row;
    required ConsumedCapacity consumed_capacity;
}
```
**Response参数**

* row
说明：返回的各列数据。<br>
类型：Row。<br>
Required：是
如果该行不存在，primary_key和attribute_columns均为空
* consumed_capacity

说明：本次操作消耗的服务能力单元。<br>
类型：ConsumedCapacity。<br>
Required：是。<br>
* Errors错误码

kInternalServerError<br>
Server端发生错误，Http Status Code：500。
kLimitExceededException<br>
操作请求超限，Http Status Code：400。
kProvisionedThroughputExceededException<br>
请求量太大，超过预配置吞吐设置，SDK内部会自动负责重试，Http Status Code：
400。
kResourceNotFoundException<br>
表不存在，或者表不是ACTIVE状态，Http Status Code: 400。
kAccessDeniedException<br>
请求未包含Authorization信息或者信息不正确，Http Status Code：400。
kThrottlingException<br>
服务器繁忙，无法响应请求，Http Status Code：400。

