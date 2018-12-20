package io.redskap.aws.dynamodb.utils.test.local;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AwsDynamoDbLocalTestUtilsTest {

  @BeforeEach
  void setUp() {
    // Removing property set by other test cases, to avoid interference with other tool related test cases
    System.clearProperty("sqlite4java.library.path");
  }

  @AfterEach
  void tearDown() {
    // Removing property set by other test cases, to avoid interference with other tool related test cases
    System.clearProperty("sqlite4java.library.path");
  }

  @Test
  void when_SystemPropertyIsNotSet_then_Updated() {
    // Given
    final String expected = "path value";

    // When
    AwsDynamoDbLocalTestUtils.initSqLite(() -> expected);

    // Then
    Assertions.assertEquals(expected, System.getProperty("sqlite4java.library.path"), "System property for sqlit4java is not set");
  }

  @Test
  void when_SystemPropertyIsAlreadySet_then_NotUpdated() {
    // Given
    final String expected = "Old value";
    System.setProperty("sqlite4java.library.path", "Old value");

    // When
    AwsDynamoDbLocalTestUtils.initSqLite(() -> "New value");

    // Then
    Assertions.assertEquals(expected, System.getProperty("sqlite4java.library.path"), "System property for sqlit4java is not set");
  }
}