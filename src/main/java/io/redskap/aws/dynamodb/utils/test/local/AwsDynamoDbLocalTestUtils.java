package io.redskap.aws.dynamodb.utils.test.local;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;

/**
 * Helper class for initializing AWS DynamoDB to run with sqlite4java for local testing.
 *
 * <p>Created from: https://github.com/redskap/aws-dynamodb-java-example-local-testing
 */
public class AwsDynamoDbLocalTestUtils {

  /**
   * Static helper class.
   */
  private AwsDynamoDbLocalTestUtils() {
  }

  /**
   * Sets the sqlite4java library path system parameter if it is not set already.
   */
  public static void initSqLite() {
    initSqLite(() -> {
      final List<String> classPath = SqliteLibNameUtils.getClassPathList(System.getProperty("java.class.path"), File.pathSeparator);

      final String libPath = SqliteLibNameUtils.getLibPath(System.getProperty("os.name"), System.getProperty("java.runtime.name"),
              System.getProperty("os.arch"), classPath);

      return libPath;
    });
  }

  /**
   * Sets the sqlite4java library path system parameter if it is not set already.
   *
   * @param libPathSupplier Calculates lib path for sqlite4java.
   */
  public static void initSqLite(final Supplier<String> libPathSupplier) {
    if (System.getProperty("sqlite4java.library.path") == null) {
      System.setProperty("sqlite4java.library.path", libPathSupplier.get());
    }
  }

}
