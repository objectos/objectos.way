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
package objectos.core.object;

import java.util.List;
import org.testng.TestNG;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class RunTests {
	public static void main(String[] args) {
		XmlSuite suite;
		suite = new XmlSuite();

		suite.setName("Objectos Core Object");

		XmlTest test;
		test = new XmlTest(suite);

		test.setName("All");

		test.setXmlPackages(
				List.of(
						new XmlPackage("objectos.core.object")
				)
		);

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
}
