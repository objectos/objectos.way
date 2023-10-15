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
package objectox.css;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;
import org.testng.annotations.Test;

public class ConstantPoolTest {

  @Test
  public void pool01() throws IOException {
    ConstantPool pool;
    pool = new ConstantPool(Pool01.class.getName());

    pool.loadResource();

    assertNotNull(pool.bytes);
    assertEquals(pool.bytes.length, 737);
    assertEquals(pool.bytesIndex, 0);
    assertEquals(pool.constantPoolCount, 0);

    pool.verifyMagic();

    pool.parseConstantPoolCount();

    assertEquals(pool.constantPoolCount, 36);

    pool.parseConstantPoolIndex();

    assertNotNull(pool.constantPoolIndex);
    assertEquals(pool.constantPoolIndex.length, 36);

    List<UtilityRef> refs;
    refs = pool.findAll();

    assertEquals(refs.size(), 1);
    assertEquals(refs.get(0), new UtilityRef("objectos.css.util.Display", "BLOCK"));
  }

}