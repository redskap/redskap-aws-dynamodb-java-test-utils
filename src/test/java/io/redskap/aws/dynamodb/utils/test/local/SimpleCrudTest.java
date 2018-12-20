package io.redskap.aws.dynamodb.utils.test.local;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleCrudTest {

  private static AmazonDynamoDBLocal amazonDynamoDBLocal;
  private SimpleCrud simpleCrud;

  @BeforeAll
  public static void setUpClass() {
    // Removing property set by other test cases, to avoid interference with other tool related test cases
    System.clearProperty("sqlite4java.library.path");

    AwsDynamoDbLocalTestUtils.initSqLite();

    amazonDynamoDBLocal = DynamoDBEmbedded.create();

    // Init DB table
    new SimpleCrud(createDocumentInterfaceClient()).initDb(new ProvisionedThroughput(1L, 1L));
  }

  @AfterAll
  public static void tearDownClass() {
    // Removing property set by other test cases, to avoid interference with other tool related test cases
    System.clearProperty("sqlite4java.library.path");

    amazonDynamoDBLocal.shutdown();
  }

  private static DynamoDB createDocumentInterfaceClient() {
    final AmazonDynamoDB client = amazonDynamoDBLocal.amazonDynamoDB();
    return new DynamoDB(client);
  }

  @BeforeEach
  public void setUp() {
    simpleCrud = new SimpleCrud(createDocumentInterfaceClient());
  }


  @Test
  public void when_StoreValueIsCalled_then_CanBeRetrieved() {
    // Given
    final int key = 10;
    final String value = "TEN";
    final String expected = value;

    // When
    simpleCrud.storeValue(10, value);

    // Then
    final String actual = simpleCrud.retrieveValue(key);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void when_RetrieveValueCalledWithNotStoredKey_then_ExceptionIsThrown() {
    // Given
    final int key = 15;

    // Then
    Assertions.assertThrows(IllegalStateException.class, () -> simpleCrud.retrieveValue(key), "Excepted exception is not thrown for invalid key");
  }

  private static class SimpleCrud {

    public static final String TABLE_NAME = "SimpleTable";

    private final DynamoDB dynamoDb;
    private final Table table;

    public SimpleCrud(final DynamoDB dynamoDb) {
      this.dynamoDb = dynamoDb;
      this.table = dynamoDb.getTable(TABLE_NAME);
    }

    public void initDb(final ProvisionedThroughput provisionedThroughput) {
      dynamoDb.createTable(new CreateTableRequest(TABLE_NAME, Lists.newArrayList(new KeySchemaElement("id", KeyType.HASH)))
              .withAttributeDefinitions(new AttributeDefinition("id", ScalarAttributeType.N))
              .withProvisionedThroughput(provisionedThroughput));
    }

    public void storeValue(final int key, final String value) {
      table.putItem(new Item().withPrimaryKey("id", key).withString("value", value));
    }

    public String retrieveValue(final int key) {
      final Item item = table.getItem("id", key);
      if (item == null) {
        throw new IllegalStateException("Value does not exist for key: " + key);
      }
      return item.getString("value");
    }
  }
}
