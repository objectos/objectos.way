/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

import objectos.way.dev.Script012;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class Script012Test {

  @Test
  public void action() {
    assertEquals(
        Script012.ACTION.toString(),

        """
        [["EI",[["JS","subject"]]],["MO",[["EI",[["JS","src"]]],["PR","Element","innerHTML"],["TY","string"]]]]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/012");

      var clickMe = page.locator("#click-me");

      var subjectTitle = page.locator("#subject .title");
      var subjectContents = page.locator("#subject .contents");

      assertThat(subjectTitle).hasText("SUBJECT");
      assertThat(subjectContents).hasText("Before");

      clickMe.click();

      assertThat(subjectTitle).hasText("SUBJECT");
      assertThat(subjectContents).hasText("After");
    }
  }

}
