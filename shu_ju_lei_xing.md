# 数据类型

ColumnType
ColumnValue
Column
ActionType
ColumnUpdate
Row
Condition
CompareType
ConsumedCapacity
ProvisionedThroughtput
TableStatus
TableDescription
GetRowsRequest
GetRowsResponse
WriteRowsRequest
WriteRowsResponse


3.1	ColumnType
```
enum ColumnType {
  kBoolean = 0;
  kInt32 = 1;
  kInt64 = 2;
  kDouble = 3;
  kString = 4;
  kBytes = 5;
} 
```
枚举类型，用于表示系统支持的数据类型。
3.2	ColumnValue
```
message ColumnValue {
    required ColumnType column_type = 1;
    optional bool bool_value = 2;
    optional int32 int32_value = 3;
    optional int64 int64_value = 4;
    optional double double_value = 5;
    optional string string_value = 6;
    optional bytes bytes_value = 7;
}
```
	用于表示一列的列值，根据列的类型，有且只有一个value被设置。
	column_type
说明：列的数据类型。
	boolean_value
说明：该列的列值，只有在column_type为kBooolean时才有效。
	int32_value
说明：该列的列值，只有在column_type为kInt32时才有效。
	int64_value
说明：该列的列值，只有在column_type为kInt64时才有效。
	double_value
说明：该列的列值，只有在column_type为kDouble时才有效。
	string_value
说明：该列的列值，只有在column_type为kString时才有效。
	binary_value
说明：该列的列值，只有在column_type为kBinary时才有效。
3.3	Column
```
message Column {
    required string column_name = 1;
    required ColumnValue column_value = 2;
}
```
表示一列数据name/vaue对。
	column_name
说明：列名，在table内必须唯一。
类型：String。
长度限制：[1, 65536]。
Required：是。
	column_value
说明：列值。
类型：ColumnValue。
Required：是。
3.4	ActionType
```
enum ActionType {
kPut = 1;
kDelete = 2;
}
```
枚举类型，其中kPut表示插入一列或者覆盖该列的数据；kDelete表示删除该列的数据。 
3.5	ColumnUpdate
```
message ColumnUpdate {
    required string column_name = 1;
    required ActionType action = 2;
    optional ColumnValue column_value = 3;
}
```
在UpdateRow中，表示更新一列的信息。
	column_name
说明：该列的列名。
长度限制: [3, 65536]。
	action
说明：对该列的更新方式：kPut，kDelete。
	column_value
说明：该列更新后的列值，在type为kPut时有效。
3.6	Row
```
message Row {
    required repeated Column primary_key;
    repeated Column attribute_columns;
}
```
表示一行数据。
	primary_key
说明：主键数据。
	attribute_columns
说明：属性列数组。
3.7	Condition
```
message Condition {
    optional PrimaryKey primary_key = 1;
    required string column_name = 2;
    required CompareType compare_type = 3;
    optional ColumnValue value = 4;
}
```
Condition用于实现查询过滤器和实现条件写入功能，Condition既可以用于属性列，也可以用于主键列。
	primary_key
说明：行主键
	column_name
说明：列名
	compare_type
说明：过滤器的比较方法。
	value
说明：待比较的value列表。
3.8	CompareType
```
enum CompareType {
kLess = 0;
kLessOrEqual = 1;
kEqual= 2;
kNotEqual = 3;
kGreaterOrEqual = 4;
kGreater = 5;
kNoOp= 6;
}
```
Condition 或者Filter的操作符
3.9	ConsumedCapacity
```
message ConsumedCapacity {
    required int64 capacity_units = 1;
}
```
	capacity_units
说明：本次操作所消耗的容量单位。
3.10	ProvisionedThroughtput
```
message ProvisionedThroughput {
    optional int64 read_capacity_units = 1;
    optional int64 write_capacity_units = 2;
}
```
描述table的预配置读和写操作的吞吐量。
	read_capacity_units
说明：每秒能够执行读请求所消耗的最大单位。
	write_capacity_units
说明：每秒能够执行写请求所消耗的最大单位。
3.11	TableStatus
```
enum TableStatus {
kCreatingTable = 1;
kUpdatingTable = 2;
kDeletingTable = 3;
kActiveTable = 4;
kInActiveTable = 5;
}
```
枚举类型，用于表示table的数状态：
3.12	TableDescription
```
message TableDescription {
required string table_name;
required repeated ColumnSchema primary_key;
required string creation_date_time；
required TableStatus table_status
ProvisionedThroughput provisioned_throughtput;
long item_count;
long table_size_count
}
```
描述table的结构信息。
	table_name
说明：table的名称。
类型：String。
长度限制：[3, 255]。
Required：是。
	primary_key
说明：table的主键结构，由一个必须的partition key和一个可选的row key组成，数组第一项是partition key，第二项是row key如果有的话。
类型：Column数组。
长度限制: 数组长度最小为1，最大为2。
Required：是。
	creation_date_time
说明：table的创建时间，遵循TODO格式。
类型：String。
Required：是。
	table_status
说明：table的当前状态: { kCreating, kUpdating, kDeleting, kActive }。
类型：TableStatus。
Required：是。
	provisioned_throughput
说明：table的预配置吞吐信息。
类型：ProvisionedThroughput。
Required：否。
	item_count
说明：table的记录（行）数目，这个值不能实时反映当前的表容量，系统会每6小时进行更新。
类型：int64。
Required：否。
	table_size_bytes
说明：table的总大小，单位是字节，这个值不能实时反映当前的表容量，系统会每6小时进行更新。
类型：int64。
3.13	GetRowsRequest
```
message GetRowsRequest {
required int64 log_id = 1;
required string table_name = 2;
repeated PrimaryKey primary_keys = 3;
repeated string column_names = 4;
optional bool is_strong_consistent_read = 5 [default = false];
optional Filter filter = 6;
}
```
在BatchGetRow操作中，表示要查询的一行信息。
	primary_key
	说明：待查询的单行数据的主键。
	类型：Column数组。
	长度限制：必须包含分片键和行键。
	Required：是。
3.14	GetRowsResponse
```
message GetRowsResponse {
required int64 log_id = 1;
required int32 code = 2;
required string msg = 3;
optional string table_name = 4;
repeated Row rows = 5;
optional GetRowsRequest unprocessed_rows = 6;
optional ConsumedCapacity consumed_capacity = 7;
}
```
在BatchGetRow操作的返回消息中，表示一个表的数据结果集。
	code
类型: int32
描述: 该行操作是否成功。若为0，则该行读取成功，否则该行读取失败，row无效。
	consumed_capacity
类型: ConsumedCapacity
描述: 该行操作消耗的服务能力单元。
	rows
类型: Row
描述: 该行需要返回的列数据集合。
	unprocessed_rows
描述: 由于吞吐量限制或者系统繁忙等原因，Batch请求中某些请求没有被处理，unprocessed_rows中包含所有未处理的行的集合
3.15	WriteRowsRequest
```
message WriteRowsRequest {
    required int64 log_id = 1;
	required string table_name = 2;
	repeated PutRowRequest put_rows = 3;
	repeated UpdateRowRequest update_rows = 4;
	repeated DeleteRowRequest delete_rows = 5;
}
```
在BatchWriteRow操作中，对某一个表要进行的更新的集合。
3.16	WriteRowsResponse
```
message WriteRowsResponse {
required int64 log_id = 1;
required int32 code = 2;
required string msg = 3;
optional string table_name = 4;
optional WriteRowsRequest unprocessed_rows = 5;
optional ConsumedCapacity consumed_capacity = 6;
optional bool processed = 7;
}
```
在BatchWriteRow操作的返回消息中，表示一个操作的结果。
	unprocessed_rows
描述: 由于吞吐量限制或者系统繁忙等原因，Batch请求中某些请求没有被处理，unprocessed_rows中包含所有未处理的行的集合。
	processed
描述: 如果WriteRowsRequest请求的中Put、Update、Delete操作带有Condition，而且所有Condition都成立，那么processed值为true，否则为false。如果WriteRowsRequest中的请求不带任何condition, 那么processed的值没有任何实际意义。
