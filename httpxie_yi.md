## HTTP协议

* **Http请求头内容**

KTS规定Http请求的Header除了必要Http协议字段，还必须包含以下信息：
* X-Kws-Date : 请求发出时间，使用UTC时间，日期格式采用ISO 8601基本格式。例如20150315T092054Z。
* Content-Type : 指定数据的交流格式。
目前支持Protocol Buffer：application/binary 和json： application/json
* Authorization：授权参数，其具体生成策略参见2.5节。Table系统会对Http请求进行验证：验证Authorization信息是否正确，验证X-Kws-Date包含的时间与服务器的时间相差小于15分钟。

* **Http请求正文内容**

Table系统规定Http请求的Body部分是Table系统定义交流数据，根据交流方式为Protocol Buffer消息序列化之后的数据或者json串，Body长度不超过2MB。

* **Http响应头内容**

Table系统规定Http响应的Header必须包含以下信息：
* X-Kws-Date：响应发出时间，使用UTC时间，日期格式采用ISO 8601基本格式。例如20150315T092054Z。//TODO 计划要做
* Content-Type：数据回传格式，如 application/json
* X-Kws-Code：Api返回码，编号意义参考base.proto

* **Http响应正文内容**

Table系统规定Http响应的内容是系统定义的Protocol Buffer消息序列化之后的数据，Body长度不超过2MB。每一个Table请求消息对应一个Table响应消息，应用程序将响应内容反序列化之后，读取Table操作的结果。

* **Authorization**

在Http请求的header中添加“Authorization”头作为验证信息<br>
其格式为：<br>
**Authorization** =<br>
"[SigningAlgorithm][空格]"+ <br>
"AccessKey=[access_key],[空格]" + <br>
"SignedHeaders=[signed_headers], [空格]" +<br>
"Signature=[signature]"<br>
其中：<br>
**[SigningAlgorithm]**<br>
= "KWS-HMAC-SHA256"<br>
签名加密算法，目前仅支持HMAC SHA256

**[access_key]**<br>
用户的Access Key

**[signed_headers]**<br>
="[header_name_1];[header_name_2]....;[header_name_x]"<br>
将Headers按照name字典序升序排列，注意不能包含Host（目前不支持）和Authorization

**[signature]**<br>
=HmacSha256([SecretKey],[stringToSign])<br>
内容签名

**[SecretKey]**<br>
用户的Secret Key

**[stringToSign]**<br>
= "KWS-HMAC-SHA256" + "\n" +
[X-Kws-Date] + "\n" +
HmacSha256 ([canonical_request])

**[canonical_request]**<br>
= [HTTPRequestMethod] + "\n" +
[CanonicalURI] + "\n" +
[CanonicalQueryString] + "\n" +
[CanonicalHeaders] + "\n" +
[signed_headers] + "\n

**[HTTPRequestMethod]**<br>
	为"POST"或"GET"

**[CanonicalURI]**<br> 
请求URL中除去Endpoint之外的剩余部分。<br>
目前URL等于Endpoint，所以CanonicalURI为空

**[CanonicalQueryString]**<br>
目前为空

**[CanonicalHeaders]**<br>
= HeaderName1 + ":" + Trim (HeaderValue1) + "\n" +<br>
HeaderName1 + ":" + Trim (HeaderValue1) + "\n" + … +<br>
HeaderNameX + ":" + Trim(HeaderValueX) + "\n"<br>
按照[signed_headers]中的排序方式进行排序
