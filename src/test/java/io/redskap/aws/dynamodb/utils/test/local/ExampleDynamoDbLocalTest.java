package io.redskap.aws.dynamodb.utils.test.local;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ExampleDynamoDbLocalTest {

  private static AmazonDynamoDBLocal amazonDynamoDBLocal;

  @BeforeAll
  public static void setUpClass() {
    AwsDynamoDbLocalTestUtils.initSqLite();

    amazonDynamoDBLocal = DynamoDBEmbedded.create();
  }

  @Test
  void when_ExampleTestCase_then_Success() {
    // Test case that using dynamoDB instance. Can be also initialized in a setUp method.
    final AmazonDynamoDB client = amazonDynamoDBLocal.amazonDynamoDB();
    final DynamoDB dynamoDb = new DynamoDB(client);

    // etc.
  }

  @AfterAll
  public static void tearDownClass() {
    amazonDynamoDBLocal.shutdown();
  }
}
