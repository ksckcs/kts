## 错误码

HTTP状态码可以指明某一操作是否成功。状态码有两类：客户端异常（4xx）和服务器异常（5xx）。如果状态码为200，则表明操作成功。
下面列出了KTS返回的错误，某些错误只需要重试同一请求即可结局。此表指出了哪些错误只需要连续重试就有可能解决。如果“重试”列为“是”，则请重新提交同一请求。如果“重试”列为“否”，请先在客户端修复问题，然后再提交请求。

Http状态码	| 错误码 | 原因 | 重试
---- | ---- | ---- | ----
400	| kAccessDeniedException | 请求未包含Authorization信息或者信息不正确 | 否
400	| kProvisionedThroughputExceededException | 请求速率过高 | 是
400	| kResourceInUseException | 资源被占用或者不可用，如表正在创建 | 否
400	| kResourceNotFoundException | 请求的表不存在 | 否
400	| kThrottlingException | 服务器繁忙，无法响应请求 | 是
400	| kValidationException | 请求缺少必要的参数、超出范围、数据类型不匹配 | 否
400	| kConditionCheckException | 解析条件异常 | 否
403	| kAccessDeniedException | 常规身份验证失败 | 否
403	| kLimitExceededException | 超过允许的上限值（一个账户的表的数目，管理类操作的数量限制）  | 否
500	| kInternalServerError | 服务器在处理请求时出现错误 | 是
503	| kServiceUnavailableException | 服务器在处理请求时出现意外错误 | 是
