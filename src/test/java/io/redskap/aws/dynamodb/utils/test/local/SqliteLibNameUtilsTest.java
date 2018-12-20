package io.redskap.aws.dynamodb.utils.test.local;

import com.google.common.base.Splitter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

class SqliteLibNameUtilsTest {

  @DisplayName("Testing getLibPath method")
  @ParameterizedTest(name = "When called with \"{0}\", \"{1}\", \"{2}\", \"{3}\" then returns: \"{4}\"")
  @CsvFileSource(resources = "/io/redskap/aws/dynamodb/utils/test/local/get-lib-path.csv", numLinesToSkip = 1)
  void testGetLibPath(final String osName, final String runtimeName, final String osArch, final String classPathCsv, final String expected) {
    // Given
    final List<String> classPath = Splitter.on(";").splitToList(classPathCsv);

    // When
    final String actual = SqliteLibNameUtils.getLibPath(osName, runtimeName, osArch, classPath);

    // Then
    Assertions.assertEquals(expected, actual, "Library names are not calculated properly");
  }

  @DisplayName("Testing getLibNames method")
  @ParameterizedTest(name = "When called with \"{0}\", \"{1}\" then returns: \"{2}\"")
  @CsvFileSource(resources = "/io/redskap/aws/dynamodb/utils/test/local/get-lib-names.csv", numLinesToSkip = 1)
  void testGetLibNames(final String os, final String arch, final String expectedCsv) {
    // Given
    final List<String> expected = Splitter.on(";").splitToList(expectedCsv);

    // When
    final List<String> actual = SqliteLibNameUtils.getLibNames(os, arch);

    // Then
    Assertions.assertEquals(expected, actual, "Library names are not calculated properly");
  }

  @DisplayName("Testing getOs method")
  @ParameterizedTest(name = "When called with \"{0}\", \"{1}\" then returns: \"{2}\"")
  @CsvFileSource(resources = "/io/redskap/aws/dynamodb/utils/test/local/get-os.csv", numLinesToSkip = 1)
  void testGetOs(final String osName, final String runtimeName, final String expected) {
    // When
    final String actual = SqliteLibNameUtils.getOs(osName, runtimeName);

    // Then
    Assertions.assertEquals(expected, actual, "OS name is not calculated properly");
  }

  @DisplayName("Testing getArch method")
  @ParameterizedTest(name = "When called with \"{0}\", \"{1}\" then returns: \"{2}\"")
  @CsvFileSource(resources = "/io/redskap/aws/dynamodb/utils/test/local/get-arch.csv", numLinesToSkip = 1)
  void testGetArch(final String os, final String osArch, final String expected) {
    // When
    final String actual = SqliteLibNameUtils.getArch(os, osArch);

    // Then
    Assertions.assertEquals(expected, actual, "Architecture name is not calculated properly");
  }

  @DisplayName("Testing getClassPathList method")
  @ParameterizedTest(name = "When called with \"{0}\", \"{1}\" then returns: \"{2}\"")
  @CsvFileSource(resources = "/io/redskap/aws/dynamodb/utils/test/local/get-class-path-list.csv", numLinesToSkip = 1)
  void testGetClassPathList(final String classPath, final String pathSeparator, final String expectedCsv) {
    // Given
    final List<String> expected = Splitter.on(";").splitToList(expectedCsv);

    // When
    final List<String> actual = SqliteLibNameUtils.getClassPathList(classPath, pathSeparator);

    // Then
    Assertions.assertEquals(expected, actual, "ClassPath were not split as expected");
  }
}