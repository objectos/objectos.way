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
package objectox.http.media;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import objectos.lang.Stage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StaticFilesStageTest {

  @DataProvider
  public Iterator<Stage> stageProvider() {
    return EnumSet.allOf(Stage.class).iterator();
  }

  @Test(dataProvider = "stageProvider")
  public void toStaticFiles01(Stage stage) throws IOException {
    final StaticFilesStageBuilder builder;
    builder = new StaticFilesStageBuilder();

    final StaticFilesStage subject;
    subject = builder.build();

    try (StaticFiles res = subject.toStaticFiles(stage)) {
      assertEquals(res.stage, stage);
    }
  }

}
