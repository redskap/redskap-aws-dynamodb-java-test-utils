# Redskap.io Aws DynamoDB Java Test Utils [![Build Status](https://travis-ci.com/redskap/redskap-aws-dynamodb-java-test-utils.svg?branch=master)](https://travis-ci.com/redskap/redskap-aws-dynamodb-java-test-utils)

Test Utilities for developing applications with AWS DynamoDB, e.g.: initializing local testing.

Table of Contents
=================
* [Tools](#tools)
  * [Setting Up DynamoDB Local with Gradle](#setting-ip-dynamodb-local-with-gradle)
* [Initialize sqlite4java for testing](#initialize-sqlite4java-for-testing)
  * [Details](#details) 
* [Build](#build)
* [References](#references)
* [License](#license)

## Tools
The following tools are available in the utility package:
* **[Initialize sqlite4java for testing](#initialize-sqlite4java-for-testing):** Simplifies _sqlite4java_ setup for running your DynamoDB Local tests in an IDE or in any build tools without providing additional environment variables. You just need to call [`AwsDynamoDbLocalTestUtils#initSqLite()`](src/main/java/io/redskap/aws/dynamodb/utils/test/local/AwsDynamoDbLocalTestUtils.java#L23) in your setUpClass test method. You can also check the example Junit 5 test class: [`ExampleDynamoDbLocalTest`](test/main/java/io/redskap/aws/dynamodb/utils/test/local/ExampleDynamoDbLocalTest.java)  

You can add the tool as a dependency to your tests:
```gradle
dependencies {
    testCompile group: 'io.redskap.aws.dynamodb.java', name: 'redskap-aws-dynamodb-java-test-utils', version: '0.1'
}
```
**You also need to set up DynamoDB Local. Details are described the [Setting Up DynamoDB Local with Gradle](#setting-ip-dynamodb-local-with-gradle) section.**

### Setting Up DynamoDB Local with Gradle

AWS DynamoDB documentation has a detailed description about [Setting Up DynamoDB Local](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html), but it does not mention Gradle setup.

You can find a working setup in ``build.gradle`` file (with EU repositories).

For each regions, first you need to set the proper repository:
* ```gradle
  repositories {
      mavenCentral()
  
      maven {
          name "DynamoDB Local Release Repository - Asia Pacific (Mumbai) Region"
          url "https://s3.ap-south-1.amazonaws.com/dynamodb-local-mumbai/release"
      }
  }
  ```
* ```gradle
  repositories {
      mavenCentral()
    
      maven {
          name "DynamoDB Local Release Repository - Asia Pacific (Singapore) Region"
          url "https://s3-ap-southeast-1.amazonaws.com/dynamodb-local-singapore/release"
      }
  }
  ```
* ```gradle
  repositories {
      mavenCentral()

      maven {
          name "DynamoDB Local Release Repository - Asia Pacific (Tokyo) Region"
          url "https://s3-ap-northeast-1.amazonaws.com/dynamodb-local-tokyo/release"
      }
  }
  ```
* ```gradle
  repositories {
      mavenCentral()
  
      maven {
          name "DynamoDB Local Release Repository - EU (Frankfurt) Region"
          url "https://s3.eu-central-1.amazonaws.com/dynamodb-local-frankfurt/release"
      }
  }
  ```
* ```gradle
  repositories {
      mavenCentral()
   
      maven {
          name "DynamoDB Local Release Repository - South America (São Paulo) Region"
          url "https://s3-sa-east-1.amazonaws.com/dynamodb-local-sao-paulo"
      }
  }
  ```
* ```gradle
  repositories {
      mavenCentral()
  
      maven {
          name "DynamoDB Local Release Repository - US West (Oregon) Region"
          url "https://s3-us-west-2.amazonaws.com/dynamodb-local/release"
      }
  }
  ```

Then set the dependency:
```gradle
dependencies {
    testCompile group: 'com.amazonaws', name: 'DynamoDBLocal', version: '1.11.119'
}
```

You can get the version numbers from each regions:
* dynamodb-local-mumbai - https://s3.ap-south-1.amazonaws.com/dynamodb-local-mumbai/release/com/amazonaws/DynamoDBLocal/maven-metadata.xml
* dynamodb-local-singapore - https://s3-ap-southeast-1.amazonaws.com/dynamodb-local-singapore/release/com/amazonaws/DynamoDBLocal/maven-metadata.xml
* dynamodb-local-tokyo - https://s3-ap-northeast-1.amazonaws.com/dynamodb-local-tokyo/release/com/amazonaws/DynamoDBLocal/maven-metadata.xml
* dynamodb-local-frankfurt - https://s3.eu-central-1.amazonaws.com/dynamodb-local-frankfurt/release/com/amazonaws/DynamoDBLocal/maven-metadata.xml
* dynamodb-local-sao-paulo - https://s3-sa-east-1.amazonaws.com/dynamodb-local-sao-paulo/release/com/amazonaws/DynamoDBLocal/maven-metadata.xml
* dynamodb-local-oregon - https://s3-us-west-2.amazonaws.com/dynamodb-local/release/com/amazonaws/DynamoDBLocal/maven-metadata.xml

## Initialize sqlite4java for testing

The official AWS DynamoDB example shows you how to use AWS DynamoDB with Maven, but it does not contain details about Gradle setup or how to run tests from your IDE (e.g.: IntelliJ IDEA). This example provides details about these missing areas.

You only need to call [`AwsDynamoDbLocalTestUtils#initSqLite()`](src/main/java/io/redskap/aws/dynamodb/utils/test/local/AwsDynamoDbLocalTestUtils.java#L23) in test classes before you plan to use the local database. You can find detailed description below about setting up local testing.

You can also check the example Junit 5 test class: [`ExampleDynamoDbLocalTest`](test/main/java/io/redskap/aws/dynamodb/utils/test/local/ExampleDynamoDbLocalTest.java). In case you are using Junit 4 replace `@BeforeAll` and `@AfterAll` annotations with `@BeforeClass` and `@AfterClass`.    

### Details

AWS DynamoDB provides possibility to test locally and it requires `sqlite4java.library.path` system property to be set. In the [official example](https://github.com/aws-samples/aws-dynamodb-examples) it is set for the Maven project. 

If you run it from Gradle or from your IDE, local database might not start as the sqlite4java native libraries are not available. These are downloaded as a dependency of `DynamoDBLocal` artifact, but it is not visible by default. Sqlite4java loads it from the path defined by `sqlite4java.library.path` system property.  

If it is not set or not valid, you probably see something similar in the logs:
```
WARNING: [sqlite] cannot open DB[1]: com.almworks.sqlite4java.SQLiteException: [-91] cannot load library: java.lang.UnsatisfiedLinkError: no sqlite4java-linux-i386-1.0.392 in java.library.path
SEVERE: [sqlite] SQLiteQueue[]: error running job queue
com.almworks.sqlite4java.SQLiteException: [-91] cannot load library: java.lang.UnsatisfiedLinkError: no sqlite4java-linux-i386-1.0.392 in java.library.path
Caused by: java.lang.UnsatisfiedLinkError: no sqlite4java-linux-i386-1.0.392 in java.library.path
SEVERE: [sqlite] SQLiteQueue[]: stopped abnormally, reincarnation is not possible for in-memory database
```
```
WARNING: [sqlite] cannot open DB[1]: com.almworks.sqlite4java.SQLiteException: [-91] cannot load library: java.lang.UnsatisfiedLinkError: no sqlite4java-linux-amd64-1.0.392 in java.library.path
SEVERE: [sqlite] SQLiteQueue[]: error running job queue
com.almworks.sqlite4java.SQLiteException: [-91] cannot load library: java.lang.UnsatisfiedLinkError: no sqlite4java-linux-amd64-1.0.392 in java.library.path
Caused by: java.lang.UnsatisfiedLinkError: no sqlite4java-linux-amd64-1.0.392 in java.library.path
SEVERE: [sqlite] SQLiteQueue[]: stopped abnormally, reincarnation is not possible for in-memory database
```
```
WARNING: [sqlite] cannot open DB[1]: com.almworks.sqlite4java.SQLiteException: [-91] cannot load library: java.lang.UnsatisfiedLinkError: no sqlite4java-win32-x64-1.0.392 in java.library.path
SEVERE: [sqlite] SQLiteQueue[]: error running job queue
com.almworks.sqlite4java.SQLiteException: [-91] cannot load library: java.lang.UnsatisfiedLinkError: no sqlite4java-win32-x64-1.0.392 in java.library.path
Caused by: java.lang.UnsatisfiedLinkError: no sqlite4java-win32-x64-1.0.392 in java.library.path
SEVERE: [sqlite] SQLiteQueue[]: stopped abnormally, reincarnation is not possible for in-memory database
```
```
WARNING: [sqlite] cannot open DB[1]: com.almworks.sqlite4java.SQLiteException: [-91] cannot load library: java.lang.UnsatisfiedLinkError: no sqlite4java-win32-x86-1.0.392 in java.library.path
SEVERE: [sqlite] SQLiteQueue[]: error running job queue
com.almworks.sqlite4java.SQLiteException: [-91] cannot load library: java.lang.UnsatisfiedLinkError: no sqlite4java-win32-x86-1.0.392 in java.library.path
Caused by: java.lang.UnsatisfiedLinkError: no sqlite4java-win32-x86-1.0.392 in java.library.path
SEVERE: [sqlite] SQLiteQueue[]: stopped abnormally, reincarnation is not possible for in-memory database
```
```
WARNING: [sqlite] cannot open DB[1]: com.almworks.sqlite4java.SQLiteException: [-91] cannot load library: java.lang.UnsatisfiedLinkError: no sqlite4java-osx-1.0.392 in java.library.path
SEVERE: [sqlite] SQLiteQueue[]: error running job queue
com.almworks.sqlite4java.SQLiteException: [-91] cannot load library: java.lang.UnsatisfiedLinkError: no sqlite4java-osx-1.0.392 in java.library.path
Caused by: java.lang.UnsatisfiedLinkError: no sqlite4java-osx-1.0.392 in java.library.path
SEVERE: [sqlite] SQLiteQueue[]: stopped abnormally, reincarnation is not possible for in-memory database
```

[`AwsDynamoDbLocalTestUtils`](src/main/java/io/redskap/aws/dynamodb/utils/test/local/AwsDynamoDbLocalTestUtils.java) class that can be used for initializing this property for DynamoDB at runtime, so it will work from both your IDE and from Gradle.

You only need to call [`AwsDynamoDbLocalTestUtils#initSqLite()`](src/test/java/io/redskap/java/aws/dynamodb/example/local/testing/AwsDynamoDbLocalTestUtils.java#L23) before you plan to use the local database. If the property is already initialized, the call will skip initialization.

There is an example test, that uses the local database. The test calls the initialization at the `@BeforeAll` setUp method [`SimpleCrudTest`](src/test/java/io/redskap/java/aws/dynamodb/example/local/testing/SimpleCrudTest.java#L28)

## Build

To build the example type the following command:

```bash
./gradlew clean build
```

Do not forget to set `JAVA_HOME` environment variable.

## References

* Gradle Build Tool: https://gradle.org/
* AWS DynamoDB documentation / Setting Up DynamoDB Local: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html 
* AWS DynamoDB local testing example with Maven: https://github.com/aws-samples/aws-dynamodb-examples

## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```