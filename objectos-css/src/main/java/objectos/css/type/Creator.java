/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.type;

public interface Creator {

  void createAngle(AngleUnit unit, double value);

  void createAngle(AngleUnit unit, int value);

  void createColor(String value);

  void createDouble(double value);

  void createInt(int value);

  void createKeyword(String name);

  void createLength(LengthUnit unit, double value);

  void createLength(LengthUnit unit, int value);

  void createPercentage(double value);

  void createPercentage(int value);

  void createRgb(double r, double g, double b);

  void createRgb(double r, double g, double b, double alpha);

  void createRgb(int r, int g, int b);

  void createRgb(int r, int g, int b, double alpha);

  void createRgba(double r, double g, double b, double alpha);

  void createRgba(int r, int g, int b, double alpha);

  void createString(String value);

  void createUri(String value);

}