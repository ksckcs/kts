package samples;

import java.util.List;

import com.kingsoft.services.ClientOptions;
import com.kingsoft.services.auth.BasicKWSCredentials;
import com.kingsoft.services.auth.KWSCredentials;
import com.kingsoft.services.table.KTSClient;
import com.kingsoft.services.table.KTSClientImpl;
import com.kingsoft.services.table.comparator.DataValueComparator;
import com.kingsoft.services.table.filter.CompareType;
import com.kingsoft.services.table.filter.Condition;
import com.kingsoft.services.table.filter.FilterList;
import com.kingsoft.services.table.filter.FilterList.Operator;
import com.kingsoft.services.table.filter.PrefixFilter;
import com.kingsoft.services.table.filter.SingleColumnValueFilter;
import com.kingsoft.services.table.model.CreateTableRequest;
import com.kingsoft.services.table.model.CreateTableResult;
import com.kingsoft.services.table.model.DeleteRowRequest;
import com.kingsoft.services.table.model.DeleteRowResult;
import com.kingsoft.services.table.model.DeleteTableResult;
import com.kingsoft.services.table.model.DescribeTableResult;
import com.kingsoft.services.table.model.GetRowRequest;
import com.kingsoft.services.table.model.GetRowResult;
import com.kingsoft.services.table.model.PutRowRequest;
import com.kingsoft.services.table.model.PutRowResult;
import com.kingsoft.services.table.model.ScanRequest;
import com.kingsoft.services.table.model.ScanResult;
import com.kingsoft.services.table.model.UpdateRowRequest;
import com.kingsoft.services.table.model.UpdateRowResult;
import com.kingsoft.services.table.model.UpdateTableRequest;
import com.kingsoft.services.table.model.UpdateTableResult;
import com.kingsoft.services.table.model.batch.BatchGetRowRequest;
import com.kingsoft.services.table.model.batch.BatchGetRowResult;
import com.kingsoft.services.table.model.batch.BatchWriteRowRequest;
import com.kingsoft.services.table.model.batch.BatchWriteRowResult;
import com.kingsoft.services.table.model.batch.GetRequestsInTable;
import com.kingsoft.services.table.model.batch.GetResultsInTable;
import com.kingsoft.services.table.model.batch.WriteRequestsInTable;
import com.kingsoft.services.table.model.common.ColumnType;
import com.kingsoft.services.table.model.common.ColumnValue;
import com.kingsoft.services.table.model.common.PrimaryKey;
import com.kingsoft.services.table.model.common.ProvisionedThroughput;
import com.kingsoft.services.table.model.common.Row;
import com.kingsoft.services.table.model.common.TableInfo;

public class KTSSample {

  public static void main(String[] args) {
    manageTable();
    simpleDataOperation();
    batchOperation();
    advanceDataOperation();
  }

  private static void manageTable() {
    String ktsEndpoint = "http://101.71.19.7";
    String ktsAccessKey = "myAK";
    String ktsSecretKey = "mySK";
    KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey,
        ktsSecretKey);
    ClientOptions options = new ClientOptions();

    KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);

    String tableName = "table-1";

    // Create table
    {
      CreateTableRequest request = new CreateTableRequest();
      request.setTableName(tableName);
      request.setPartitionKeyType(ColumnType.STRING);
      request.setRowKeyType(null);
      request.setProvisionedThroughput(new ProvisionedThroughput(4000, 4000));
      CreateTableResult result = ktsClient.createTable(request);
    }

    // Delete table
    {
      DeleteTableResult result = ktsClient.deleteTable(tableName);
    }

    // List Tables
    {
      List<TableInfo> infos = ktsClient.listTables();
    }

    // Update Table
    {
      UpdateTableRequest request = new UpdateTableRequest();
      request.setTableName(tableName);
      request.setProvisionedThroughput(new ProvisionedThroughput(1000, 1000));
      UpdateTableResult result = ktsClient.updateTable(request);
    }

    // describe table
    {
      DescribeTableResult result = ktsClient.describeTable(tableName);
    }
  }

  private static void batchOperation() {
    String ktsEndpoint = "http://101.71.19.7";
    String ktsAccessKey = "myAK";
    String ktsSecretKey = "mySK";
    KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey,
        ktsSecretKey);
    ClientOptions options = new ClientOptions();

    KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);

    String tableName = "table-1";

    // Batch Write row
    {
      BatchWriteRowRequest request = new BatchWriteRowRequest();

      // table 1
      WriteRequestsInTable table1Changes = new WriteRequestsInTable("table-1");
      // delete row-1
      PrimaryKey key1 = new PrimaryKey("primary-key-1", null);
      table1Changes.addDeleteChange(key1);
      // put row-2
      Row row = new Row();
      row.setPrimaryKey(new PrimaryKey("primary-key-2", null));
      row.addColumn("column-1", 2);
      table1Changes.addPutChange(row);

      request.addRequestsInTable(table1Changes);

      // you can also add request in table 2, 3
      // ...

      BatchWriteRowResult result = ktsClient.batchWriteRow(request);

      // 检查批处理里面的所有请求是否全部成功执行
      assert (result.getUnprocessedRows().size() == 0);
    }

    // Batch get row
    {
      BatchGetRowRequest batchGetRequest = new BatchGetRowRequest();

      GetRequestsInTable table1Request = new GetRequestsInTable();
      table1Request.setTableName(tableName);
      table1Request.setStrongConsistent(true);
      table1Request.addPrimaryKey(new PrimaryKey("primary-key-1", null));
      table1Request.addPrimaryKey(new PrimaryKey("primary-key-2", null));
      batchGetRequest.addRequestRowsEntry(table1Request);

      // you can also add request in table 2, 3
      // ...

      BatchGetRowResult result = ktsClient.batchGetRow(batchGetRequest);
      // read rows in table-1
      GetResultsInTable rows = result.getTableResponseRows(tableName);
      for (Row row : rows.getRows()) {
        System.out.println("row value:" + row.toString());
      }

      // 检查批处理里面的所有请求是否全部成功执行
      assert (result.getUnprocessedKeysSet().size() == 0);
    }

    // Scan
    {
      ScanRequest request = new ScanRequest();
      request.setTableName(tableName);

      // 设置scan起始点和终点(可选)
      request.setInclusiveStartKey(new PrimaryKey("start-key", null));
      request.setExclusiveEndKey(new PrimaryKey("end-key", null));

      request.setLimit(1000);// 最多返回1000行
      request.setStrongConsistent(false);

      while (true) {
        ScanResult result = ktsClient.scan(request);
        System.out.println("rows data:" + result.getRows());
        if (result.getNextStartKey() != null) {// 继续Scan
          request.setInclusiveStartKey(result.getNextStartKey());
          continue;
        } else {// 完成Scan
          break;
        }
      }
    }
  }

  private static void simpleDataOperation() {
    String ktsEndpoint = "http://101.71.19.7";
    String ktsAccessKey = "myAK";
    String ktsSecretKey = "mySK";
    KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey,
        ktsSecretKey);
    ClientOptions options = new ClientOptions();

    KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);

    String tableName = "table-1";

    // Put row
    {
      Row row = new Row();
      row.setPrimaryKey(new PrimaryKey("row-pk-1", null));
      row.addColumn("colunn-1", ColumnValue.fromInt(1));
      row.addColumn("colunn-2", ColumnValue.fromString("value2"));

      PutRowRequest request = new PutRowRequest(tableName, row);
      PutRowResult result = ktsClient.putRow(request);
    }

    // Update row
    {
      UpdateRowRequest request = new UpdateRowRequest();
      request.setTableName(tableName);
      request.setPrimaryKey(new PrimaryKey("primary-key-1", null));
      // delete column-1
      request.deleteColumn("column-1");
      // put a value to column-2
      request.addColumn("column-2", ColumnValue.fromInt(1));

      UpdateRowResult result = ktsClient.updateRow(request);
    }

    // Delete row
    {
      DeleteRowRequest request = new DeleteRowRequest();
      request.setTableName(tableName);
      request.setPrimaryKey(new PrimaryKey("primary-key-1", null));
      DeleteRowResult result = ktsClient.deleteRow(request);
    }

    // Get row
    {
      GetRowRequest request = new GetRowRequest();
      request.setTableName(tableName);

      // 指定需要获取哪些列，不指定的情况下默认获取所有列
      request.addColumnName("column-1");
      request.addColumnName("column-2");

      request.setPrimaryKey(new PrimaryKey("primary-key-1", null));

      // 设置是否强一致性获取
      request.setStrongConsistent(true);

      GetRowResult result = ktsClient.getRow(request);

      if (!result.getRow().isEmpty()) { // 如果行确实存在
        ColumnValue v = result.getRow().getColumn("column-1");
        if (v != null) {
          System.out.println("column-1 value:" + v.asInt());
        } else {
          System.out.println("column-1 not exist");
        }
      } else { // 这一行数据不存在
        System.out.println("row primary-key-1 not exist");
      }
    }
  }

  public static void advanceDataOperation() {
    String ktsEndpoint = "http://101.71.19.7";
    String ktsAccessKey = "myAK";
    String ktsSecretKey = "mySK";
    KWSCredentials credentials = new BasicKWSCredentials(ktsAccessKey,
        ktsSecretKey);
    ClientOptions options = new ClientOptions();

    KTSClient ktsClient = new KTSClientImpl(ktsEndpoint, credentials, options);

    String tableName = "table-1";

    // Condition delete row
    {
      // prepare data
      Row row = new Row();
      row.setPrimaryKey(new PrimaryKey("row-1", null));
      row.addColumn("age", ColumnValue.fromInt(18));
      PutRowRequest putRequest = new PutRowRequest(tableName, row);
      PutRowResult putResult = ktsClient.putRow(putRequest);

      // delete this row if age == 18
      DeleteRowRequest request = new DeleteRowRequest();
      request.setPrimaryKey(new PrimaryKey("row-1", null));
      request.setTableName(tableName);

      Condition condition = new Condition();
      condition.setPrimaryKey(new PrimaryKey("row-1", null));
      condition.setOperator(CompareType.EQUAL);
      condition.setColumnName("age");
      condition.setValue(ColumnValue.fromInt(18));
      request.setCondition(condition);

      DeleteRowResult result = ktsClient.deleteRow(request);
      assert (result.isProcessed() == true);
    }

    // Filter
    {
      PrefixFilter innerFilter1 = new PrefixFilter();
      innerFilter1.setPartitionKeyPrefix("luo_");

      SingleColumnValueFilter innerFilter2 = new SingleColumnValueFilter();
      innerFilter2.setColumnName("status");
      innerFilter2.setCompareType(CompareType.EQUAL);
      innerFilter2
          .setComparator(new DataValueComparator(ColumnValue.fromInt(1)));

      FilterList filter = new FilterList();
      filter.setOperator(Operator.AND);
      filter.addFilter(innerFilter1);
      filter.addFilter(innerFilter2);

      ScanRequest request = new ScanRequest();
      request.setTableName(tableName);
      request.setFilter(filter);

      ScanResult reuslt = ktsClient.scan(request);
    }
  }

  
}
