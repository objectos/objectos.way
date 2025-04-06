/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

public class MediaTypeTest {

  @Test
  public void testBasicTextPlainWithCharset() {
    Media.Type type = Media.Type.parse("text/plain; charset=utf-8");
    assertEquals(type.type(), "text", "Main type should be 'text'");
    assertEquals(type.subtype(), "plain", "Subtype should be 'plain'");
    assertEquals(type.param("charset"), "utf-8", "Charset parameter should be 'utf-8'");
    assertNull(type.param("boundary"), "Non-existent parameter should return null");
  }

  @Test
  public void testTextPlainNoParameters() {
    Media.Type type = Media.Type.parse("text/plain");
    assertEquals(type.type(), "text", "Main type should be 'text'");
    assertEquals(type.subtype(), "plain", "Subtype should be 'plain'");
    assertNull(type.param("charset"), "Charset should be null when not specified");
  }

  @Test
  public void testCaseInsensitivity() {
    Media.Type type = Media.Type.parse("TEXT/HTML; CHARSET=UTF-8");
    assertEquals(type.type(), "text", "Main type should be lowercase 'text'");
    assertEquals(type.subtype(), "html", "Subtype should be lowercase 'html'");
    assertEquals(type.param("charset"), "UTF-8", "Charset parameter should preserve case");
  }

  @Test
  public void testMultipleParameters() {
    Media.Type type = Media.Type.parse("text/csv; charset=utf-8; boundary=xyz");
    assertEquals(type.type(), "text");
    assertEquals(type.subtype(), "csv");
    assertEquals(type.param("charset"), "utf-8");
    assertEquals(type.param("boundary"), "xyz");
  }

  @Test
  public void testQuotedParameter() {
    Media.Type type = Media.Type.parse("text/plain; charset=\"utf-8\"");
    assertEquals(type.type(), "text");
    assertEquals(type.subtype(), "plain");
    assertEquals(type.param("charset"), "utf-8", "Quotes should be stripped");
  }

  @Test
  public void testWhitespaceHandling() {
    Media.Type type = Media.Type.parse("  text/plain  ;  charset  =  utf-8  ");
    assertEquals(type.type(), "text");
    assertEquals(type.subtype(), "plain");
    assertEquals(type.param("charset"), "utf-8");
  }

  @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "s == null")
  public void testNullInput() {
    Media.Type.parse(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testEmptyInput() {
    Media.Type.parse("");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testInvalidFormatNoSlash() {
    Media.Type.parse("text");
  }

  @Test
  public void testComplexSubtype() {
    Media.Type type = Media.Type.parse("application/vnd.api+json");
    assertEquals(type.type(), "application");
    assertEquals(type.subtype(), "vnd.api+json");
  }

  @Test
  public void testParameterWithNoValue() {
    Media.Type type = Media.Type.parse("text/plain; charset=");
    assertEquals(type.type(), "text");
    assertEquals(type.subtype(), "plain");
    assertEquals(type.param("charset"), "", "Empty parameter value should return empty string");
  }

  @Test
  public void testMultipleSemicolons() {
    Media.Type type = Media.Type.parse("text/plain;;;charset=utf-8;;");
    assertEquals(type.type(), "text");
    assertEquals(type.subtype(), "plain");
    assertEquals(type.param("charset"), "utf-8");
  }

  @Test(enabled = false)
  public void testWildcardType() {
    Media.Type type = Media.Type.parse("*/*");
    assertEquals(type.type(), "*");
    assertEquals(type.subtype(), "*");
  }

}