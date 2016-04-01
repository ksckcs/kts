## DeleteTable

通过DeleteTable操作能够删除指定的table及其数据。删除操作是一个异步操作，正常情况下，当Table系统收到DeleteTable请求后，会立即返回一个DELETING的状态信息。只有当table是ACTIVE状态时，才能成功执行DeleteTable请求。当table是CREATING或者UPDATING状态时，返回kResourceInUseException。当请求的table不存在时，返回kResourceNotFoundException。当table已经是DELETING状态时，返回成功。需要注意的是，当table是DELETING状态时，系统可能还会接受读写请求，直到删除操作彻底完成。

**Request协议**
```
message DeleteTableRequest {
    required string table_name;
}
```
**Request参数**

* table_name

说明：需要删除的表名。<br>
类型：String。<br>
长度限制：[3,255]。<br>
Required：是。

**Response协议**
```
message DeleteTableResponse {
    optional TableDescription table_description;
}
```
** Response参数**

* table_description

说明：代表table的属性信息。<br>
类型：TableDescription。<br>
Required：否。<br>
* Errors错误码
  1. kInternalServerError<br>
Server端发生错误，Http Status Code：500。
  2. kLimitExceededException<br>
并发的table操作请求超限，Http Status Code：400。
  3. kResourceInUseException<br>
table的并发访问冲突，比如删除CREATING或者UPDATING状态的table，
Http Status Code：400
  4. kResourceNotFoundException<br>
删除不存在的table，Http Status Code：400
  5. kAccessDeniedException<br>
请求未包含Authorization信息或者信息不正确，Http Status Code：400。
