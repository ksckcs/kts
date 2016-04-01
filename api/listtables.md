## ListTables

通过ListTables操作能够获取当前账户在当前Endpoint下的所有表名。
Request协议
```
message ListTablesRequest {
    required string exclusive_start_table_name;
    required int limit;
}
```
**Request参数**

* exclusive_start_table_name

说明：启始table名称，用于分批返回。<br>
类型：String<br>
长度限制：[3,255]。<br>
Required：否。
* limit

说明：一次返回的table的数目，如果没有设置，则默认是100。<br>
类型：Int<br>
Required：否。

**Response协议**

```
message ListTablesResponse {
    required string last_evaluated_table_name;
    repeated string table_names;
}
```
**Response参数**

* last_evaluated_table_name

说明：当前返回结果页中最后一个table的名称。<br>
类型：String<br>
长度限制：[3,255]。<br>
Required：是。
* table_names

说明：当前Account在当前Endpoint中的table名称列表，一次返回最大100
个table。<br>
类型：String数组<br>
Required：是。
* Errors错误码

  1. kInternalServerError<br>
Server端发生错误，Http Status Code：500。
  2. kLimitExceededException<br>
操作请求超限，Http Status Code：400。
  3. kAccessDeniedException<br>
请求未包含Authorization信息或者信息不正确，Http Status Code：400。
