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
package objectos.css.util;

import objectos.css.tmpl.Api;
import objectox.css.InternalLength;
import objectox.css.LengthUnit;

public sealed abstract class Length
    implements Api.LengthValue
    permits InternalLength {

  protected Length() {}

  public static Length ch(double value) {
    return InternalLength.of(value, LengthUnit.CH);
  }

  public static Length ch(int value) {
    return InternalLength.of(value, LengthUnit.CH);
  }

  public static Length cm(double value) {
    return InternalLength.of(value, LengthUnit.CM);
  }

  public static Length cm(int value) {
    return InternalLength.of(value, LengthUnit.CM);
  }

  public static Length em(double value) {
    return InternalLength.of(value, LengthUnit.EM);
  }

  public static Length em(int value) {
    return InternalLength.of(value, LengthUnit.EM);
  }

  public static Length ex(double value) {
    return InternalLength.of(value, LengthUnit.EX);
  }

  public static Length ex(int value) {
    return InternalLength.of(value, LengthUnit.EX);
  }

  public static Length in(double value) {
    return InternalLength.of(value, LengthUnit.IN);
  }

  public static Length in(int value) {
    return InternalLength.of(value, LengthUnit.IN);
  }

  public static Length mm(double value) {
    return InternalLength.of(value, LengthUnit.MM);
  }

  public static Length mm(int value) {
    return InternalLength.of(value, LengthUnit.MM);
  }

  public static Length pc(double value) {
    return InternalLength.of(value, LengthUnit.PC);
  }

  public static Length pc(int value) {
    return InternalLength.of(value, LengthUnit.PC);
  }

  public static Length pt(double value) {
    return InternalLength.of(value, LengthUnit.PT);
  }

  public static Length pt(int value) {
    return InternalLength.of(value, LengthUnit.PT);
  }

  public static Length px(double value) {
    return InternalLength.of(value, LengthUnit.PX);
  }

  public static Length px(int value) {
    return InternalLength.of(value, LengthUnit.PX);
  }

  public static Length q(double value) {
    return InternalLength.of(value, LengthUnit.Q);
  }

  public static Length q(int value) {
    return InternalLength.of(value, LengthUnit.Q);
  }

  public static Length rem(double value) {
    return InternalLength.of(value, LengthUnit.REM);
  }

  public static Length rem(int value) {
    return InternalLength.of(value, LengthUnit.REM);
  }

  public static Length vh(double value) {
    return InternalLength.of(value, LengthUnit.VH);
  }

  public static Length vh(int value) {
    return InternalLength.of(value, LengthUnit.VH);
  }

  public static Length vmax(double value) {
    return InternalLength.of(value, LengthUnit.VMAX);
  }

  public static Length vmax(int value) {
    return InternalLength.of(value, LengthUnit.VMAX);
  }

  public static Length vmin(double value) {
    return InternalLength.of(value, LengthUnit.VMIN);
  }

  public static Length vmin(int value) {
    return InternalLength.of(value, LengthUnit.VMIN);
  }

  public static Length vw(double value) {
    return InternalLength.of(value, LengthUnit.VW);
  }

  public static Length vw(int value) {
    return InternalLength.of(value, LengthUnit.VW);
  }

}