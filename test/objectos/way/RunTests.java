/*
 * Copyright (C) 2025 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.testng.IAnnotationTransformer;
import org.testng.TestNG;
import org.testng.annotations.ITestAnnotation;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class RunTests {

  public static final class TimeoutSetter implements IAnnotationTransformer {

    @SuppressWarnings({"exports", "rawtypes"})
    @Override
    public void transform(
        ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
      if (testMethod == null) {
        return;
      }

      annotation.setTimeOut(TimeUnit.SECONDS.toMillis(10));
    }
  }

  public static void main(String[] args) {
    // Use GMT-3 for all tests
    final TimeZone gmtMinusThree;
    gmtMinusThree = TimeZone.getTimeZone("GMT-3");

    TimeZone.setDefault(gmtMinusThree);

    XmlSuite suite;
    suite = new XmlSuite();

    suite.setName("Objectos Way");

    XmlTest test;
    test = new XmlTest(suite);

    test.setName("All");

    test.setXmlPackages(
        List.of(
            new XmlPackage("objectos.*"),
            new XmlPackage("objectox.*"),
            new XmlPackage("testing.*")
        )
    );

    TestNG ng;
    ng = new TestNG();

    if (args.length > 0) {
      ng.setOutputDirectory(args[0]);
    }

    ng.setXmlSuites(
        List.of(suite)
    );

    ng.run();

    System.exit(ng.getStatus());
  }
}
