/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code.internal;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JavaModelTest {

  @Test
  public void checkPackageName() {
    JavaModel.checkPackageName("");
    JavaModel.checkPackageName("com.example");
    JavaModel.checkPackageName("objectos.code");
    JavaModel.checkPackageName("java.lang");
    JavaModel.checkPackageName("java.lang.annotation");
    JavaModel.checkPackageName("ありがとう");
    JavaModel.checkPackageName("ありがとう.はい");
    JavaModel.checkPackageName("กระทำ");
    JavaModel.checkPackageName("กระทำ.กรกฎาคม");
    JavaModel.checkPackageName("等待");
    JavaModel.checkPackageName("等待.知道");
    JavaModel.checkPackageName("等待.知道.隧道");

    try {
      JavaModel.checkPackageName("丽丸乁");
      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }
  }

  @Test
  public void checkSimpleName() {
    try {
      JavaModel.checkSimpleName("");
      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }

    JavaModel.checkSimpleName("Hey");
    JavaModel.checkSimpleName("Foo");
    JavaModel.checkSimpleName("Bar");

    try {
      JavaModel.checkSimpleName("com.example.Hey");
      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }
  }

}