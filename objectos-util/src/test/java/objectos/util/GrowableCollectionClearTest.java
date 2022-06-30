/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.util;

import static org.testng.Assert.assertEquals;

final class GrowableCollectionClearTest {

  private final GrowableCollection<Thing> it;

  private final AssertContents assertContents;

  public GrowableCollectionClearTest(GrowableCollection<Thing> it,
                                     AssertContents assertContents) {
    this.it = it;
    this.assertContents = assertContents;
  }

  public void execute() {
    assertEquals(it.size(), 0);

    it.clear();
    assertContents.execute();

    var arrayList = Thing.nextArrayList();

    it.addAll(arrayList);
    it.clear();
    assertContents.execute();
  }

}