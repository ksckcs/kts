## DescribeTable

通过DescribeTable操作能够检索指定table的基本信息：状态、创建时间、主键和预配置吞吐量信息。

**Request协议**
```
message DescribeTableRequest {
    required string table_name;
}
```
**Request参数**

* **table_name**<br>
说明：表名。<br>
类型：String。<br>
长度限制：[3, 255]。<br>
Required：是。<br>

**Response协议**

```
message DescribeTableResponse {
  optional TableDescription table;
}
```

**Response参数**

* **table**<br>
说明：代表table的属性信息。<br>
类型：TableDescription。<br>
Required：否。

* **Errors错误码**<br>
**kInternalServerError**<br>
Server端发生错误，Http Status Code：500。<br>
**kLimitExceededException**<br>
操作请求超限，Http Status Code：400。<br>
**kResourceNotFoundException**<br>
查看不存在的table，Http Status Code：400。<br>
**kAccessDeniedException**<br>
请求未包含Authorization信息或者信息不正确，Http Status Code：400。



