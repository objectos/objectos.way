/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import java.util.List;
import java.util.stream.Stream;
import org.testng.TestNG;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class RunTests {
  public static void main(String[] args) {
    XmlSuite suite;
    suite = new XmlSuite();

    suite.setName("Objectos Way");

    test(suite, "Objectos Lang", pkgs(
      "objectos.lang",
      "objectox.lang"
    ));

    test(suite, "Objectos Util", pkgs(
      "objectos.util"
    ));

    test(suite, "Objectos HTML", pkgs(
      "objectos.html",
      "objectos.html.internal",
      "objectos.html.tmpl"
    ));

    test(suite, "Objectos CSS", pkgs(
      "objectos.css",
      "objectos.css.util",
      "objectox.css",
      "objectox.css.util"
    ));

    test(suite, "Objectos HTTP", pkgs(
      "objectos.http",
      "objectos.http.util",
      "objectox.http"
    ));

    TestNG ng;
    ng = new TestNG();

    ng.setOutputDirectory(args[0]);

    ng.setXmlSuites(
      List.of(suite)
    );

    ng.setVerbose(2);

    ng.run();

    System.exit(ng.getStatus());
  }

  private static void test(XmlSuite suite, String testName, List<XmlPackage> pkgs) {
    XmlTest test;
    test = new XmlTest(suite);

    test.setName(testName);

    test.setXmlPackages(pkgs);
  }

  private static List<XmlPackage> pkgs(String... names) {
    return Stream.of(names)
        .map(XmlPackage::new)
        .toList();
  }
}
