/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import org.testng.annotations.Test;

public class CssGeneratorTest {

  private static abstract class AbstractSubject extends Html.Template {
    @Override
    protected final void render() {
      div(
          include(this::classes)
      );
    }

    abstract void classes();
  }

  @Test
  public void alignContent() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("content-normal content-center content-start content-end content-between content-around content-evenly content-baseline content-stretch");
      }
    }

    test(
        Subject.class,

        """
        .content-normal { align-content: normal }
        .content-center { align-content: center }
        .content-start { align-content: flex-start }
        .content-end { align-content: flex-end }
        .content-between { align-content: space-between }
        .content-around { align-content: space-around }
        .content-evenly { align-content: space-evenly }
        .content-baseline { align-content: baseline }
        .content-stretch { align-content: stretch }
        """
    );
  }

  @Test
  public void alignItems() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("items-start");
        className("items-end");
        className("items-center");
        className("items-baseline");
        className("items-stretch");
      }
    }

    test(
        Subject.class,

        """
        .items-start { align-items: flex-start }
        .items-end { align-items: flex-end }
        .items-center { align-items: center }
        .items-baseline { align-items: baseline }
        .items-stretch { align-items: stretch }
        """
    );
  }

  @Test
  public void alignSelf() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("self-auto self-start self-end self-center self-stretch self-baseline");
      }
    }

    test(
        Subject.class,

        """
        .self-auto { align-self: auto }
        .self-start { align-self: flex-start }
        .self-end { align-self: flex-end }
        .self-center { align-self: center }
        .self-stretch { align-self: stretch }
        .self-baseline { align-self: baseline }
        """
    );
  }

  @Test
  public void appearance() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("appearance-auto appearance-none");
      }
    }

    test(
        Subject.class,

        """
        .appearance-auto { appearance: auto }
        .appearance-none { appearance: none }
        """
    );
  }

  @Test
  public void aspectRatio() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("aspect-auto aspect-16x9 aspect-9x16 aspect-2x1 aspect-1x2 aspect-4x3 aspect-3x4 aspect-3x2 aspect-2x3 aspect-1x1");
      }
    }

    test(
        Subject.class,

        """
        .aspect-16x9::before {
          float: left;
          margin-left: -1px;
          height: 0;
          width: 1px;
          padding-top: 56.25%;
          content: '';
        }
        .aspect-16x9::after {
          clear: both;
          display: table;
          content: '';
        }

        .aspect-1x1::before {
          float: left;
          margin-left: -1px;
          height: 0;
          width: 1px;
          padding-top: 100%;
          content: '';
        }
        .aspect-1x1::after {
          clear: both;
          display: table;
          content: '';
        }

        .aspect-1x2::before {
          float: left;
          margin-left: -1px;
          height: 0;
          width: 1px;
          padding-top: 200%;
          content: '';
        }
        .aspect-1x2::after {
          clear: both;
          display: table;
          content: '';
        }

        .aspect-2x1::before {
          float: left;
          margin-left: -1px;
          height: 0;
          width: 1px;
          padding-top: 50%;
          content: '';
        }
        .aspect-2x1::after {
          clear: both;
          display: table;
          content: '';
        }

        .aspect-2x3::before {
          float: left;
          margin-left: -1px;
          height: 0;
          width: 1px;
          padding-top: 150%;
          content: '';
        }
        .aspect-2x3::after {
          clear: both;
          display: table;
          content: '';
        }

        .aspect-3x2::before {
          float: left;
          margin-left: -1px;
          height: 0;
          width: 1px;
          padding-top: 66.66667%;
          content: '';
        }
        .aspect-3x2::after {
          clear: both;
          display: table;
          content: '';
        }

        .aspect-3x4::before {
          float: left;
          margin-left: -1px;
          height: 0;
          width: 1px;
          padding-top: 133.33333%;
          content: '';
        }
        .aspect-3x4::after {
          clear: both;
          display: table;
          content: '';
        }

        .aspect-4x3::before {
          float: left;
          margin-left: -1px;
          height: 0;
          width: 1px;
          padding-top: 75%;
          content: '';
        }
        .aspect-4x3::after {
          clear: both;
          display: table;
          content: '';
        }

        .aspect-9x16::before {
          float: left;
          margin-left: -1px;
          height: 0;
          width: 1px;
          padding-top: 177.77778%;
          content: '';
        }
        .aspect-9x16::after {
          clear: both;
          display: table;
          content: '';
        }

        .aspect-auto::before {
          display: none;
        }
        .aspect-auto::after {
          display: none;
        }
        """
    );
  }

  @Test
  public void backgroundColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("bg-inherit bg-current bg-transparent bg-black bg-white");
      }
    }

    test(
        Subject.class,

        """
        .bg-inherit { background-color: inherit }
        .bg-current { background-color: currentColor }
        .bg-transparent { background-color: transparent }
        .bg-black { background-color: #000000 }
        .bg-white { background-color: #ffffff }
        """
    );
  }

  @Test
  public void borderCollapse() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-collapse border-separate");
      }
    }

    test(
        Subject.class,

        """
        .border-collapse { border-collapse: collapse }
        .border-separate { border-collapse: separate }
        """
    );
  }

  @Test
  public void borderColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("border-inherit border-current border-transparent border-black border-white");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .border-inherit { border-color: inherit }
        .border-current { border-color: currentColor }
        .border-transparent { border-color: transparent }
        .border-black { border-color: #000000 }
        .border-white { border-color: #ffffff }
        """
    );
  }

  @Test
  public void borderColorXY() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("border-x-inherit border-x-current border-x-transparent border-x-black border-x-white");
        className("border-y-inherit border-y-current border-y-transparent border-y-black border-y-white");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .border-x-inherit { border-left-color: inherit; border-right-color: inherit }
        .border-x-current { border-left-color: currentColor; border-right-color: currentColor }
        .border-x-transparent { border-left-color: transparent; border-right-color: transparent }
        .border-x-black { border-left-color: #000000; border-right-color: #000000 }
        .border-x-white { border-left-color: #ffffff; border-right-color: #ffffff }
        .border-y-inherit { border-top-color: inherit; border-bottom-color: inherit }
        .border-y-current { border-top-color: currentColor; border-bottom-color: currentColor }
        .border-y-transparent { border-top-color: transparent; border-bottom-color: transparent }
        .border-y-black { border-top-color: #000000; border-bottom-color: #000000 }
        .border-y-white { border-top-color: #ffffff; border-bottom-color: #ffffff }
        """
    );
  }

  @Test
  public void borderColorSides() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("border-t-inherit border-t-current border-t-transparent border-t-black border-t-white");
        className("border-r-inherit border-r-current border-r-transparent border-r-black border-r-white");
        className("border-b-inherit border-b-current border-b-transparent border-b-black border-b-white");
        className("border-l-inherit border-l-current border-l-transparent border-l-black border-l-white");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .border-t-inherit { border-top-color: inherit }
        .border-t-current { border-top-color: currentColor }
        .border-t-transparent { border-top-color: transparent }
        .border-t-black { border-top-color: #000000 }
        .border-t-white { border-top-color: #ffffff }
        .border-r-inherit { border-right-color: inherit }
        .border-r-current { border-right-color: currentColor }
        .border-r-transparent { border-right-color: transparent }
        .border-r-black { border-right-color: #000000 }
        .border-r-white { border-right-color: #ffffff }
        .border-b-inherit { border-bottom-color: inherit }
        .border-b-current { border-bottom-color: currentColor }
        .border-b-transparent { border-bottom-color: transparent }
        .border-b-black { border-bottom-color: #000000 }
        .border-b-white { border-bottom-color: #ffffff }
        .border-l-inherit { border-left-color: inherit }
        .border-l-current { border-left-color: currentColor }
        .border-l-transparent { border-left-color: transparent }
        .border-l-black { border-left-color: #000000 }
        .border-l-white { border-left-color: #ffffff }
        """
    );
  }

  @Test
  public void borderRadius() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("rounded-none rounded-sm rounded rounded-md rounded-lg rounded-xl rounded-2xl rounded-3xl rounded-full");
        className("rounded-t-none rounded-t-sm rounded-t rounded-t-md rounded-t-lg rounded-t-xl rounded-t-2xl rounded-t-3xl rounded-t-full");
        className("rounded-r-none rounded-r-sm rounded-r rounded-r-md rounded-r-lg rounded-r-xl rounded-r-2xl rounded-r-3xl rounded-r-full");
        className("rounded-b-none rounded-b-sm rounded-b rounded-b-md rounded-b-lg rounded-b-xl rounded-b-2xl rounded-b-3xl rounded-b-full");
        className("rounded-l-none rounded-l-sm rounded-l rounded-l-md rounded-l-lg rounded-l-xl rounded-l-2xl rounded-l-3xl rounded-l-full");
        className("rounded-tl-none rounded-tl-sm rounded-tl rounded-tl-md rounded-tl-lg rounded-tl-xl rounded-tl-2xl rounded-tl-3xl rounded-tl-full");
        className("rounded-tr-none rounded-tr-sm rounded-tr rounded-tr-md rounded-tr-lg rounded-tr-xl rounded-tr-2xl rounded-tr-3xl rounded-tr-full");
        className("rounded-br-none rounded-br-sm rounded-br rounded-br-md rounded-br-lg rounded-br-xl rounded-br-2xl rounded-br-3xl rounded-br-full");
        className("rounded-bl-none rounded-bl-sm rounded-bl rounded-bl-md rounded-bl-lg rounded-bl-xl rounded-bl-2xl rounded-bl-3xl rounded-bl-full");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .rounded-none { border-radius: 0px }
        .rounded-sm { border-radius: 0.125rem }
        .rounded { border-radius: 0.25rem }
        .rounded-md { border-radius: 0.375rem }
        .rounded-lg { border-radius: 0.5rem }
        .rounded-xl { border-radius: 0.75rem }
        .rounded-2xl { border-radius: 1rem }
        .rounded-3xl { border-radius: 1.5rem }
        .rounded-full { border-radius: 9999px }
        .rounded-t-none { border-top-left-radius: 0px; border-top-right-radius: 0px }
        .rounded-t-sm { border-top-left-radius: 0.125rem; border-top-right-radius: 0.125rem }
        .rounded-t { border-top-left-radius: 0.25rem; border-top-right-radius: 0.25rem }
        .rounded-t-md { border-top-left-radius: 0.375rem; border-top-right-radius: 0.375rem }
        .rounded-t-lg { border-top-left-radius: 0.5rem; border-top-right-radius: 0.5rem }
        .rounded-t-xl { border-top-left-radius: 0.75rem; border-top-right-radius: 0.75rem }
        .rounded-t-2xl { border-top-left-radius: 1rem; border-top-right-radius: 1rem }
        .rounded-t-3xl { border-top-left-radius: 1.5rem; border-top-right-radius: 1.5rem }
        .rounded-t-full { border-top-left-radius: 9999px; border-top-right-radius: 9999px }
        .rounded-r-none { border-top-right-radius: 0px; border-bottom-right-radius: 0px }
        .rounded-r-sm { border-top-right-radius: 0.125rem; border-bottom-right-radius: 0.125rem }
        .rounded-r { border-top-right-radius: 0.25rem; border-bottom-right-radius: 0.25rem }
        .rounded-r-md { border-top-right-radius: 0.375rem; border-bottom-right-radius: 0.375rem }
        .rounded-r-lg { border-top-right-radius: 0.5rem; border-bottom-right-radius: 0.5rem }
        .rounded-r-xl { border-top-right-radius: 0.75rem; border-bottom-right-radius: 0.75rem }
        .rounded-r-2xl { border-top-right-radius: 1rem; border-bottom-right-radius: 1rem }
        .rounded-r-3xl { border-top-right-radius: 1.5rem; border-bottom-right-radius: 1.5rem }
        .rounded-r-full { border-top-right-radius: 9999px; border-bottom-right-radius: 9999px }
        .rounded-b-none { border-bottom-right-radius: 0px; border-bottom-left-radius: 0px }
        .rounded-b-sm { border-bottom-right-radius: 0.125rem; border-bottom-left-radius: 0.125rem }
        .rounded-b { border-bottom-right-radius: 0.25rem; border-bottom-left-radius: 0.25rem }
        .rounded-b-md { border-bottom-right-radius: 0.375rem; border-bottom-left-radius: 0.375rem }
        .rounded-b-lg { border-bottom-right-radius: 0.5rem; border-bottom-left-radius: 0.5rem }
        .rounded-b-xl { border-bottom-right-radius: 0.75rem; border-bottom-left-radius: 0.75rem }
        .rounded-b-2xl { border-bottom-right-radius: 1rem; border-bottom-left-radius: 1rem }
        .rounded-b-3xl { border-bottom-right-radius: 1.5rem; border-bottom-left-radius: 1.5rem }
        .rounded-b-full { border-bottom-right-radius: 9999px; border-bottom-left-radius: 9999px }
        .rounded-l-none { border-bottom-left-radius: 0px; border-top-left-radius: 0px }
        .rounded-l-sm { border-bottom-left-radius: 0.125rem; border-top-left-radius: 0.125rem }
        .rounded-l { border-bottom-left-radius: 0.25rem; border-top-left-radius: 0.25rem }
        .rounded-l-md { border-bottom-left-radius: 0.375rem; border-top-left-radius: 0.375rem }
        .rounded-l-lg { border-bottom-left-radius: 0.5rem; border-top-left-radius: 0.5rem }
        .rounded-l-xl { border-bottom-left-radius: 0.75rem; border-top-left-radius: 0.75rem }
        .rounded-l-2xl { border-bottom-left-radius: 1rem; border-top-left-radius: 1rem }
        .rounded-l-3xl { border-bottom-left-radius: 1.5rem; border-top-left-radius: 1.5rem }
        .rounded-l-full { border-bottom-left-radius: 9999px; border-top-left-radius: 9999px }
        .rounded-tl-none { border-top-left-radius: 0px }
        .rounded-tl-sm { border-top-left-radius: 0.125rem }
        .rounded-tl { border-top-left-radius: 0.25rem }
        .rounded-tl-md { border-top-left-radius: 0.375rem }
        .rounded-tl-lg { border-top-left-radius: 0.5rem }
        .rounded-tl-xl { border-top-left-radius: 0.75rem }
        .rounded-tl-2xl { border-top-left-radius: 1rem }
        .rounded-tl-3xl { border-top-left-radius: 1.5rem }
        .rounded-tl-full { border-top-left-radius: 9999px }
        .rounded-tr-none { border-top-right-radius: 0px }
        .rounded-tr-sm { border-top-right-radius: 0.125rem }
        .rounded-tr { border-top-right-radius: 0.25rem }
        .rounded-tr-md { border-top-right-radius: 0.375rem }
        .rounded-tr-lg { border-top-right-radius: 0.5rem }
        .rounded-tr-xl { border-top-right-radius: 0.75rem }
        .rounded-tr-2xl { border-top-right-radius: 1rem }
        .rounded-tr-3xl { border-top-right-radius: 1.5rem }
        .rounded-tr-full { border-top-right-radius: 9999px }
        .rounded-br-none { border-bottom-right-radius: 0px }
        .rounded-br-sm { border-bottom-right-radius: 0.125rem }
        .rounded-br { border-bottom-right-radius: 0.25rem }
        .rounded-br-md { border-bottom-right-radius: 0.375rem }
        .rounded-br-lg { border-bottom-right-radius: 0.5rem }
        .rounded-br-xl { border-bottom-right-radius: 0.75rem }
        .rounded-br-2xl { border-bottom-right-radius: 1rem }
        .rounded-br-3xl { border-bottom-right-radius: 1.5rem }
        .rounded-br-full { border-bottom-right-radius: 9999px }
        .rounded-bl-none { border-bottom-left-radius: 0px }
        .rounded-bl-sm { border-bottom-left-radius: 0.125rem }
        .rounded-bl { border-bottom-left-radius: 0.25rem }
        .rounded-bl-md { border-bottom-left-radius: 0.375rem }
        .rounded-bl-lg { border-bottom-left-radius: 0.5rem }
        .rounded-bl-xl { border-bottom-left-radius: 0.75rem }
        .rounded-bl-2xl { border-bottom-left-radius: 1rem }
        .rounded-bl-3xl { border-bottom-left-radius: 1.5rem }
        .rounded-bl-full { border-bottom-left-radius: 9999px }
        """
    );
  }

  @Test
  public void borderStyle() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-solid border-dashed border-dotted border-double border-hidden border-none");
      }
    }

    test(
        Subject.class,

        """
        .border-solid { border-style: solid }
        .border-dashed { border-style: dashed }
        .border-dotted { border-style: dotted }
        .border-double { border-style: double }
        .border-hidden { border-style: hidden }
        .border-none { border-style: none }
        """
    );
  }

  @Test
  public void borderWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border border-0 border-2 border-4 border-8");
        className("border-x border-x-0 border-x-2 border-x-4 border-x-8");
        className("border-y border-y-0 border-y-2 border-y-4 border-y-8");
        className("border-t border-t-0 border-t-2 border-t-4 border-t-8");
        className("border-r border-r-0 border-r-2 border-r-4 border-r-8");
        className("border-b border-b-0 border-b-2 border-b-4 border-b-8");
        className("border-l border-l-0 border-l-2 border-l-4 border-l-8");
      }
    }

    test(
        Subject.class,

        """
        .border { border-width: 1px }
        .border-0 { border-width: 0px }
        .border-2 { border-width: 2px }
        .border-4 { border-width: 4px }
        .border-8 { border-width: 8px }
        .border-x { border-left-width: 1px; border-right-width: 1px }
        .border-x-0 { border-left-width: 0px; border-right-width: 0px }
        .border-x-2 { border-left-width: 2px; border-right-width: 2px }
        .border-x-4 { border-left-width: 4px; border-right-width: 4px }
        .border-x-8 { border-left-width: 8px; border-right-width: 8px }
        .border-y { border-top-width: 1px; border-bottom-width: 1px }
        .border-y-0 { border-top-width: 0px; border-bottom-width: 0px }
        .border-y-2 { border-top-width: 2px; border-bottom-width: 2px }
        .border-y-4 { border-top-width: 4px; border-bottom-width: 4px }
        .border-y-8 { border-top-width: 8px; border-bottom-width: 8px }
        .border-t { border-top-width: 1px }
        .border-t-0 { border-top-width: 0px }
        .border-t-2 { border-top-width: 2px }
        .border-t-4 { border-top-width: 4px }
        .border-t-8 { border-top-width: 8px }
        .border-r { border-right-width: 1px }
        .border-r-0 { border-right-width: 0px }
        .border-r-2 { border-right-width: 2px }
        .border-r-4 { border-right-width: 4px }
        .border-r-8 { border-right-width: 8px }
        .border-b { border-bottom-width: 1px }
        .border-b-0 { border-bottom-width: 0px }
        .border-b-2 { border-bottom-width: 2px }
        .border-b-4 { border-bottom-width: 4px }
        .border-b-8 { border-bottom-width: 8px }
        .border-l { border-left-width: 1px }
        .border-l-0 { border-left-width: 0px }
        .border-l-2 { border-left-width: 2px }
        .border-l-4 { border-left-width: 4px }
        .border-l-8 { border-left-width: 8px }
        """
    );
  }

  @Test
  public void borderSpacing() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("border-spacing-px border-spacing-0 border-spacing-0.5 border-spacing-1 border-spacing-1.5 border-spacing-2 border-spacing-2.5 border-spacing-3 border-spacing-3.5 border-spacing-4 border-spacing-5 border-spacing-6 border-spacing-7 border-spacing-8 border-spacing-9 border-spacing-10 border-spacing-11 border-spacing-12 border-spacing-14 border-spacing-16 border-spacing-20 border-spacing-24 border-spacing-28 border-spacing-32 border-spacing-36 border-spacing-40 border-spacing-44 border-spacing-48 border-spacing-52 border-spacing-56 border-spacing-60 border-spacing-64 border-spacing-72 border-spacing-80 border-spacing-96");
        className("border-spacing-x-px border-spacing-x-0 border-spacing-x-0.5 border-spacing-x-1 border-spacing-x-1.5 border-spacing-x-2 border-spacing-x-2.5 border-spacing-x-3 border-spacing-x-3.5 border-spacing-x-4 border-spacing-x-5 border-spacing-x-6 border-spacing-x-7 border-spacing-x-8 border-spacing-x-9 border-spacing-x-10 border-spacing-x-11 border-spacing-x-12 border-spacing-x-14 border-spacing-x-16 border-spacing-x-20 border-spacing-x-24 border-spacing-x-28 border-spacing-x-32 border-spacing-x-36 border-spacing-x-40 border-spacing-x-44 border-spacing-x-48 border-spacing-x-52 border-spacing-x-56 border-spacing-x-60 border-spacing-x-64 border-spacing-x-72 border-spacing-x-80 border-spacing-x-96");
        className("border-spacing-y-px border-spacing-y-0 border-spacing-y-0.5 border-spacing-y-1 border-spacing-y-1.5 border-spacing-y-2 border-spacing-y-2.5 border-spacing-y-3 border-spacing-y-3.5 border-spacing-y-4 border-spacing-y-5 border-spacing-y-6 border-spacing-y-7 border-spacing-y-8 border-spacing-y-9 border-spacing-y-10 border-spacing-y-11 border-spacing-y-12 border-spacing-y-14 border-spacing-y-16 border-spacing-y-20 border-spacing-y-24 border-spacing-y-28 border-spacing-y-32 border-spacing-y-36 border-spacing-y-40 border-spacing-y-44 border-spacing-y-48 border-spacing-y-52 border-spacing-y-56 border-spacing-y-60 border-spacing-y-64 border-spacing-y-72 border-spacing-y-80 border-spacing-y-96");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .border-spacing-px { border-spacing: 1px 1px }
        .border-spacing-0 { border-spacing: 0px 0px }
        .border-spacing-0\\.5 { border-spacing: 0.125rem 0.125rem }
        .border-spacing-1 { border-spacing: 0.25rem 0.25rem }
        .border-spacing-1\\.5 { border-spacing: 0.375rem 0.375rem }
        .border-spacing-2 { border-spacing: 0.5rem 0.5rem }
        .border-spacing-2\\.5 { border-spacing: 0.625rem 0.625rem }
        .border-spacing-3 { border-spacing: 0.75rem 0.75rem }
        .border-spacing-3\\.5 { border-spacing: 0.875rem 0.875rem }
        .border-spacing-4 { border-spacing: 1rem 1rem }
        .border-spacing-5 { border-spacing: 1.25rem 1.25rem }
        .border-spacing-6 { border-spacing: 1.5rem 1.5rem }
        .border-spacing-7 { border-spacing: 1.75rem 1.75rem }
        .border-spacing-8 { border-spacing: 2rem 2rem }
        .border-spacing-9 { border-spacing: 2.25rem 2.25rem }
        .border-spacing-10 { border-spacing: 2.5rem 2.5rem }
        .border-spacing-11 { border-spacing: 2.75rem 2.75rem }
        .border-spacing-12 { border-spacing: 3rem 3rem }
        .border-spacing-14 { border-spacing: 3.5rem 3.5rem }
        .border-spacing-16 { border-spacing: 4rem 4rem }
        .border-spacing-20 { border-spacing: 5rem 5rem }
        .border-spacing-24 { border-spacing: 6rem 6rem }
        .border-spacing-28 { border-spacing: 7rem 7rem }
        .border-spacing-32 { border-spacing: 8rem 8rem }
        .border-spacing-36 { border-spacing: 9rem 9rem }
        .border-spacing-40 { border-spacing: 10rem 10rem }
        .border-spacing-44 { border-spacing: 11rem 11rem }
        .border-spacing-48 { border-spacing: 12rem 12rem }
        .border-spacing-52 { border-spacing: 13rem 13rem }
        .border-spacing-56 { border-spacing: 14rem 14rem }
        .border-spacing-60 { border-spacing: 15rem 15rem }
        .border-spacing-64 { border-spacing: 16rem 16rem }
        .border-spacing-72 { border-spacing: 18rem 18rem }
        .border-spacing-80 { border-spacing: 20rem 20rem }
        .border-spacing-96 { border-spacing: 24rem 24rem }
        .border-spacing-x-px { border-spacing: 1px 0 }
        .border-spacing-x-0 { border-spacing: 0px 0 }
        .border-spacing-x-0\\.5 { border-spacing: 0.125rem 0 }
        .border-spacing-x-1 { border-spacing: 0.25rem 0 }
        .border-spacing-x-1\\.5 { border-spacing: 0.375rem 0 }
        .border-spacing-x-2 { border-spacing: 0.5rem 0 }
        .border-spacing-x-2\\.5 { border-spacing: 0.625rem 0 }
        .border-spacing-x-3 { border-spacing: 0.75rem 0 }
        .border-spacing-x-3\\.5 { border-spacing: 0.875rem 0 }
        .border-spacing-x-4 { border-spacing: 1rem 0 }
        .border-spacing-x-5 { border-spacing: 1.25rem 0 }
        .border-spacing-x-6 { border-spacing: 1.5rem 0 }
        .border-spacing-x-7 { border-spacing: 1.75rem 0 }
        .border-spacing-x-8 { border-spacing: 2rem 0 }
        .border-spacing-x-9 { border-spacing: 2.25rem 0 }
        .border-spacing-x-10 { border-spacing: 2.5rem 0 }
        .border-spacing-x-11 { border-spacing: 2.75rem 0 }
        .border-spacing-x-12 { border-spacing: 3rem 0 }
        .border-spacing-x-14 { border-spacing: 3.5rem 0 }
        .border-spacing-x-16 { border-spacing: 4rem 0 }
        .border-spacing-x-20 { border-spacing: 5rem 0 }
        .border-spacing-x-24 { border-spacing: 6rem 0 }
        .border-spacing-x-28 { border-spacing: 7rem 0 }
        .border-spacing-x-32 { border-spacing: 8rem 0 }
        .border-spacing-x-36 { border-spacing: 9rem 0 }
        .border-spacing-x-40 { border-spacing: 10rem 0 }
        .border-spacing-x-44 { border-spacing: 11rem 0 }
        .border-spacing-x-48 { border-spacing: 12rem 0 }
        .border-spacing-x-52 { border-spacing: 13rem 0 }
        .border-spacing-x-56 { border-spacing: 14rem 0 }
        .border-spacing-x-60 { border-spacing: 15rem 0 }
        .border-spacing-x-64 { border-spacing: 16rem 0 }
        .border-spacing-x-72 { border-spacing: 18rem 0 }
        .border-spacing-x-80 { border-spacing: 20rem 0 }
        .border-spacing-x-96 { border-spacing: 24rem 0 }
        .border-spacing-y-px { border-spacing: 0 1px }
        .border-spacing-y-0 { border-spacing: 0 0px }
        .border-spacing-y-0\\.5 { border-spacing: 0 0.125rem }
        .border-spacing-y-1 { border-spacing: 0 0.25rem }
        .border-spacing-y-1\\.5 { border-spacing: 0 0.375rem }
        .border-spacing-y-2 { border-spacing: 0 0.5rem }
        .border-spacing-y-2\\.5 { border-spacing: 0 0.625rem }
        .border-spacing-y-3 { border-spacing: 0 0.75rem }
        .border-spacing-y-3\\.5 { border-spacing: 0 0.875rem }
        .border-spacing-y-4 { border-spacing: 0 1rem }
        .border-spacing-y-5 { border-spacing: 0 1.25rem }
        .border-spacing-y-6 { border-spacing: 0 1.5rem }
        .border-spacing-y-7 { border-spacing: 0 1.75rem }
        .border-spacing-y-8 { border-spacing: 0 2rem }
        .border-spacing-y-9 { border-spacing: 0 2.25rem }
        .border-spacing-y-10 { border-spacing: 0 2.5rem }
        .border-spacing-y-11 { border-spacing: 0 2.75rem }
        .border-spacing-y-12 { border-spacing: 0 3rem }
        .border-spacing-y-14 { border-spacing: 0 3.5rem }
        .border-spacing-y-16 { border-spacing: 0 4rem }
        .border-spacing-y-20 { border-spacing: 0 5rem }
        .border-spacing-y-24 { border-spacing: 0 6rem }
        .border-spacing-y-28 { border-spacing: 0 7rem }
        .border-spacing-y-32 { border-spacing: 0 8rem }
        .border-spacing-y-36 { border-spacing: 0 9rem }
        .border-spacing-y-40 { border-spacing: 0 10rem }
        .border-spacing-y-44 { border-spacing: 0 11rem }
        .border-spacing-y-48 { border-spacing: 0 12rem }
        .border-spacing-y-52 { border-spacing: 0 13rem }
        .border-spacing-y-56 { border-spacing: 0 14rem }
        .border-spacing-y-60 { border-spacing: 0 15rem }
        .border-spacing-y-64 { border-spacing: 0 16rem }
        .border-spacing-y-72 { border-spacing: 0 18rem }
        .border-spacing-y-80 { border-spacing: 0 20rem }
        .border-spacing-y-96 { border-spacing: 0 24rem }
        """
    );
  }

  @Test
  public void boxShadow() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("shadow-sm shadow shadow-md shadow-lg shadow-xl shadow-2xl shadow-inner shadow-none");
        className("shadow-[0_35px_60px_-15px_rgb(0_0_0_/_0.3)]");
      }
    }

    test(
        Subject.class,

        """
        .shadow-sm {
          --tw-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05);
          --tw-shadow-colored: 0 1px 2px 0 var(--tw-shadow-color);
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .shadow {
          --tw-shadow: 0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1);
          --tw-shadow-colored: 0 1px 3px 0 var(--tw-shadow-color), 0 1px 2px -1px var(--tw-shadow-color);
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .shadow-md {
          --tw-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
          --tw-shadow-colored: 0 4px 6px -1px var(--tw-shadow-color), 0 2px 4px -2px var(--tw-shadow-color);
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .shadow-lg {
          --tw-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
          --tw-shadow-colored: 0 10px 15px -3px var(--tw-shadow-color), 0 4px 6px -4px var(--tw-shadow-color);
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .shadow-xl {
          --tw-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1);
          --tw-shadow-colored: 0 20px 25px -5px var(--tw-shadow-color), 0 8px 10px -6px var(--tw-shadow-color);
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .shadow-2xl {
          --tw-shadow: 0 25px 50px -12px rgb(0 0 0 / 0.25);
          --tw-shadow-colored: 0 25px 50px -12px var(--tw-shadow-color);
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .shadow-inner {
          --tw-shadow: inset 0 2px 4px 0 rgb(0 0 0 / 0.05);
          --tw-shadow-colored: inset 0 2px 4px 0 var(--tw-shadow-color);
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .shadow-none {
          --tw-shadow: none;
          --tw-shadow-colored: none;
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .shadow-\\[0_35px_60px_-15px_rgb\\(0_0_0_\\/_0\\.3\\)\\] {
          --tw-shadow: 0 35px 60px -15px rgb(0 0 0 / 0.3);
          --tw-shadow-colored: 0 35px 60px -15px var(--tw-shadow-color);
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        """
    );
  }

  @Test
  public void boxShadowColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("shadow-inherit shadow-current shadow-transparent shadow-white shadow-black");
      }
    }

    test(
        Subject.class,

        """
        .shadow-inherit { --tw-shadow-colored: inherit; --tw-shadow: var(--tw-shadow-colored) }
        .shadow-current { --tw-shadow-colored: currentColor; --tw-shadow: var(--tw-shadow-colored) }
        .shadow-transparent { --tw-shadow-colored: transparent; --tw-shadow: var(--tw-shadow-colored) }
        .shadow-white { --tw-shadow-colored: #ffffff; --tw-shadow: var(--tw-shadow-colored) }
        .shadow-black { --tw-shadow-colored: #000000; --tw-shadow: var(--tw-shadow-colored) }
        """
    );
  }

  @Test
  public void clear() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("clear-left clear-right clear-both clear-none");
      }
    }

    test(
        Subject.class,

        """
        .clear-left { clear: left }
        .clear-right { clear: right }
        .clear-both { clear: both }
        .clear-none { clear: none }
        """
    );
  }

  @Test
  public void container() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("container");
      }
    }

    test(
        Subject.class,

        """
        .container {
          width: 100%;
        }
        @media (min-width: 640px) {
          .container {
            max-width: 640px;
          }
        }
        @media (min-width: 768px) {
          .container {
            max-width: 768px;
          }
        }
        @media (min-width: 1024px) {
          .container {
            max-width: 1024px;
          }
        }
        @media (min-width: 1280px) {
          .container {
            max-width: 1280px;
          }
        }
        @media (min-width: 1536px) {
          .container {
            max-width: 1536px;
          }
        }
        """
    );
  }

  @Test
  public void content() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("content-none");
      }
    }

    test(
        Subject.class,

        """
        .content-none { content: none }
        """
    );
  }

  @Test
  public void cursor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className(
            "cursor-auto cursor-default cursor-pointer cursor-wait cursor-text cursor-move cursor-help cursor-not-allowed cursor-none cursor-context-menu cursor-progress cursor-cell cursor-crosshair cursor-vertical-text cursor-alias cursor-copy cursor-no-drop cursor-grab cursor-grabbing cursor-all-scroll cursor-col-resize cursor-row-resize cursor-n-resize cursor-e-resize cursor-s-resize cursor-w-resize cursor-ne-resize cursor-nw-resize cursor-se-resize cursor-sw-resize cursor-ew-resize cursor-ns-resize cursor-nesw-resize cursor-nwse-resize cursor-zoom-in cursor-zoom-out");
      }
    }

    test(
        Subject.class,

        """
        .cursor-auto { cursor: auto }
        .cursor-default { cursor: default }
        .cursor-pointer { cursor: pointer }
        .cursor-wait { cursor: wait }
        .cursor-text { cursor: text }
        .cursor-move { cursor: move }
        .cursor-help { cursor: help }
        .cursor-not-allowed { cursor: not-allowed }
        .cursor-none { cursor: none }
        .cursor-context-menu { cursor: context-menu }
        .cursor-progress { cursor: progress }
        .cursor-cell { cursor: cell }
        .cursor-crosshair { cursor: crosshair }
        .cursor-vertical-text { cursor: vertical-text }
        .cursor-alias { cursor: alias }
        .cursor-copy { cursor: copy }
        .cursor-no-drop { cursor: no-drop }
        .cursor-grab { cursor: grab }
        .cursor-grabbing { cursor: grabbing }
        .cursor-all-scroll { cursor: all-scroll }
        .cursor-col-resize { cursor: col-resize }
        .cursor-row-resize { cursor: row-resize }
        .cursor-n-resize { cursor: n-resize }
        .cursor-e-resize { cursor: e-resize }
        .cursor-s-resize { cursor: s-resize }
        .cursor-w-resize { cursor: w-resize }
        .cursor-ne-resize { cursor: ne-resize }
        .cursor-nw-resize { cursor: nw-resize }
        .cursor-se-resize { cursor: se-resize }
        .cursor-sw-resize { cursor: sw-resize }
        .cursor-ew-resize { cursor: ew-resize }
        .cursor-ns-resize { cursor: ns-resize }
        .cursor-nesw-resize { cursor: nesw-resize }
        .cursor-nwse-resize { cursor: nwse-resize }
        .cursor-zoom-in { cursor: zoom-in }
        .cursor-zoom-out { cursor: zoom-out }
        """
    );
  }

  @Test
  public void display() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("block");
        className("inline-block");
        className("inline");
        className("flex");
        className("inline-flex");
        className("table");
        className("inline-table");
        className("table-caption");
        className("table-cell");
        className("table-column");
        className("table-column-group");
        className("table-footer-group");
        className("table-header-group");
        className("table-row-group");
        className("table-row");
        className("flow-root");
        className("grid");
        className("inline-grid");
        className("contents");
        className("list-item");
        className("hidden");
      }
    }

    test(
        Subject.class,

        """
        .block { display: block }
        .inline-block { display: inline-block }
        .inline { display: inline }
        .flex { display: flex }
        .inline-flex { display: inline-flex }
        .table { display: table }
        .inline-table { display: inline-table }
        .table-caption { display: table-caption }
        .table-cell { display: table-cell }
        .table-column { display: table-column }
        .table-column-group { display: table-column-group }
        .table-footer-group { display: table-footer-group }
        .table-header-group { display: table-header-group }
        .table-row-group { display: table-row-group }
        .table-row { display: table-row }
        .flow-root { display: flow-root }
        .grid { display: grid }
        .inline-grid { display: inline-grid }
        .contents { display: contents }
        .list-item { display: list-item }
        .hidden { display: none }
        """
    );
  }

  @Test
  public void fill() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("fill-none");
        className("fill-inherit fill-current fill-transparent fill-black fill-white");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .fill-none { fill: none }
        .fill-inherit { fill: inherit }
        .fill-current { fill: currentColor }
        .fill-transparent { fill: transparent }
        .fill-black { fill: #000000 }
        .fill-white { fill: #ffffff }
        """
    );
  }

  @Test
  public void flex() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("flex-1 flex-auto flex-initial flex-none");
        className("flex-[2_2_0%]");
      }
    }

    test(
        Subject.class,

        """
        .flex-1 { flex: 1 1 0% }
        .flex-auto { flex: 1 1 auto }
        .flex-initial { flex: 0 1 auto }
        .flex-none { flex: none }
        .flex-\\[2_2_0\\%\\] { flex: 2 2 0% }
        """
    );
  }

  @Test
  public void flexBasis() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("basis-px basis-0 basis-0.5 basis-1 basis-1.5 basis-2 basis-2.5 basis-3 basis-3.5 basis-4 basis-5 basis-6 basis-7 basis-8 basis-9 basis-10 basis-11 basis-12 basis-14 basis-16 basis-20 basis-24 basis-28 basis-32 basis-36 basis-40 basis-44 basis-48 basis-52 basis-56 basis-60 basis-64 basis-72 basis-80 basis-96");
        className("basis-auto basis-1/2 basis-1/3 basis-2/3 basis-1/4 basis-2/4 basis-3/4 basis-1/5 basis-2/5 basis-3/5 basis-4/5 basis-1/6 basis-2/6 basis-3/6 basis-4/6 basis-5/6 basis-1/12 basis-2/12 basis-3/12 basis-4/12 basis-5/12 basis-6/12 basis-7/12 basis-8/12 basis-9/12 basis-10/12 basis-11/12 basis-full");
        className("basis-[14.2857143%]");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .basis-px { flex-basis: 1px }
        .basis-0 { flex-basis: 0px }
        .basis-0\\.5 { flex-basis: 0.125rem }
        .basis-1 { flex-basis: 0.25rem }
        .basis-1\\.5 { flex-basis: 0.375rem }
        .basis-2 { flex-basis: 0.5rem }
        .basis-2\\.5 { flex-basis: 0.625rem }
        .basis-3 { flex-basis: 0.75rem }
        .basis-3\\.5 { flex-basis: 0.875rem }
        .basis-4 { flex-basis: 1rem }
        .basis-5 { flex-basis: 1.25rem }
        .basis-6 { flex-basis: 1.5rem }
        .basis-7 { flex-basis: 1.75rem }
        .basis-8 { flex-basis: 2rem }
        .basis-9 { flex-basis: 2.25rem }
        .basis-10 { flex-basis: 2.5rem }
        .basis-11 { flex-basis: 2.75rem }
        .basis-12 { flex-basis: 3rem }
        .basis-14 { flex-basis: 3.5rem }
        .basis-16 { flex-basis: 4rem }
        .basis-20 { flex-basis: 5rem }
        .basis-24 { flex-basis: 6rem }
        .basis-28 { flex-basis: 7rem }
        .basis-32 { flex-basis: 8rem }
        .basis-36 { flex-basis: 9rem }
        .basis-40 { flex-basis: 10rem }
        .basis-44 { flex-basis: 11rem }
        .basis-48 { flex-basis: 12rem }
        .basis-52 { flex-basis: 13rem }
        .basis-56 { flex-basis: 14rem }
        .basis-60 { flex-basis: 15rem }
        .basis-64 { flex-basis: 16rem }
        .basis-72 { flex-basis: 18rem }
        .basis-80 { flex-basis: 20rem }
        .basis-96 { flex-basis: 24rem }
        .basis-auto { flex-basis: auto }
        .basis-1\\/2 { flex-basis: 50% }
        .basis-1\\/3 { flex-basis: 33.333333% }
        .basis-2\\/3 { flex-basis: 66.666667% }
        .basis-1\\/4 { flex-basis: 25% }
        .basis-2\\/4 { flex-basis: 50% }
        .basis-3\\/4 { flex-basis: 75% }
        .basis-1\\/5 { flex-basis: 20% }
        .basis-2\\/5 { flex-basis: 40% }
        .basis-3\\/5 { flex-basis: 60% }
        .basis-4\\/5 { flex-basis: 80% }
        .basis-1\\/6 { flex-basis: 16.666667% }
        .basis-2\\/6 { flex-basis: 33.333333% }
        .basis-3\\/6 { flex-basis: 50% }
        .basis-4\\/6 { flex-basis: 66.666667% }
        .basis-5\\/6 { flex-basis: 83.333333% }
        .basis-1\\/12 { flex-basis: 8.333333% }
        .basis-2\\/12 { flex-basis: 16.666667% }
        .basis-3\\/12 { flex-basis: 25% }
        .basis-4\\/12 { flex-basis: 33.333333% }
        .basis-5\\/12 { flex-basis: 41.666667% }
        .basis-6\\/12 { flex-basis: 50% }
        .basis-7\\/12 { flex-basis: 58.333333% }
        .basis-8\\/12 { flex-basis: 66.666667% }
        .basis-9\\/12 { flex-basis: 75% }
        .basis-10\\/12 { flex-basis: 83.333333% }
        .basis-11\\/12 { flex-basis: 91.666667% }
        .basis-full { flex-basis: 100% }
        .basis-\\[14\\.2857143\\%\\] { flex-basis: 14.2857143% }
        """
    );
  }

  @Test
  public void flexDirection() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("flex-row");
        className("flex-row-reverse");
        className("flex-col");
        className("flex-col-reverse");
      }
    }

    test(
        Subject.class,

        """
        .flex-row { flex-direction: row }
        .flex-row-reverse { flex-direction: row-reverse }
        .flex-col { flex-direction: column }
        .flex-col-reverse { flex-direction: column-reverse }
        """
    );
  }

  @Test
  public void flexGrow() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("grow grow-0 grow-[2]");
      }
    }

    test(
        Subject.class,

        """
        .grow { flex-grow: 1 }
        .grow-0 { flex-grow: 0 }
        .grow-\\[2\\] { flex-grow: 2 }
        """
    );
  }

  @Test
  public void flexShrink() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("shrink shrink-0 shrink-[2]");
      }
    }

    test(
        Subject.class,

        """
        .shrink { flex-shrink: 1 }
        .shrink-0 { flex-shrink: 0 }
        .shrink-\\[2\\] { flex-shrink: 2 }
        """
    );
  }

  @Test
  public void flexWrap() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("flex-wrap flex-wrap-reverse flex-nowrap");
      }
    }

    test(
        Subject.class,

        """
        .flex-wrap { flex-wrap: wrap }
        .flex-wrap-reverse { flex-wrap: wrap-reverse }
        .flex-nowrap { flex-wrap: nowrap }
        """
    );
  }

  @Test
  public void floatTest() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("float-right float-left float-none");
      }
    }

    test(
        Subject.class,

        """
        .float-right { float: right }
        .float-left { float: left }
        .float-none { float: none }
        """
    );
  }

  @Test
  public void fontSize() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-xs text-sm text-base text-lg text-xl text-2xl text-3xl text-4xl text-5xl text-6xl text-7xl text-8xl text-9xl");
      }
    }

    test(
        Subject.class,

        """
        .text-xs { font-size: 0.75rem; line-height: 1rem }
        .text-sm { font-size: 0.875rem; line-height: 1.25rem }
        .text-base { font-size: 1rem; line-height: 1.5rem }
        .text-lg { font-size: 1.125rem; line-height: 1.75rem }
        .text-xl { font-size: 1.25rem; line-height: 1.75rem }
        .text-2xl { font-size: 1.5rem; line-height: 2rem }
        .text-3xl { font-size: 1.875rem; line-height: 2.25rem }
        .text-4xl { font-size: 2.25rem; line-height: 2.5rem }
        .text-5xl { font-size: 3rem; line-height: 1 }
        .text-6xl { font-size: 3.75rem; line-height: 1 }
        .text-7xl { font-size: 4.5rem; line-height: 1 }
        .text-8xl { font-size: 6rem; line-height: 1 }
        .text-9xl { font-size: 8rem; line-height: 1 }
        """
    );
  }

  @Test
  public void fontStyle() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("italic not-italic");
      }
    }

    test(
        Subject.class,

        """
        .italic { font-style: italic }
        .not-italic { font-style: normal }
        """
    );
  }

  @Test
  public void fontWeight() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("font-thin font-extralight font-light font-normal font-medium font-semibold font-bold font-extrabold font-black");
      }
    }

    test(
        Subject.class,

        """
        .font-thin { font-weight: 100 }
        .font-extralight { font-weight: 200 }
        .font-light { font-weight: 300 }
        .font-normal { font-weight: 400 }
        .font-medium { font-weight: 500 }
        .font-semibold { font-weight: 600 }
        .font-bold { font-weight: 700 }
        .font-extrabold { font-weight: 800 }
        .font-black { font-weight: 900 }
        """
    );
  }

  @Test
  public void gap() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className(
            "gap-px gap-0 gap-0.5 gap-1 gap-1.5 gap-2 gap-2.5 gap-3 gap-3.5 gap-4 gap-5 gap-6 gap-7 gap-8 gap-9 gap-10 gap-11 gap-12 gap-14 gap-16 gap-20 gap-24 gap-28 gap-32 gap-36 gap-40 gap-44 gap-48 gap-52 gap-56 gap-60 gap-64 gap-72 gap-80 gap-96");
        className(
            "gap-x-px gap-x-0 gap-x-0.5 gap-x-1 gap-x-1.5 gap-x-2 gap-x-2.5 gap-x-3 gap-x-3.5 gap-x-4 gap-x-5 gap-x-6 gap-x-7 gap-x-8 gap-x-9 gap-x-10 gap-x-11 gap-x-12 gap-x-14 gap-x-16 gap-x-20 gap-x-24 gap-x-28 gap-x-32 gap-x-36 gap-x-40 gap-x-44 gap-x-48 gap-x-52 gap-x-56 gap-x-60 gap-x-64 gap-x-72 gap-x-80 gap-x-96");
        className(
            "gap-y-px gap-y-0 gap-y-0.5 gap-y-1 gap-y-1.5 gap-y-2 gap-y-2.5 gap-y-3 gap-y-3.5 gap-y-4 gap-y-5 gap-y-6 gap-y-7 gap-y-8 gap-y-9 gap-y-10 gap-y-11 gap-y-12 gap-y-14 gap-y-16 gap-y-20 gap-y-24 gap-y-28 gap-y-32 gap-y-36 gap-y-40 gap-y-44 gap-y-48 gap-y-52 gap-y-56 gap-y-60 gap-y-64 gap-y-72 gap-y-80 gap-y-96");
      }
    }

    test(
        Subject.class,

        """
        .gap-px { gap: 1px }
        .gap-0 { gap: 0px }
        .gap-0\\.5 { gap: 0.125rem }
        .gap-1 { gap: 0.25rem }
        .gap-1\\.5 { gap: 0.375rem }
        .gap-2 { gap: 0.5rem }
        .gap-2\\.5 { gap: 0.625rem }
        .gap-3 { gap: 0.75rem }
        .gap-3\\.5 { gap: 0.875rem }
        .gap-4 { gap: 1rem }
        .gap-5 { gap: 1.25rem }
        .gap-6 { gap: 1.5rem }
        .gap-7 { gap: 1.75rem }
        .gap-8 { gap: 2rem }
        .gap-9 { gap: 2.25rem }
        .gap-10 { gap: 2.5rem }
        .gap-11 { gap: 2.75rem }
        .gap-12 { gap: 3rem }
        .gap-14 { gap: 3.5rem }
        .gap-16 { gap: 4rem }
        .gap-20 { gap: 5rem }
        .gap-24 { gap: 6rem }
        .gap-28 { gap: 7rem }
        .gap-32 { gap: 8rem }
        .gap-36 { gap: 9rem }
        .gap-40 { gap: 10rem }
        .gap-44 { gap: 11rem }
        .gap-48 { gap: 12rem }
        .gap-52 { gap: 13rem }
        .gap-56 { gap: 14rem }
        .gap-60 { gap: 15rem }
        .gap-64 { gap: 16rem }
        .gap-72 { gap: 18rem }
        .gap-80 { gap: 20rem }
        .gap-96 { gap: 24rem }
        .gap-x-px { column-gap: 1px }
        .gap-x-0 { column-gap: 0px }
        .gap-x-0\\.5 { column-gap: 0.125rem }
        .gap-x-1 { column-gap: 0.25rem }
        .gap-x-1\\.5 { column-gap: 0.375rem }
        .gap-x-2 { column-gap: 0.5rem }
        .gap-x-2\\.5 { column-gap: 0.625rem }
        .gap-x-3 { column-gap: 0.75rem }
        .gap-x-3\\.5 { column-gap: 0.875rem }
        .gap-x-4 { column-gap: 1rem }
        .gap-x-5 { column-gap: 1.25rem }
        .gap-x-6 { column-gap: 1.5rem }
        .gap-x-7 { column-gap: 1.75rem }
        .gap-x-8 { column-gap: 2rem }
        .gap-x-9 { column-gap: 2.25rem }
        .gap-x-10 { column-gap: 2.5rem }
        .gap-x-11 { column-gap: 2.75rem }
        .gap-x-12 { column-gap: 3rem }
        .gap-x-14 { column-gap: 3.5rem }
        .gap-x-16 { column-gap: 4rem }
        .gap-x-20 { column-gap: 5rem }
        .gap-x-24 { column-gap: 6rem }
        .gap-x-28 { column-gap: 7rem }
        .gap-x-32 { column-gap: 8rem }
        .gap-x-36 { column-gap: 9rem }
        .gap-x-40 { column-gap: 10rem }
        .gap-x-44 { column-gap: 11rem }
        .gap-x-48 { column-gap: 12rem }
        .gap-x-52 { column-gap: 13rem }
        .gap-x-56 { column-gap: 14rem }
        .gap-x-60 { column-gap: 15rem }
        .gap-x-64 { column-gap: 16rem }
        .gap-x-72 { column-gap: 18rem }
        .gap-x-80 { column-gap: 20rem }
        .gap-x-96 { column-gap: 24rem }
        .gap-y-px { row-gap: 1px }
        .gap-y-0 { row-gap: 0px }
        .gap-y-0\\.5 { row-gap: 0.125rem }
        .gap-y-1 { row-gap: 0.25rem }
        .gap-y-1\\.5 { row-gap: 0.375rem }
        .gap-y-2 { row-gap: 0.5rem }
        .gap-y-2\\.5 { row-gap: 0.625rem }
        .gap-y-3 { row-gap: 0.75rem }
        .gap-y-3\\.5 { row-gap: 0.875rem }
        .gap-y-4 { row-gap: 1rem }
        .gap-y-5 { row-gap: 1.25rem }
        .gap-y-6 { row-gap: 1.5rem }
        .gap-y-7 { row-gap: 1.75rem }
        .gap-y-8 { row-gap: 2rem }
        .gap-y-9 { row-gap: 2.25rem }
        .gap-y-10 { row-gap: 2.5rem }
        .gap-y-11 { row-gap: 2.75rem }
        .gap-y-12 { row-gap: 3rem }
        .gap-y-14 { row-gap: 3.5rem }
        .gap-y-16 { row-gap: 4rem }
        .gap-y-20 { row-gap: 5rem }
        .gap-y-24 { row-gap: 6rem }
        .gap-y-28 { row-gap: 7rem }
        .gap-y-32 { row-gap: 8rem }
        .gap-y-36 { row-gap: 9rem }
        .gap-y-40 { row-gap: 10rem }
        .gap-y-44 { row-gap: 11rem }
        .gap-y-48 { row-gap: 12rem }
        .gap-y-52 { row-gap: 13rem }
        .gap-y-56 { row-gap: 14rem }
        .gap-y-60 { row-gap: 15rem }
        .gap-y-64 { row-gap: 16rem }
        .gap-y-72 { row-gap: 18rem }
        .gap-y-80 { row-gap: 20rem }
        .gap-y-96 { row-gap: 24rem }
        """
    );
  }

  @Test
  public void gridColumn() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("col-auto col-span-1 col-span-2 col-span-3 col-span-4 col-span-5 col-span-6 col-span-7 col-span-8 col-span-9 col-span-10 col-span-11 col-span-12 col-span-full");
        className("col-end-auto col-end-1 col-end-2 col-end-3 col-end-4 col-end-5 col-end-6 col-end-7 col-end-8 col-end-9 col-end-10 col-end-11 col-end-12 col-end-13");
        className("col-start-auto col-start-1 col-start-2 col-start-3 col-start-4 col-start-5 col-start-6 col-start-7 col-start-8 col-start-9 col-start-10 col-start-11 col-start-12 col-start-13");
      }
    }

    test(
        Subject.class,

        """
        .col-auto { grid-column: auto }
        .col-span-1 { grid-column: span 1 / span 1 }
        .col-span-2 { grid-column: span 2 / span 2 }
        .col-span-3 { grid-column: span 3 / span 3 }
        .col-span-4 { grid-column: span 4 / span 4 }
        .col-span-5 { grid-column: span 5 / span 5 }
        .col-span-6 { grid-column: span 6 / span 6 }
        .col-span-7 { grid-column: span 7 / span 7 }
        .col-span-8 { grid-column: span 8 / span 8 }
        .col-span-9 { grid-column: span 9 / span 9 }
        .col-span-10 { grid-column: span 10 / span 10 }
        .col-span-11 { grid-column: span 11 / span 11 }
        .col-span-12 { grid-column: span 12 / span 12 }
        .col-span-full { grid-column: 1 / -1 }
        .col-start-auto { grid-column-start: auto }
        .col-start-1 { grid-column-start: 1 }
        .col-start-2 { grid-column-start: 2 }
        .col-start-3 { grid-column-start: 3 }
        .col-start-4 { grid-column-start: 4 }
        .col-start-5 { grid-column-start: 5 }
        .col-start-6 { grid-column-start: 6 }
        .col-start-7 { grid-column-start: 7 }
        .col-start-8 { grid-column-start: 8 }
        .col-start-9 { grid-column-start: 9 }
        .col-start-10 { grid-column-start: 10 }
        .col-start-11 { grid-column-start: 11 }
        .col-start-12 { grid-column-start: 12 }
        .col-start-13 { grid-column-start: 13 }
        .col-end-auto { grid-column-end: auto }
        .col-end-1 { grid-column-end: 1 }
        .col-end-2 { grid-column-end: 2 }
        .col-end-3 { grid-column-end: 3 }
        .col-end-4 { grid-column-end: 4 }
        .col-end-5 { grid-column-end: 5 }
        .col-end-6 { grid-column-end: 6 }
        .col-end-7 { grid-column-end: 7 }
        .col-end-8 { grid-column-end: 8 }
        .col-end-9 { grid-column-end: 9 }
        .col-end-10 { grid-column-end: 10 }
        .col-end-11 { grid-column-end: 11 }
        .col-end-12 { grid-column-end: 12 }
        .col-end-13 { grid-column-end: 13 }
        """
    );
  }

  @Test
  public void gridTemplateColumns() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("grid-cols-none grid-cols-subgrid");
        className("grid-cols-1 grid-cols-2 grid-cols-3 grid-cols-4 grid-cols-5 grid-cols-6 grid-cols-7 grid-cols-8 grid-cols-9 grid-cols-10 grid-cols-11 grid-cols-12");
        className("grid-cols-[200px_minmax(900px,_1fr)_100px]");
      }
    }

    test(
        Subject.class,

        """
        .grid-cols-none { grid-template-columns: none }
        .grid-cols-subgrid { grid-template-columns: subgrid }
        .grid-cols-1 { grid-template-columns: repeat(1, minmax(0, 1fr)) }
        .grid-cols-2 { grid-template-columns: repeat(2, minmax(0, 1fr)) }
        .grid-cols-3 { grid-template-columns: repeat(3, minmax(0, 1fr)) }
        .grid-cols-4 { grid-template-columns: repeat(4, minmax(0, 1fr)) }
        .grid-cols-5 { grid-template-columns: repeat(5, minmax(0, 1fr)) }
        .grid-cols-6 { grid-template-columns: repeat(6, minmax(0, 1fr)) }
        .grid-cols-7 { grid-template-columns: repeat(7, minmax(0, 1fr)) }
        .grid-cols-8 { grid-template-columns: repeat(8, minmax(0, 1fr)) }
        .grid-cols-9 { grid-template-columns: repeat(9, minmax(0, 1fr)) }
        .grid-cols-10 { grid-template-columns: repeat(10, minmax(0, 1fr)) }
        .grid-cols-11 { grid-template-columns: repeat(11, minmax(0, 1fr)) }
        .grid-cols-12 { grid-template-columns: repeat(12, minmax(0, 1fr)) }
        .grid-cols-\\[200px_minmax\\(900px\\2c _1fr\\)_100px\\] { grid-template-columns: 200px minmax(900px, 1fr) 100px }
        """
    );
  }

  @Test
  public void gridTemplateRows() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("grid-rows-none grid-rows-subgrid");
        className("grid-rows-1 grid-rows-2 grid-rows-3 grid-rows-4 grid-rows-5 grid-rows-6 grid-rows-7 grid-rows-8 grid-rows-9 grid-rows-10 grid-rows-11 grid-rows-12");
        className("grid-rows-[200px_minmax(900px,_1fr)_100px]");
      }
    }

    test(
        Subject.class,

        """
        .grid-rows-none { grid-template-rows: none }
        .grid-rows-subgrid { grid-template-rows: subgrid }
        .grid-rows-1 { grid-template-rows: repeat(1, minmax(0, 1fr)) }
        .grid-rows-2 { grid-template-rows: repeat(2, minmax(0, 1fr)) }
        .grid-rows-3 { grid-template-rows: repeat(3, minmax(0, 1fr)) }
        .grid-rows-4 { grid-template-rows: repeat(4, minmax(0, 1fr)) }
        .grid-rows-5 { grid-template-rows: repeat(5, minmax(0, 1fr)) }
        .grid-rows-6 { grid-template-rows: repeat(6, minmax(0, 1fr)) }
        .grid-rows-7 { grid-template-rows: repeat(7, minmax(0, 1fr)) }
        .grid-rows-8 { grid-template-rows: repeat(8, minmax(0, 1fr)) }
        .grid-rows-9 { grid-template-rows: repeat(9, minmax(0, 1fr)) }
        .grid-rows-10 { grid-template-rows: repeat(10, minmax(0, 1fr)) }
        .grid-rows-11 { grid-template-rows: repeat(11, minmax(0, 1fr)) }
        .grid-rows-12 { grid-template-rows: repeat(12, minmax(0, 1fr)) }
        .grid-rows-\\[200px_minmax\\(900px\\2c _1fr\\)_100px\\] { grid-template-rows: 200px minmax(900px, 1fr) 100px }
        """
    );
  }

  @Test
  public void height() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("h-auto");

        className("h-px");
        className("h-0");
        className("h-0.5");
        className("h-1");
        className("h-1.5");
        className("h-2");
        className("h-2.5");
        className("h-3");
        className("h-3.5");
        className("h-4");
        className("h-5");
        className("h-6");
        className("h-7");
        className("h-8");
        className("h-9");
        className("h-10");
        className("h-11");
        className("h-12");
        className("h-14");
        className("h-16");
        className("h-20");
        className("h-24");
        className("h-28");
        className("h-32");
        className("h-36");
        className("h-40");
        className("h-44");
        className("h-48");
        className("h-52");
        className("h-56");
        className("h-60");
        className("h-64");
        className("h-72");
        className("h-80");
        className("h-96");

        className("h-1/2");
        className("h-1/3");
        className("h-2/3");
        className("h-1/4");
        className("h-2/4");
        className("h-3/4");
        className("h-1/5");
        className("h-2/5");
        className("h-3/5");
        className("h-4/5");
        className("h-1/6");
        className("h-2/6");
        className("h-3/6");
        className("h-4/6");
        className("h-5/6");
        className("h-full");
        className("h-screen");
        className("h-svh");
        className("h-lvh");
        className("h-dvh");
        className("h-min");
        className("h-max");
        className("h-fit");
      }
    }

    test(
        Subject.class,

        """
        .h-auto { height: auto }
        .h-px { height: 1px }
        .h-0 { height: 0px }
        .h-0\\.5 { height: 0.125rem }
        .h-1 { height: 0.25rem }
        .h-1\\.5 { height: 0.375rem }
        .h-2 { height: 0.5rem }
        .h-2\\.5 { height: 0.625rem }
        .h-3 { height: 0.75rem }
        .h-3\\.5 { height: 0.875rem }
        .h-4 { height: 1rem }
        .h-5 { height: 1.25rem }
        .h-6 { height: 1.5rem }
        .h-7 { height: 1.75rem }
        .h-8 { height: 2rem }
        .h-9 { height: 2.25rem }
        .h-10 { height: 2.5rem }
        .h-11 { height: 2.75rem }
        .h-12 { height: 3rem }
        .h-14 { height: 3.5rem }
        .h-16 { height: 4rem }
        .h-20 { height: 5rem }
        .h-24 { height: 6rem }
        .h-28 { height: 7rem }
        .h-32 { height: 8rem }
        .h-36 { height: 9rem }
        .h-40 { height: 10rem }
        .h-44 { height: 11rem }
        .h-48 { height: 12rem }
        .h-52 { height: 13rem }
        .h-56 { height: 14rem }
        .h-60 { height: 15rem }
        .h-64 { height: 16rem }
        .h-72 { height: 18rem }
        .h-80 { height: 20rem }
        .h-96 { height: 24rem }
        .h-1\\/2 { height: 50% }
        .h-1\\/3 { height: 33.333333% }
        .h-2\\/3 { height: 66.666667% }
        .h-1\\/4 { height: 25% }
        .h-2\\/4 { height: 50% }
        .h-3\\/4 { height: 75% }
        .h-1\\/5 { height: 20% }
        .h-2\\/5 { height: 40% }
        .h-3\\/5 { height: 60% }
        .h-4\\/5 { height: 80% }
        .h-1\\/6 { height: 16.666667% }
        .h-2\\/6 { height: 33.333333% }
        .h-3\\/6 { height: 50% }
        .h-4\\/6 { height: 66.666667% }
        .h-5\\/6 { height: 83.333333% }
        .h-full { height: 100% }
        .h-screen { height: 100vh }
        .h-svh { height: 100svh }
        .h-lvh { height: 100lvh }
        .h-dvh { height: 100dvh }
        .h-min { height: min-content }
        .h-max { height: max-content }
        .h-fit { height: fit-content }
        """
    );
  }

  @Test
  public void inset() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("inset-auto inset-1/2 inset-1/3 inset-2/3 inset-1/4 inset-2/4 inset-3/4 inset-full");
        className("inset-px inset-0 inset-0.5 inset-1 inset-1.5 inset-2 inset-2.5 inset-3 inset-3.5 inset-4 inset-5 inset-6 inset-7 inset-8 inset-9 inset-10 inset-11 inset-12 inset-14 inset-16 inset-20 inset-24 inset-28 inset-32 inset-36 inset-40 inset-44 inset-48 inset-52 inset-56 inset-60 inset-64 inset-72 inset-80 inset-96");
        className("inset-x-auto inset-x-1/2 inset-x-1/3 inset-x-2/3 inset-x-1/4 inset-x-2/4 inset-x-3/4 inset-x-full");
        className("inset-x-px inset-x-0 inset-x-0.5 inset-x-1 inset-x-1.5 inset-x-2 inset-x-2.5 inset-x-3 inset-x-3.5 inset-x-4 inset-x-5 inset-x-6 inset-x-7 inset-x-8 inset-x-9 inset-x-10 inset-x-11 inset-x-12 inset-x-14 inset-x-16 inset-x-20 inset-x-24 inset-x-28 inset-x-32 inset-x-36 inset-x-40 inset-x-44 inset-x-48 inset-x-52 inset-x-56 inset-x-60 inset-x-64 inset-x-72 inset-x-80 inset-x-96");
        className("inset-y-auto inset-y-1/2 inset-y-1/3 inset-y-2/3 inset-y-1/4 inset-y-2/4 inset-y-3/4 inset-y-full");
        className("inset-y-px inset-y-0 inset-y-0.5 inset-y-1 inset-y-1.5 inset-y-2 inset-y-2.5 inset-y-3 inset-y-3.5 inset-y-4 inset-y-5 inset-y-6 inset-y-7 inset-y-8 inset-y-9 inset-y-10 inset-y-11 inset-y-12 inset-y-14 inset-y-16 inset-y-20 inset-y-24 inset-y-28 inset-y-32 inset-y-36 inset-y-40 inset-y-44 inset-y-48 inset-y-52 inset-y-56 inset-y-60 inset-y-64 inset-y-72 inset-y-80 inset-y-96");
        className("top-auto top-1/2 top-1/3 top-2/3 top-1/4 top-2/4 top-3/4 top-full");
        className("top-px top-0 top-0.5 top-1 top-1.5 top-2 top-2.5 top-3 top-3.5 top-4 top-5 top-6 top-7 top-8 top-9 top-10 top-11 top-12 top-14 top-16 top-20 top-24 top-28 top-32 top-36 top-40 top-44 top-48 top-52 top-56 top-60 top-64 top-72 top-80 top-96");
        className("right-auto right-1/2 right-1/3 right-2/3 right-1/4 right-2/4 right-3/4 right-full");
        className("right-px right-0 right-0.5 right-1 right-1.5 right-2 right-2.5 right-3 right-3.5 right-4 right-5 right-6 right-7 right-8 right-9 right-10 right-11 right-12 right-14 right-16 right-20 right-24 right-28 right-32 right-36 right-40 right-44 right-48 right-52 right-56 right-60 right-64 right-72 right-80 right-96");
        className("bottom-auto bottom-1/2 bottom-1/3 bottom-2/3 bottom-1/4 bottom-2/4 bottom-3/4 bottom-full");
        className("bottom-px bottom-0 bottom-0.5 bottom-1 bottom-1.5 bottom-2 bottom-2.5 bottom-3 bottom-3.5 bottom-4 bottom-5 bottom-6 bottom-7 bottom-8 bottom-9 bottom-10 bottom-11 bottom-12 bottom-14 bottom-16 bottom-20 bottom-24 bottom-28 bottom-32 bottom-36 bottom-40 bottom-44 bottom-48 bottom-52 bottom-56 bottom-60 bottom-64 bottom-72 bottom-80 bottom-96");
        className("left-auto left-1/2 left-1/3 left-2/3 left-1/4 left-2/4 left-3/4 left-full");
        className("left-px left-0 left-0.5 left-1 left-1.5 left-2 left-2.5 left-3 left-3.5 left-4 left-5 left-6 left-7 left-8 left-9 left-10 left-11 left-12 left-14 left-16 left-20 left-24 left-28 left-32 left-36 left-40 left-44 left-48 left-52 left-56 left-60 left-64 left-72 left-80 left-96");
        // @formatter:on
        className("-top-1 -right-2 -bottom-3 -left-4");
      }
    }

    test(
        Subject.class,

        """
        .inset-auto { inset: auto }
        .inset-1\\/2 { inset: 50% }
        .inset-1\\/3 { inset: 33.333333% }
        .inset-2\\/3 { inset: 66.666667% }
        .inset-1\\/4 { inset: 25% }
        .inset-2\\/4 { inset: 50% }
        .inset-3\\/4 { inset: 75% }
        .inset-full { inset: 100% }
        .inset-px { inset: 1px }
        .inset-0 { inset: 0px }
        .inset-0\\.5 { inset: 0.125rem }
        .inset-1 { inset: 0.25rem }
        .inset-1\\.5 { inset: 0.375rem }
        .inset-2 { inset: 0.5rem }
        .inset-2\\.5 { inset: 0.625rem }
        .inset-3 { inset: 0.75rem }
        .inset-3\\.5 { inset: 0.875rem }
        .inset-4 { inset: 1rem }
        .inset-5 { inset: 1.25rem }
        .inset-6 { inset: 1.5rem }
        .inset-7 { inset: 1.75rem }
        .inset-8 { inset: 2rem }
        .inset-9 { inset: 2.25rem }
        .inset-10 { inset: 2.5rem }
        .inset-11 { inset: 2.75rem }
        .inset-12 { inset: 3rem }
        .inset-14 { inset: 3.5rem }
        .inset-16 { inset: 4rem }
        .inset-20 { inset: 5rem }
        .inset-24 { inset: 6rem }
        .inset-28 { inset: 7rem }
        .inset-32 { inset: 8rem }
        .inset-36 { inset: 9rem }
        .inset-40 { inset: 10rem }
        .inset-44 { inset: 11rem }
        .inset-48 { inset: 12rem }
        .inset-52 { inset: 13rem }
        .inset-56 { inset: 14rem }
        .inset-60 { inset: 15rem }
        .inset-64 { inset: 16rem }
        .inset-72 { inset: 18rem }
        .inset-80 { inset: 20rem }
        .inset-96 { inset: 24rem }
        .inset-x-auto { left: auto; right: auto }
        .inset-x-1\\/2 { left: 50%; right: 50% }
        .inset-x-1\\/3 { left: 33.333333%; right: 33.333333% }
        .inset-x-2\\/3 { left: 66.666667%; right: 66.666667% }
        .inset-x-1\\/4 { left: 25%; right: 25% }
        .inset-x-2\\/4 { left: 50%; right: 50% }
        .inset-x-3\\/4 { left: 75%; right: 75% }
        .inset-x-full { left: 100%; right: 100% }
        .inset-x-px { left: 1px; right: 1px }
        .inset-x-0 { left: 0px; right: 0px }
        .inset-x-0\\.5 { left: 0.125rem; right: 0.125rem }
        .inset-x-1 { left: 0.25rem; right: 0.25rem }
        .inset-x-1\\.5 { left: 0.375rem; right: 0.375rem }
        .inset-x-2 { left: 0.5rem; right: 0.5rem }
        .inset-x-2\\.5 { left: 0.625rem; right: 0.625rem }
        .inset-x-3 { left: 0.75rem; right: 0.75rem }
        .inset-x-3\\.5 { left: 0.875rem; right: 0.875rem }
        .inset-x-4 { left: 1rem; right: 1rem }
        .inset-x-5 { left: 1.25rem; right: 1.25rem }
        .inset-x-6 { left: 1.5rem; right: 1.5rem }
        .inset-x-7 { left: 1.75rem; right: 1.75rem }
        .inset-x-8 { left: 2rem; right: 2rem }
        .inset-x-9 { left: 2.25rem; right: 2.25rem }
        .inset-x-10 { left: 2.5rem; right: 2.5rem }
        .inset-x-11 { left: 2.75rem; right: 2.75rem }
        .inset-x-12 { left: 3rem; right: 3rem }
        .inset-x-14 { left: 3.5rem; right: 3.5rem }
        .inset-x-16 { left: 4rem; right: 4rem }
        .inset-x-20 { left: 5rem; right: 5rem }
        .inset-x-24 { left: 6rem; right: 6rem }
        .inset-x-28 { left: 7rem; right: 7rem }
        .inset-x-32 { left: 8rem; right: 8rem }
        .inset-x-36 { left: 9rem; right: 9rem }
        .inset-x-40 { left: 10rem; right: 10rem }
        .inset-x-44 { left: 11rem; right: 11rem }
        .inset-x-48 { left: 12rem; right: 12rem }
        .inset-x-52 { left: 13rem; right: 13rem }
        .inset-x-56 { left: 14rem; right: 14rem }
        .inset-x-60 { left: 15rem; right: 15rem }
        .inset-x-64 { left: 16rem; right: 16rem }
        .inset-x-72 { left: 18rem; right: 18rem }
        .inset-x-80 { left: 20rem; right: 20rem }
        .inset-x-96 { left: 24rem; right: 24rem }
        .inset-y-auto { top: auto; bottom: auto }
        .inset-y-1\\/2 { top: 50%; bottom: 50% }
        .inset-y-1\\/3 { top: 33.333333%; bottom: 33.333333% }
        .inset-y-2\\/3 { top: 66.666667%; bottom: 66.666667% }
        .inset-y-1\\/4 { top: 25%; bottom: 25% }
        .inset-y-2\\/4 { top: 50%; bottom: 50% }
        .inset-y-3\\/4 { top: 75%; bottom: 75% }
        .inset-y-full { top: 100%; bottom: 100% }
        .inset-y-px { top: 1px; bottom: 1px }
        .inset-y-0 { top: 0px; bottom: 0px }
        .inset-y-0\\.5 { top: 0.125rem; bottom: 0.125rem }
        .inset-y-1 { top: 0.25rem; bottom: 0.25rem }
        .inset-y-1\\.5 { top: 0.375rem; bottom: 0.375rem }
        .inset-y-2 { top: 0.5rem; bottom: 0.5rem }
        .inset-y-2\\.5 { top: 0.625rem; bottom: 0.625rem }
        .inset-y-3 { top: 0.75rem; bottom: 0.75rem }
        .inset-y-3\\.5 { top: 0.875rem; bottom: 0.875rem }
        .inset-y-4 { top: 1rem; bottom: 1rem }
        .inset-y-5 { top: 1.25rem; bottom: 1.25rem }
        .inset-y-6 { top: 1.5rem; bottom: 1.5rem }
        .inset-y-7 { top: 1.75rem; bottom: 1.75rem }
        .inset-y-8 { top: 2rem; bottom: 2rem }
        .inset-y-9 { top: 2.25rem; bottom: 2.25rem }
        .inset-y-10 { top: 2.5rem; bottom: 2.5rem }
        .inset-y-11 { top: 2.75rem; bottom: 2.75rem }
        .inset-y-12 { top: 3rem; bottom: 3rem }
        .inset-y-14 { top: 3.5rem; bottom: 3.5rem }
        .inset-y-16 { top: 4rem; bottom: 4rem }
        .inset-y-20 { top: 5rem; bottom: 5rem }
        .inset-y-24 { top: 6rem; bottom: 6rem }
        .inset-y-28 { top: 7rem; bottom: 7rem }
        .inset-y-32 { top: 8rem; bottom: 8rem }
        .inset-y-36 { top: 9rem; bottom: 9rem }
        .inset-y-40 { top: 10rem; bottom: 10rem }
        .inset-y-44 { top: 11rem; bottom: 11rem }
        .inset-y-48 { top: 12rem; bottom: 12rem }
        .inset-y-52 { top: 13rem; bottom: 13rem }
        .inset-y-56 { top: 14rem; bottom: 14rem }
        .inset-y-60 { top: 15rem; bottom: 15rem }
        .inset-y-64 { top: 16rem; bottom: 16rem }
        .inset-y-72 { top: 18rem; bottom: 18rem }
        .inset-y-80 { top: 20rem; bottom: 20rem }
        .inset-y-96 { top: 24rem; bottom: 24rem }
        .top-auto { top: auto }
        .top-1\\/2 { top: 50% }
        .top-1\\/3 { top: 33.333333% }
        .top-2\\/3 { top: 66.666667% }
        .top-1\\/4 { top: 25% }
        .top-2\\/4 { top: 50% }
        .top-3\\/4 { top: 75% }
        .top-full { top: 100% }
        .top-px { top: 1px }
        .top-0 { top: 0px }
        .top-0\\.5 { top: 0.125rem }
        .top-1 { top: 0.25rem }
        .top-1\\.5 { top: 0.375rem }
        .top-2 { top: 0.5rem }
        .top-2\\.5 { top: 0.625rem }
        .top-3 { top: 0.75rem }
        .top-3\\.5 { top: 0.875rem }
        .top-4 { top: 1rem }
        .top-5 { top: 1.25rem }
        .top-6 { top: 1.5rem }
        .top-7 { top: 1.75rem }
        .top-8 { top: 2rem }
        .top-9 { top: 2.25rem }
        .top-10 { top: 2.5rem }
        .top-11 { top: 2.75rem }
        .top-12 { top: 3rem }
        .top-14 { top: 3.5rem }
        .top-16 { top: 4rem }
        .top-20 { top: 5rem }
        .top-24 { top: 6rem }
        .top-28 { top: 7rem }
        .top-32 { top: 8rem }
        .top-36 { top: 9rem }
        .top-40 { top: 10rem }
        .top-44 { top: 11rem }
        .top-48 { top: 12rem }
        .top-52 { top: 13rem }
        .top-56 { top: 14rem }
        .top-60 { top: 15rem }
        .top-64 { top: 16rem }
        .top-72 { top: 18rem }
        .top-80 { top: 20rem }
        .top-96 { top: 24rem }
        .-top-1 { top: -0.25rem }
        .right-auto { right: auto }
        .right-1\\/2 { right: 50% }
        .right-1\\/3 { right: 33.333333% }
        .right-2\\/3 { right: 66.666667% }
        .right-1\\/4 { right: 25% }
        .right-2\\/4 { right: 50% }
        .right-3\\/4 { right: 75% }
        .right-full { right: 100% }
        .right-px { right: 1px }
        .right-0 { right: 0px }
        .right-0\\.5 { right: 0.125rem }
        .right-1 { right: 0.25rem }
        .right-1\\.5 { right: 0.375rem }
        .right-2 { right: 0.5rem }
        .right-2\\.5 { right: 0.625rem }
        .right-3 { right: 0.75rem }
        .right-3\\.5 { right: 0.875rem }
        .right-4 { right: 1rem }
        .right-5 { right: 1.25rem }
        .right-6 { right: 1.5rem }
        .right-7 { right: 1.75rem }
        .right-8 { right: 2rem }
        .right-9 { right: 2.25rem }
        .right-10 { right: 2.5rem }
        .right-11 { right: 2.75rem }
        .right-12 { right: 3rem }
        .right-14 { right: 3.5rem }
        .right-16 { right: 4rem }
        .right-20 { right: 5rem }
        .right-24 { right: 6rem }
        .right-28 { right: 7rem }
        .right-32 { right: 8rem }
        .right-36 { right: 9rem }
        .right-40 { right: 10rem }
        .right-44 { right: 11rem }
        .right-48 { right: 12rem }
        .right-52 { right: 13rem }
        .right-56 { right: 14rem }
        .right-60 { right: 15rem }
        .right-64 { right: 16rem }
        .right-72 { right: 18rem }
        .right-80 { right: 20rem }
        .right-96 { right: 24rem }
        .-right-2 { right: -0.5rem }
        .bottom-auto { bottom: auto }
        .bottom-1\\/2 { bottom: 50% }
        .bottom-1\\/3 { bottom: 33.333333% }
        .bottom-2\\/3 { bottom: 66.666667% }
        .bottom-1\\/4 { bottom: 25% }
        .bottom-2\\/4 { bottom: 50% }
        .bottom-3\\/4 { bottom: 75% }
        .bottom-full { bottom: 100% }
        .bottom-px { bottom: 1px }
        .bottom-0 { bottom: 0px }
        .bottom-0\\.5 { bottom: 0.125rem }
        .bottom-1 { bottom: 0.25rem }
        .bottom-1\\.5 { bottom: 0.375rem }
        .bottom-2 { bottom: 0.5rem }
        .bottom-2\\.5 { bottom: 0.625rem }
        .bottom-3 { bottom: 0.75rem }
        .bottom-3\\.5 { bottom: 0.875rem }
        .bottom-4 { bottom: 1rem }
        .bottom-5 { bottom: 1.25rem }
        .bottom-6 { bottom: 1.5rem }
        .bottom-7 { bottom: 1.75rem }
        .bottom-8 { bottom: 2rem }
        .bottom-9 { bottom: 2.25rem }
        .bottom-10 { bottom: 2.5rem }
        .bottom-11 { bottom: 2.75rem }
        .bottom-12 { bottom: 3rem }
        .bottom-14 { bottom: 3.5rem }
        .bottom-16 { bottom: 4rem }
        .bottom-20 { bottom: 5rem }
        .bottom-24 { bottom: 6rem }
        .bottom-28 { bottom: 7rem }
        .bottom-32 { bottom: 8rem }
        .bottom-36 { bottom: 9rem }
        .bottom-40 { bottom: 10rem }
        .bottom-44 { bottom: 11rem }
        .bottom-48 { bottom: 12rem }
        .bottom-52 { bottom: 13rem }
        .bottom-56 { bottom: 14rem }
        .bottom-60 { bottom: 15rem }
        .bottom-64 { bottom: 16rem }
        .bottom-72 { bottom: 18rem }
        .bottom-80 { bottom: 20rem }
        .bottom-96 { bottom: 24rem }
        .-bottom-3 { bottom: -0.75rem }
        .left-auto { left: auto }
        .left-1\\/2 { left: 50% }
        .left-1\\/3 { left: 33.333333% }
        .left-2\\/3 { left: 66.666667% }
        .left-1\\/4 { left: 25% }
        .left-2\\/4 { left: 50% }
        .left-3\\/4 { left: 75% }
        .left-full { left: 100% }
        .left-px { left: 1px }
        .left-0 { left: 0px }
        .left-0\\.5 { left: 0.125rem }
        .left-1 { left: 0.25rem }
        .left-1\\.5 { left: 0.375rem }
        .left-2 { left: 0.5rem }
        .left-2\\.5 { left: 0.625rem }
        .left-3 { left: 0.75rem }
        .left-3\\.5 { left: 0.875rem }
        .left-4 { left: 1rem }
        .left-5 { left: 1.25rem }
        .left-6 { left: 1.5rem }
        .left-7 { left: 1.75rem }
        .left-8 { left: 2rem }
        .left-9 { left: 2.25rem }
        .left-10 { left: 2.5rem }
        .left-11 { left: 2.75rem }
        .left-12 { left: 3rem }
        .left-14 { left: 3.5rem }
        .left-16 { left: 4rem }
        .left-20 { left: 5rem }
        .left-24 { left: 6rem }
        .left-28 { left: 7rem }
        .left-32 { left: 8rem }
        .left-36 { left: 9rem }
        .left-40 { left: 10rem }
        .left-44 { left: 11rem }
        .left-48 { left: 12rem }
        .left-52 { left: 13rem }
        .left-56 { left: 14rem }
        .left-60 { left: 15rem }
        .left-64 { left: 16rem }
        .left-72 { left: 18rem }
        .left-80 { left: 20rem }
        .left-96 { left: 24rem }
        .-left-4 { left: -1rem }
        """
    );
  }

  @Test
  public void justifyContent() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("justify-normal justify-start justify-end justify-center justify-between justify-around justify-evenly justify-stretch");
      }
    }

    test(
        Subject.class,

        """
        .justify-normal { justify-content: normal }
        .justify-start { justify-content: flex-start }
        .justify-end { justify-content: flex-end }
        .justify-center { justify-content: center }
        .justify-between { justify-content: space-between }
        .justify-around { justify-content: space-around }
        .justify-evenly { justify-content: space-evenly }
        .justify-stretch { justify-content: stretch }
        """
    );
  }

  @Test
  public void letterSpacing() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("tracking-tighter");
        className("tracking-tight");
        className("tracking-normal");
        className("tracking-wide");
        className("tracking-wider");
        className("tracking-widest");
        className("-tracking-widest");
      }
    }

    test(
        Subject.class,

        """
        .tracking-tighter { letter-spacing: -0.05em }
        .tracking-tight { letter-spacing: -0.025em }
        .tracking-normal { letter-spacing: 0em }
        .tracking-wide { letter-spacing: 0.025em }
        .tracking-wider { letter-spacing: 0.05em }
        .tracking-widest { letter-spacing: 0.1em }
        .-tracking-widest { letter-spacing: -0.1em }
        """
    );
  }

  @Test
  public void lineHeight() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("leading-3 leading-4 leading-5 leading-6 leading-7 leading-8 leading-9 leading-10 leading-none leading-tight leading-snug leading-normal leading-relaxed leading-loose");
        className("leading-[1.45] leading-[3]");
      }
    }

    test(
        Subject.class,

        """
        .leading-3 { line-height: 0.75rem }
        .leading-4 { line-height: 1rem }
        .leading-5 { line-height: 1.25rem }
        .leading-6 { line-height: 1.5rem }
        .leading-7 { line-height: 1.75rem }
        .leading-8 { line-height: 2rem }
        .leading-9 { line-height: 2.25rem }
        .leading-10 { line-height: 2.5rem }
        .leading-none { line-height: 1 }
        .leading-tight { line-height: 1.25 }
        .leading-snug { line-height: 1.375 }
        .leading-normal { line-height: 1.5 }
        .leading-relaxed { line-height: 1.625 }
        .leading-loose { line-height: 2 }
        .leading-\\[1\\.45\\] { line-height: 1.45 }
        .leading-\\[3\\] { line-height: 3 }
        """
    );
  }

  @Test
  public void listStyleType() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("list-none list-disc list-decimal list-[upper-roman]");
      }
    }

    test(
        Subject.class,

        """
        .list-none { list-style-type: none }
        .list-disc { list-style-type: disc }
        .list-decimal { list-style-type: decimal }
        .list-\\[upper-roman\\] { list-style-type: upper-roman }
        """
    );
  }

  @Test
  public void margin() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("m-auto m-px m-0 m-0.5 m-1 m-1.5 m-2 m-2.5 m-3 m-3.5 m-4 m-5 m-6 m-7 m-8 m-9 m-10 m-11 m-12 m-14 m-16 m-20 m-24 m-28 m-32 m-36 m-40 m-44 m-48 m-52 m-56 m-60 m-64 m-72 m-80 m-96");
        className("mx-auto mx-px mx-0 mx-0.5 mx-1 mx-1.5 mx-2 mx-2.5 mx-3 mx-3.5 mx-4 mx-5 mx-6 mx-7 mx-8 mx-9 mx-10 mx-11 mx-12 mx-14 mx-16 mx-20 mx-24 mx-28 mx-32 mx-36 mx-40 mx-44 mx-48 mx-52 mx-56 mx-60 mx-64 mx-72 mx-80 mx-96");
        className("my-auto my-px my-0 my-0.5 my-1 my-1.5 my-2 my-2.5 my-3 my-3.5 my-4 my-5 my-6 my-7 my-8 my-9 my-10 my-11 my-12 my-14 my-16 my-20 my-24 my-28 my-32 my-36 my-40 my-44 my-48 my-52 my-56 my-60 my-64 my-72 my-80 my-96");
        className("mt-auto mt-px mt-0 mt-0.5 mt-1 mt-1.5 mt-2 mt-2.5 mt-3 mt-3.5 mt-4 mt-5 mt-6 mt-7 mt-8 mt-9 mt-10 mt-11 mt-12 mt-14 mt-16 mt-20 mt-24 mt-28 mt-32 mt-36 mt-40 mt-44 mt-48 mt-52 mt-56 mt-60 mt-64 mt-72 mt-80 mt-96");
        className("mr-auto mr-px mr-0 mr-0.5 mr-1 mr-1.5 mr-2 mr-2.5 mr-3 mr-3.5 mr-4 mr-5 mr-6 mr-7 mr-8 mr-9 mr-10 mr-11 mr-12 mr-14 mr-16 mr-20 mr-24 mr-28 mr-32 mr-36 mr-40 mr-44 mr-48 mr-52 mr-56 mr-60 mr-64 mr-72 mr-80 mr-96");
        className("mb-auto mb-px mb-0 mb-0.5 mb-1 mb-1.5 mb-2 mb-2.5 mb-3 mb-3.5 mb-4 mb-5 mb-6 mb-7 mb-8 mb-9 mb-10 mb-11 mb-12 mb-14 mb-16 mb-20 mb-24 mb-28 mb-32 mb-36 mb-40 mb-44 mb-48 mb-52 mb-56 mb-60 mb-64 mb-72 mb-80 mb-96");
        className("ml-auto ml-px ml-0 ml-0.5 ml-1 ml-1.5 ml-2 ml-2.5 ml-3 ml-3.5 ml-4 ml-5 ml-6 ml-7 ml-8 ml-9 ml-10 ml-11 ml-12 ml-14 ml-16 ml-20 ml-24 ml-28 ml-32 ml-36 ml-40 ml-44 ml-48 ml-52 ml-56 ml-60 ml-64 ml-72 ml-80 ml-96");
        className("-m-px -mx-1 -my-2 -mt-3 -mr-4 -mb-5 -ml-6");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .m-auto { margin: auto }
        .m-px { margin: 1px }
        .m-0 { margin: 0px }
        .m-0\\.5 { margin: 0.125rem }
        .m-1 { margin: 0.25rem }
        .m-1\\.5 { margin: 0.375rem }
        .m-2 { margin: 0.5rem }
        .m-2\\.5 { margin: 0.625rem }
        .m-3 { margin: 0.75rem }
        .m-3\\.5 { margin: 0.875rem }
        .m-4 { margin: 1rem }
        .m-5 { margin: 1.25rem }
        .m-6 { margin: 1.5rem }
        .m-7 { margin: 1.75rem }
        .m-8 { margin: 2rem }
        .m-9 { margin: 2.25rem }
        .m-10 { margin: 2.5rem }
        .m-11 { margin: 2.75rem }
        .m-12 { margin: 3rem }
        .m-14 { margin: 3.5rem }
        .m-16 { margin: 4rem }
        .m-20 { margin: 5rem }
        .m-24 { margin: 6rem }
        .m-28 { margin: 7rem }
        .m-32 { margin: 8rem }
        .m-36 { margin: 9rem }
        .m-40 { margin: 10rem }
        .m-44 { margin: 11rem }
        .m-48 { margin: 12rem }
        .m-52 { margin: 13rem }
        .m-56 { margin: 14rem }
        .m-60 { margin: 15rem }
        .m-64 { margin: 16rem }
        .m-72 { margin: 18rem }
        .m-80 { margin: 20rem }
        .m-96 { margin: 24rem }
        .-m-px { margin: -1px }
        .mx-auto { margin-left: auto; margin-right: auto }
        .mx-px { margin-left: 1px; margin-right: 1px }
        .mx-0 { margin-left: 0px; margin-right: 0px }
        .mx-0\\.5 { margin-left: 0.125rem; margin-right: 0.125rem }
        .mx-1 { margin-left: 0.25rem; margin-right: 0.25rem }
        .mx-1\\.5 { margin-left: 0.375rem; margin-right: 0.375rem }
        .mx-2 { margin-left: 0.5rem; margin-right: 0.5rem }
        .mx-2\\.5 { margin-left: 0.625rem; margin-right: 0.625rem }
        .mx-3 { margin-left: 0.75rem; margin-right: 0.75rem }
        .mx-3\\.5 { margin-left: 0.875rem; margin-right: 0.875rem }
        .mx-4 { margin-left: 1rem; margin-right: 1rem }
        .mx-5 { margin-left: 1.25rem; margin-right: 1.25rem }
        .mx-6 { margin-left: 1.5rem; margin-right: 1.5rem }
        .mx-7 { margin-left: 1.75rem; margin-right: 1.75rem }
        .mx-8 { margin-left: 2rem; margin-right: 2rem }
        .mx-9 { margin-left: 2.25rem; margin-right: 2.25rem }
        .mx-10 { margin-left: 2.5rem; margin-right: 2.5rem }
        .mx-11 { margin-left: 2.75rem; margin-right: 2.75rem }
        .mx-12 { margin-left: 3rem; margin-right: 3rem }
        .mx-14 { margin-left: 3.5rem; margin-right: 3.5rem }
        .mx-16 { margin-left: 4rem; margin-right: 4rem }
        .mx-20 { margin-left: 5rem; margin-right: 5rem }
        .mx-24 { margin-left: 6rem; margin-right: 6rem }
        .mx-28 { margin-left: 7rem; margin-right: 7rem }
        .mx-32 { margin-left: 8rem; margin-right: 8rem }
        .mx-36 { margin-left: 9rem; margin-right: 9rem }
        .mx-40 { margin-left: 10rem; margin-right: 10rem }
        .mx-44 { margin-left: 11rem; margin-right: 11rem }
        .mx-48 { margin-left: 12rem; margin-right: 12rem }
        .mx-52 { margin-left: 13rem; margin-right: 13rem }
        .mx-56 { margin-left: 14rem; margin-right: 14rem }
        .mx-60 { margin-left: 15rem; margin-right: 15rem }
        .mx-64 { margin-left: 16rem; margin-right: 16rem }
        .mx-72 { margin-left: 18rem; margin-right: 18rem }
        .mx-80 { margin-left: 20rem; margin-right: 20rem }
        .mx-96 { margin-left: 24rem; margin-right: 24rem }
        .-mx-1 { margin-left: -0.25rem; margin-right: -0.25rem }
        .my-auto { margin-top: auto; margin-bottom: auto }
        .my-px { margin-top: 1px; margin-bottom: 1px }
        .my-0 { margin-top: 0px; margin-bottom: 0px }
        .my-0\\.5 { margin-top: 0.125rem; margin-bottom: 0.125rem }
        .my-1 { margin-top: 0.25rem; margin-bottom: 0.25rem }
        .my-1\\.5 { margin-top: 0.375rem; margin-bottom: 0.375rem }
        .my-2 { margin-top: 0.5rem; margin-bottom: 0.5rem }
        .my-2\\.5 { margin-top: 0.625rem; margin-bottom: 0.625rem }
        .my-3 { margin-top: 0.75rem; margin-bottom: 0.75rem }
        .my-3\\.5 { margin-top: 0.875rem; margin-bottom: 0.875rem }
        .my-4 { margin-top: 1rem; margin-bottom: 1rem }
        .my-5 { margin-top: 1.25rem; margin-bottom: 1.25rem }
        .my-6 { margin-top: 1.5rem; margin-bottom: 1.5rem }
        .my-7 { margin-top: 1.75rem; margin-bottom: 1.75rem }
        .my-8 { margin-top: 2rem; margin-bottom: 2rem }
        .my-9 { margin-top: 2.25rem; margin-bottom: 2.25rem }
        .my-10 { margin-top: 2.5rem; margin-bottom: 2.5rem }
        .my-11 { margin-top: 2.75rem; margin-bottom: 2.75rem }
        .my-12 { margin-top: 3rem; margin-bottom: 3rem }
        .my-14 { margin-top: 3.5rem; margin-bottom: 3.5rem }
        .my-16 { margin-top: 4rem; margin-bottom: 4rem }
        .my-20 { margin-top: 5rem; margin-bottom: 5rem }
        .my-24 { margin-top: 6rem; margin-bottom: 6rem }
        .my-28 { margin-top: 7rem; margin-bottom: 7rem }
        .my-32 { margin-top: 8rem; margin-bottom: 8rem }
        .my-36 { margin-top: 9rem; margin-bottom: 9rem }
        .my-40 { margin-top: 10rem; margin-bottom: 10rem }
        .my-44 { margin-top: 11rem; margin-bottom: 11rem }
        .my-48 { margin-top: 12rem; margin-bottom: 12rem }
        .my-52 { margin-top: 13rem; margin-bottom: 13rem }
        .my-56 { margin-top: 14rem; margin-bottom: 14rem }
        .my-60 { margin-top: 15rem; margin-bottom: 15rem }
        .my-64 { margin-top: 16rem; margin-bottom: 16rem }
        .my-72 { margin-top: 18rem; margin-bottom: 18rem }
        .my-80 { margin-top: 20rem; margin-bottom: 20rem }
        .my-96 { margin-top: 24rem; margin-bottom: 24rem }
        .-my-2 { margin-top: -0.5rem; margin-bottom: -0.5rem }
        .mt-auto { margin-top: auto }
        .mt-px { margin-top: 1px }
        .mt-0 { margin-top: 0px }
        .mt-0\\.5 { margin-top: 0.125rem }
        .mt-1 { margin-top: 0.25rem }
        .mt-1\\.5 { margin-top: 0.375rem }
        .mt-2 { margin-top: 0.5rem }
        .mt-2\\.5 { margin-top: 0.625rem }
        .mt-3 { margin-top: 0.75rem }
        .mt-3\\.5 { margin-top: 0.875rem }
        .mt-4 { margin-top: 1rem }
        .mt-5 { margin-top: 1.25rem }
        .mt-6 { margin-top: 1.5rem }
        .mt-7 { margin-top: 1.75rem }
        .mt-8 { margin-top: 2rem }
        .mt-9 { margin-top: 2.25rem }
        .mt-10 { margin-top: 2.5rem }
        .mt-11 { margin-top: 2.75rem }
        .mt-12 { margin-top: 3rem }
        .mt-14 { margin-top: 3.5rem }
        .mt-16 { margin-top: 4rem }
        .mt-20 { margin-top: 5rem }
        .mt-24 { margin-top: 6rem }
        .mt-28 { margin-top: 7rem }
        .mt-32 { margin-top: 8rem }
        .mt-36 { margin-top: 9rem }
        .mt-40 { margin-top: 10rem }
        .mt-44 { margin-top: 11rem }
        .mt-48 { margin-top: 12rem }
        .mt-52 { margin-top: 13rem }
        .mt-56 { margin-top: 14rem }
        .mt-60 { margin-top: 15rem }
        .mt-64 { margin-top: 16rem }
        .mt-72 { margin-top: 18rem }
        .mt-80 { margin-top: 20rem }
        .mt-96 { margin-top: 24rem }
        .-mt-3 { margin-top: -0.75rem }
        .mr-auto { margin-right: auto }
        .mr-px { margin-right: 1px }
        .mr-0 { margin-right: 0px }
        .mr-0\\.5 { margin-right: 0.125rem }
        .mr-1 { margin-right: 0.25rem }
        .mr-1\\.5 { margin-right: 0.375rem }
        .mr-2 { margin-right: 0.5rem }
        .mr-2\\.5 { margin-right: 0.625rem }
        .mr-3 { margin-right: 0.75rem }
        .mr-3\\.5 { margin-right: 0.875rem }
        .mr-4 { margin-right: 1rem }
        .mr-5 { margin-right: 1.25rem }
        .mr-6 { margin-right: 1.5rem }
        .mr-7 { margin-right: 1.75rem }
        .mr-8 { margin-right: 2rem }
        .mr-9 { margin-right: 2.25rem }
        .mr-10 { margin-right: 2.5rem }
        .mr-11 { margin-right: 2.75rem }
        .mr-12 { margin-right: 3rem }
        .mr-14 { margin-right: 3.5rem }
        .mr-16 { margin-right: 4rem }
        .mr-20 { margin-right: 5rem }
        .mr-24 { margin-right: 6rem }
        .mr-28 { margin-right: 7rem }
        .mr-32 { margin-right: 8rem }
        .mr-36 { margin-right: 9rem }
        .mr-40 { margin-right: 10rem }
        .mr-44 { margin-right: 11rem }
        .mr-48 { margin-right: 12rem }
        .mr-52 { margin-right: 13rem }
        .mr-56 { margin-right: 14rem }
        .mr-60 { margin-right: 15rem }
        .mr-64 { margin-right: 16rem }
        .mr-72 { margin-right: 18rem }
        .mr-80 { margin-right: 20rem }
        .mr-96 { margin-right: 24rem }
        .-mr-4 { margin-right: -1rem }
        .mb-auto { margin-bottom: auto }
        .mb-px { margin-bottom: 1px }
        .mb-0 { margin-bottom: 0px }
        .mb-0\\.5 { margin-bottom: 0.125rem }
        .mb-1 { margin-bottom: 0.25rem }
        .mb-1\\.5 { margin-bottom: 0.375rem }
        .mb-2 { margin-bottom: 0.5rem }
        .mb-2\\.5 { margin-bottom: 0.625rem }
        .mb-3 { margin-bottom: 0.75rem }
        .mb-3\\.5 { margin-bottom: 0.875rem }
        .mb-4 { margin-bottom: 1rem }
        .mb-5 { margin-bottom: 1.25rem }
        .mb-6 { margin-bottom: 1.5rem }
        .mb-7 { margin-bottom: 1.75rem }
        .mb-8 { margin-bottom: 2rem }
        .mb-9 { margin-bottom: 2.25rem }
        .mb-10 { margin-bottom: 2.5rem }
        .mb-11 { margin-bottom: 2.75rem }
        .mb-12 { margin-bottom: 3rem }
        .mb-14 { margin-bottom: 3.5rem }
        .mb-16 { margin-bottom: 4rem }
        .mb-20 { margin-bottom: 5rem }
        .mb-24 { margin-bottom: 6rem }
        .mb-28 { margin-bottom: 7rem }
        .mb-32 { margin-bottom: 8rem }
        .mb-36 { margin-bottom: 9rem }
        .mb-40 { margin-bottom: 10rem }
        .mb-44 { margin-bottom: 11rem }
        .mb-48 { margin-bottom: 12rem }
        .mb-52 { margin-bottom: 13rem }
        .mb-56 { margin-bottom: 14rem }
        .mb-60 { margin-bottom: 15rem }
        .mb-64 { margin-bottom: 16rem }
        .mb-72 { margin-bottom: 18rem }
        .mb-80 { margin-bottom: 20rem }
        .mb-96 { margin-bottom: 24rem }
        .-mb-5 { margin-bottom: -1.25rem }
        .ml-auto { margin-left: auto }
        .ml-px { margin-left: 1px }
        .ml-0 { margin-left: 0px }
        .ml-0\\.5 { margin-left: 0.125rem }
        .ml-1 { margin-left: 0.25rem }
        .ml-1\\.5 { margin-left: 0.375rem }
        .ml-2 { margin-left: 0.5rem }
        .ml-2\\.5 { margin-left: 0.625rem }
        .ml-3 { margin-left: 0.75rem }
        .ml-3\\.5 { margin-left: 0.875rem }
        .ml-4 { margin-left: 1rem }
        .ml-5 { margin-left: 1.25rem }
        .ml-6 { margin-left: 1.5rem }
        .ml-7 { margin-left: 1.75rem }
        .ml-8 { margin-left: 2rem }
        .ml-9 { margin-left: 2.25rem }
        .ml-10 { margin-left: 2.5rem }
        .ml-11 { margin-left: 2.75rem }
        .ml-12 { margin-left: 3rem }
        .ml-14 { margin-left: 3.5rem }
        .ml-16 { margin-left: 4rem }
        .ml-20 { margin-left: 5rem }
        .ml-24 { margin-left: 6rem }
        .ml-28 { margin-left: 7rem }
        .ml-32 { margin-left: 8rem }
        .ml-36 { margin-left: 9rem }
        .ml-40 { margin-left: 10rem }
        .ml-44 { margin-left: 11rem }
        .ml-48 { margin-left: 12rem }
        .ml-52 { margin-left: 13rem }
        .ml-56 { margin-left: 14rem }
        .ml-60 { margin-left: 15rem }
        .ml-64 { margin-left: 16rem }
        .ml-72 { margin-left: 18rem }
        .ml-80 { margin-left: 20rem }
        .ml-96 { margin-left: 24rem }
        .-ml-6 { margin-left: -1.5rem }
        """
    );
  }

  @Test
  public void maxHeight() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("max-h-px max-h-0 max-h-0.5 max-h-1 max-h-1.5 max-h-2 max-h-2.5 max-h-3 max-h-3.5 max-h-4 max-h-5 max-h-6 max-h-7 max-h-8 max-h-9 max-h-10 max-h-11 max-h-12 max-h-14 max-h-16 max-h-20 max-h-24 max-h-28 max-h-32 max-h-36 max-h-40 max-h-44 max-h-48 max-h-52 max-h-56 max-h-60 max-h-64 max-h-72 max-h-80 max-h-96 max-h-none max-h-full max-h-screen max-h-svh max-h-lvh max-h-dvh max-h-min max-h-max max-h-fit");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .max-h-px { max-height: 1px }
        .max-h-0 { max-height: 0px }
        .max-h-0\\.5 { max-height: 0.125rem }
        .max-h-1 { max-height: 0.25rem }
        .max-h-1\\.5 { max-height: 0.375rem }
        .max-h-2 { max-height: 0.5rem }
        .max-h-2\\.5 { max-height: 0.625rem }
        .max-h-3 { max-height: 0.75rem }
        .max-h-3\\.5 { max-height: 0.875rem }
        .max-h-4 { max-height: 1rem }
        .max-h-5 { max-height: 1.25rem }
        .max-h-6 { max-height: 1.5rem }
        .max-h-7 { max-height: 1.75rem }
        .max-h-8 { max-height: 2rem }
        .max-h-9 { max-height: 2.25rem }
        .max-h-10 { max-height: 2.5rem }
        .max-h-11 { max-height: 2.75rem }
        .max-h-12 { max-height: 3rem }
        .max-h-14 { max-height: 3.5rem }
        .max-h-16 { max-height: 4rem }
        .max-h-20 { max-height: 5rem }
        .max-h-24 { max-height: 6rem }
        .max-h-28 { max-height: 7rem }
        .max-h-32 { max-height: 8rem }
        .max-h-36 { max-height: 9rem }
        .max-h-40 { max-height: 10rem }
        .max-h-44 { max-height: 11rem }
        .max-h-48 { max-height: 12rem }
        .max-h-52 { max-height: 13rem }
        .max-h-56 { max-height: 14rem }
        .max-h-60 { max-height: 15rem }
        .max-h-64 { max-height: 16rem }
        .max-h-72 { max-height: 18rem }
        .max-h-80 { max-height: 20rem }
        .max-h-96 { max-height: 24rem }
        .max-h-none { max-height: none }
        .max-h-full { max-height: 100% }
        .max-h-screen { max-height: 100vh }
        .max-h-svh { max-height: 100svh }
        .max-h-lvh { max-height: 100lvh }
        .max-h-dvh { max-height: 100dvh }
        .max-h-min { max-height: min-content }
        .max-h-max { max-height: max-content }
        .max-h-fit { max-height: fit-content }
        """
    );
  }

  @Test
  public void maxWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("max-w-px max-w-0 max-w-0.5 max-w-1 max-w-1.5 max-w-2 max-w-2.5 max-w-3 max-w-3.5 max-w-4 max-w-5 max-w-6 max-w-7 max-w-8 max-w-9 max-w-10 max-w-11 max-w-12 max-w-14 max-w-16 max-w-20 max-w-24 max-w-28 max-w-32 max-w-36 max-w-40 max-w-44 max-w-48 max-w-52 max-w-56 max-w-60 max-w-64 max-w-72 max-w-80 max-w-96 max-w-none max-w-xs max-w-sm max-w-md max-w-lg max-w-xl max-w-2xl max-w-3xl max-w-4xl max-w-5xl max-w-6xl max-w-7xl max-w-full max-w-min max-w-max max-w-fit max-w-prose max-w-screen-sm max-w-screen-md max-w-screen-lg max-w-screen-xl max-w-screen-2xl");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .max-w-px { max-width: 1px }
        .max-w-0 { max-width: 0px }
        .max-w-0\\.5 { max-width: 0.125rem }
        .max-w-1 { max-width: 0.25rem }
        .max-w-1\\.5 { max-width: 0.375rem }
        .max-w-2 { max-width: 0.5rem }
        .max-w-2\\.5 { max-width: 0.625rem }
        .max-w-3 { max-width: 0.75rem }
        .max-w-3\\.5 { max-width: 0.875rem }
        .max-w-4 { max-width: 1rem }
        .max-w-5 { max-width: 1.25rem }
        .max-w-6 { max-width: 1.5rem }
        .max-w-7 { max-width: 1.75rem }
        .max-w-8 { max-width: 2rem }
        .max-w-9 { max-width: 2.25rem }
        .max-w-10 { max-width: 2.5rem }
        .max-w-11 { max-width: 2.75rem }
        .max-w-12 { max-width: 3rem }
        .max-w-14 { max-width: 3.5rem }
        .max-w-16 { max-width: 4rem }
        .max-w-20 { max-width: 5rem }
        .max-w-24 { max-width: 6rem }
        .max-w-28 { max-width: 7rem }
        .max-w-32 { max-width: 8rem }
        .max-w-36 { max-width: 9rem }
        .max-w-40 { max-width: 10rem }
        .max-w-44 { max-width: 11rem }
        .max-w-48 { max-width: 12rem }
        .max-w-52 { max-width: 13rem }
        .max-w-56 { max-width: 14rem }
        .max-w-60 { max-width: 15rem }
        .max-w-64 { max-width: 16rem }
        .max-w-72 { max-width: 18rem }
        .max-w-80 { max-width: 20rem }
        .max-w-96 { max-width: 24rem }
        .max-w-none { max-width: none }
        .max-w-xs { max-width: 20rem }
        .max-w-sm { max-width: 24rem }
        .max-w-md { max-width: 28rem }
        .max-w-lg { max-width: 32rem }
        .max-w-xl { max-width: 36rem }
        .max-w-2xl { max-width: 42rem }
        .max-w-3xl { max-width: 48rem }
        .max-w-4xl { max-width: 56rem }
        .max-w-5xl { max-width: 64rem }
        .max-w-6xl { max-width: 72rem }
        .max-w-7xl { max-width: 80rem }
        .max-w-full { max-width: 100% }
        .max-w-min { max-width: min-content }
        .max-w-max { max-width: max-content }
        .max-w-fit { max-width: fit-content }
        .max-w-prose { max-width: 65ch }
        .max-w-screen-sm { max-width: 640px }
        .max-w-screen-md { max-width: 768px }
        .max-w-screen-lg { max-width: 1024px }
        .max-w-screen-xl { max-width: 1280px }
        .max-w-screen-2xl { max-width: 1536px }
        """
    );
  }

  @Test
  public void minHeight() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("min-h-px min-h-0 min-h-0.5 min-h-1 min-h-1.5 min-h-2 min-h-2.5 min-h-3 min-h-3.5 min-h-4 min-h-5 min-h-6 min-h-7 min-h-8 min-h-9 min-h-10 min-h-11 min-h-12 min-h-14 min-h-16 min-h-20 min-h-24 min-h-28 min-h-32 min-h-36 min-h-40 min-h-44 min-h-48 min-h-52 min-h-56 min-h-60 min-h-64 min-h-72 min-h-80 min-h-96 min-h-full min-h-screen min-h-svh min-h-lvh min-h-dvh min-h-min min-h-max min-h-fit");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .min-h-px { min-height: 1px }
        .min-h-0 { min-height: 0px }
        .min-h-0\\.5 { min-height: 0.125rem }
        .min-h-1 { min-height: 0.25rem }
        .min-h-1\\.5 { min-height: 0.375rem }
        .min-h-2 { min-height: 0.5rem }
        .min-h-2\\.5 { min-height: 0.625rem }
        .min-h-3 { min-height: 0.75rem }
        .min-h-3\\.5 { min-height: 0.875rem }
        .min-h-4 { min-height: 1rem }
        .min-h-5 { min-height: 1.25rem }
        .min-h-6 { min-height: 1.5rem }
        .min-h-7 { min-height: 1.75rem }
        .min-h-8 { min-height: 2rem }
        .min-h-9 { min-height: 2.25rem }
        .min-h-10 { min-height: 2.5rem }
        .min-h-11 { min-height: 2.75rem }
        .min-h-12 { min-height: 3rem }
        .min-h-14 { min-height: 3.5rem }
        .min-h-16 { min-height: 4rem }
        .min-h-20 { min-height: 5rem }
        .min-h-24 { min-height: 6rem }
        .min-h-28 { min-height: 7rem }
        .min-h-32 { min-height: 8rem }
        .min-h-36 { min-height: 9rem }
        .min-h-40 { min-height: 10rem }
        .min-h-44 { min-height: 11rem }
        .min-h-48 { min-height: 12rem }
        .min-h-52 { min-height: 13rem }
        .min-h-56 { min-height: 14rem }
        .min-h-60 { min-height: 15rem }
        .min-h-64 { min-height: 16rem }
        .min-h-72 { min-height: 18rem }
        .min-h-80 { min-height: 20rem }
        .min-h-96 { min-height: 24rem }
        .min-h-full { min-height: 100% }
        .min-h-screen { min-height: 100vh }
        .min-h-svh { min-height: 100svh }
        .min-h-lvh { min-height: 100lvh }
        .min-h-dvh { min-height: 100dvh }
        .min-h-min { min-height: min-content }
        .min-h-max { min-height: max-content }
        .min-h-fit { min-height: fit-content }
        """
    );
  }

  @Test
  public void minWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("min-w-px min-w-0 min-w-0.5 min-w-1 min-w-1.5 min-w-2 min-w-2.5 min-w-3 min-w-3.5 min-w-4 min-w-5 min-w-6 min-w-7 min-w-8 min-w-9 min-w-10 min-w-11 min-w-12 min-w-14 min-w-16 min-w-20 min-w-24 min-w-28 min-w-32 min-w-36 min-w-40 min-w-44 min-w-48 min-w-52 min-w-56 min-w-60 min-w-64 min-w-72 min-w-80 min-w-96 min-w-full min-w-min min-w-max min-w-fit");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .min-w-px { min-width: 1px }
        .min-w-0 { min-width: 0px }
        .min-w-0\\.5 { min-width: 0.125rem }
        .min-w-1 { min-width: 0.25rem }
        .min-w-1\\.5 { min-width: 0.375rem }
        .min-w-2 { min-width: 0.5rem }
        .min-w-2\\.5 { min-width: 0.625rem }
        .min-w-3 { min-width: 0.75rem }
        .min-w-3\\.5 { min-width: 0.875rem }
        .min-w-4 { min-width: 1rem }
        .min-w-5 { min-width: 1.25rem }
        .min-w-6 { min-width: 1.5rem }
        .min-w-7 { min-width: 1.75rem }
        .min-w-8 { min-width: 2rem }
        .min-w-9 { min-width: 2.25rem }
        .min-w-10 { min-width: 2.5rem }
        .min-w-11 { min-width: 2.75rem }
        .min-w-12 { min-width: 3rem }
        .min-w-14 { min-width: 3.5rem }
        .min-w-16 { min-width: 4rem }
        .min-w-20 { min-width: 5rem }
        .min-w-24 { min-width: 6rem }
        .min-w-28 { min-width: 7rem }
        .min-w-32 { min-width: 8rem }
        .min-w-36 { min-width: 9rem }
        .min-w-40 { min-width: 10rem }
        .min-w-44 { min-width: 11rem }
        .min-w-48 { min-width: 12rem }
        .min-w-52 { min-width: 13rem }
        .min-w-56 { min-width: 14rem }
        .min-w-60 { min-width: 15rem }
        .min-w-64 { min-width: 16rem }
        .min-w-72 { min-width: 18rem }
        .min-w-80 { min-width: 20rem }
        .min-w-96 { min-width: 24rem }
        .min-w-full { min-width: 100% }
        .min-w-min { min-width: min-content }
        .min-w-max { min-width: max-content }
        .min-w-fit { min-width: fit-content }
        """
    );
  }

  @Test
  public void opacity() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("opacity-0 opacity-5 opacity-10 opacity-15 opacity-20 opacity-25 opacity-30 opacity-35 opacity-40 opacity-45 opacity-50 opacity-55 opacity-60 opacity-65 opacity-70 opacity-75 opacity-80 opacity-85 opacity-90 opacity-95 opacity-100");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .opacity-0 { opacity: 0 }
        .opacity-5 { opacity: 0.05 }
        .opacity-10 { opacity: 0.1 }
        .opacity-15 { opacity: 0.15 }
        .opacity-20 { opacity: 0.2 }
        .opacity-25 { opacity: 0.25 }
        .opacity-30 { opacity: 0.3 }
        .opacity-35 { opacity: 0.35 }
        .opacity-40 { opacity: 0.4 }
        .opacity-45 { opacity: 0.45 }
        .opacity-50 { opacity: 0.5 }
        .opacity-55 { opacity: 0.55 }
        .opacity-60 { opacity: 0.6 }
        .opacity-65 { opacity: 0.65 }
        .opacity-70 { opacity: 0.7 }
        .opacity-75 { opacity: 0.75 }
        .opacity-80 { opacity: 0.8 }
        .opacity-85 { opacity: 0.85 }
        .opacity-90 { opacity: 0.9 }
        .opacity-95 { opacity: 0.95 }
        .opacity-100 { opacity: 1 }
        """
    );
  }

  @Test
  public void outlineColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("outline-inherit outline-current outline-transparent outline-black outline-white");
      }
    }

    test(
        Subject.class,

        """
        .outline-inherit { outline-color: inherit }
        .outline-current { outline-color: currentColor }
        .outline-transparent { outline-color: transparent }
        .outline-black { outline-color: #000000 }
        .outline-white { outline-color: #ffffff }
        """
    );
  }

  @Test
  public void outlineOffset() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("outline-offset-0 outline-offset-1 outline-offset-2 outline-offset-4 outline-offset-8");
        className("-outline-offset-1 focus:-outline-offset-2");
      }
    }

    test(
        Subject.class,

        """
        .outline-offset-0 { outline-offset: 0px }
        .outline-offset-1 { outline-offset: 1px }
        .outline-offset-2 { outline-offset: 2px }
        .outline-offset-4 { outline-offset: 4px }
        .outline-offset-8 { outline-offset: 8px }
        .-outline-offset-1 { outline-offset: -1px }
        .focus\\:-outline-offset-2:focus { outline-offset: -2px }
        """
    );
  }

  @Test
  public void outlineStyle() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("outline-none outline outline-dashed outline-dotted outline-double");
      }
    }

    test(
        Subject.class,

        """
        .outline-none { outline: 2px solid transparent; outline-offset: 2px }
        .outline { outline-style: solid }
        .outline-dashed { outline-style: dashed }
        .outline-dotted { outline-style: dotted }
        .outline-double { outline-style: double }
        """
    );
  }

  @Test
  public void outlineWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("outline-0 outline-1 outline-2 outline-4 outline-8");
      }
    }

    test(
        Subject.class,

        """
        .outline-0 { outline-width: 0px }
        .outline-1 { outline-width: 1px }
        .outline-2 { outline-width: 2px }
        .outline-4 { outline-width: 4px }
        .outline-8 { outline-width: 8px }
        """
    );
  }

  @Test
  public void overflow() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("overflow-auto overflow-hidden overflow-clip overflow-visible overflow-scroll");
        className("overflow-x-auto overflow-x-hidden overflow-x-clip overflow-x-visible overflow-x-scroll");
        className("overflow-y-auto overflow-y-hidden overflow-y-clip overflow-y-visible overflow-y-scroll");
      }
    }

    test(
        Subject.class,

        """
        .overflow-auto { overflow: auto }
        .overflow-hidden { overflow: hidden }
        .overflow-clip { overflow: clip }
        .overflow-visible { overflow: visible }
        .overflow-scroll { overflow: scroll }
        .overflow-x-auto { overflow-x: auto }
        .overflow-x-hidden { overflow-x: hidden }
        .overflow-x-clip { overflow-x: clip }
        .overflow-x-visible { overflow-x: visible }
        .overflow-x-scroll { overflow-x: scroll }
        .overflow-y-auto { overflow-y: auto }
        .overflow-y-hidden { overflow-y: hidden }
        .overflow-y-clip { overflow-y: clip }
        .overflow-y-visible { overflow-y: visible }
        .overflow-y-scroll { overflow-y: scroll }
        """
    );
  }

  @Test
  public void padding() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("p-px p-0 p-0.5 p-1 p-1.5 p-2 p-2.5 p-3 p-3.5 p-4 p-5 p-6 p-7 p-8 p-9 p-10 p-11 p-12 p-14 p-16 p-20 p-24 p-28 p-32 p-36 p-40 p-44 p-48 p-52 p-56 p-60 p-64 p-72 p-80 p-96");
        className("px-px px-0 px-0.5 px-1 px-1.5 px-2 px-2.5 px-3 px-3.5 px-4 px-5 px-6 px-7 px-8 px-9 px-10 px-11 px-12 px-14 px-16 px-20 px-24 px-28 px-32 px-36 px-40 px-44 px-48 px-52 px-56 px-60 px-64 px-72 px-80 px-96");
        className("py-px py-0 py-0.5 py-1 py-1.5 py-2 py-2.5 py-3 py-3.5 py-4 py-5 py-6 py-7 py-8 py-9 py-10 py-11 py-12 py-14 py-16 py-20 py-24 py-28 py-32 py-36 py-40 py-44 py-48 py-52 py-56 py-60 py-64 py-72 py-80 py-96");
        className("pt-px pt-0 pt-0.5 pt-1 pt-1.5 pt-2 pt-2.5 pt-3 pt-3.5 pt-4 pt-5 pt-6 pt-7 pt-8 pt-9 pt-10 pt-11 pt-12 pt-14 pt-16 pt-20 pt-24 pt-28 pt-32 pt-36 pt-40 pt-44 pt-48 pt-52 pt-56 pt-60 pt-64 pt-72 pt-80 pt-96");
        className("pr-px pr-0 pr-0.5 pr-1 pr-1.5 pr-2 pr-2.5 pr-3 pr-3.5 pr-4 pr-5 pr-6 pr-7 pr-8 pr-9 pr-10 pr-11 pr-12 pr-14 pr-16 pr-20 pr-24 pr-28 pr-32 pr-36 pr-40 pr-44 pr-48 pr-52 pr-56 pr-60 pr-64 pr-72 pr-80 pr-96");
        className("pb-px pb-0 pb-0.5 pb-1 pb-1.5 pb-2 pb-2.5 pb-3 pb-3.5 pb-4 pb-5 pb-6 pb-7 pb-8 pb-9 pb-10 pb-11 pb-12 pb-14 pb-16 pb-20 pb-24 pb-28 pb-32 pb-36 pb-40 pb-44 pb-48 pb-52 pb-56 pb-60 pb-64 pb-72 pb-80 pb-96");
        className("pl-px pl-0 pl-0.5 pl-1 pl-1.5 pl-2 pl-2.5 pl-3 pl-3.5 pl-4 pl-5 pl-6 pl-7 pl-8 pl-9 pl-10 pl-11 pl-12 pl-14 pl-16 pl-20 pl-24 pl-28 pl-32 pl-36 pl-40 pl-44 pl-48 pl-52 pl-56 pl-60 pl-64 pl-72 pl-80 pl-96");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .p-px { padding: 1px }
        .p-0 { padding: 0px }
        .p-0\\.5 { padding: 0.125rem }
        .p-1 { padding: 0.25rem }
        .p-1\\.5 { padding: 0.375rem }
        .p-2 { padding: 0.5rem }
        .p-2\\.5 { padding: 0.625rem }
        .p-3 { padding: 0.75rem }
        .p-3\\.5 { padding: 0.875rem }
        .p-4 { padding: 1rem }
        .p-5 { padding: 1.25rem }
        .p-6 { padding: 1.5rem }
        .p-7 { padding: 1.75rem }
        .p-8 { padding: 2rem }
        .p-9 { padding: 2.25rem }
        .p-10 { padding: 2.5rem }
        .p-11 { padding: 2.75rem }
        .p-12 { padding: 3rem }
        .p-14 { padding: 3.5rem }
        .p-16 { padding: 4rem }
        .p-20 { padding: 5rem }
        .p-24 { padding: 6rem }
        .p-28 { padding: 7rem }
        .p-32 { padding: 8rem }
        .p-36 { padding: 9rem }
        .p-40 { padding: 10rem }
        .p-44 { padding: 11rem }
        .p-48 { padding: 12rem }
        .p-52 { padding: 13rem }
        .p-56 { padding: 14rem }
        .p-60 { padding: 15rem }
        .p-64 { padding: 16rem }
        .p-72 { padding: 18rem }
        .p-80 { padding: 20rem }
        .p-96 { padding: 24rem }
        .px-px { padding-left: 1px; padding-right: 1px }
        .px-0 { padding-left: 0px; padding-right: 0px }
        .px-0\\.5 { padding-left: 0.125rem; padding-right: 0.125rem }
        .px-1 { padding-left: 0.25rem; padding-right: 0.25rem }
        .px-1\\.5 { padding-left: 0.375rem; padding-right: 0.375rem }
        .px-2 { padding-left: 0.5rem; padding-right: 0.5rem }
        .px-2\\.5 { padding-left: 0.625rem; padding-right: 0.625rem }
        .px-3 { padding-left: 0.75rem; padding-right: 0.75rem }
        .px-3\\.5 { padding-left: 0.875rem; padding-right: 0.875rem }
        .px-4 { padding-left: 1rem; padding-right: 1rem }
        .px-5 { padding-left: 1.25rem; padding-right: 1.25rem }
        .px-6 { padding-left: 1.5rem; padding-right: 1.5rem }
        .px-7 { padding-left: 1.75rem; padding-right: 1.75rem }
        .px-8 { padding-left: 2rem; padding-right: 2rem }
        .px-9 { padding-left: 2.25rem; padding-right: 2.25rem }
        .px-10 { padding-left: 2.5rem; padding-right: 2.5rem }
        .px-11 { padding-left: 2.75rem; padding-right: 2.75rem }
        .px-12 { padding-left: 3rem; padding-right: 3rem }
        .px-14 { padding-left: 3.5rem; padding-right: 3.5rem }
        .px-16 { padding-left: 4rem; padding-right: 4rem }
        .px-20 { padding-left: 5rem; padding-right: 5rem }
        .px-24 { padding-left: 6rem; padding-right: 6rem }
        .px-28 { padding-left: 7rem; padding-right: 7rem }
        .px-32 { padding-left: 8rem; padding-right: 8rem }
        .px-36 { padding-left: 9rem; padding-right: 9rem }
        .px-40 { padding-left: 10rem; padding-right: 10rem }
        .px-44 { padding-left: 11rem; padding-right: 11rem }
        .px-48 { padding-left: 12rem; padding-right: 12rem }
        .px-52 { padding-left: 13rem; padding-right: 13rem }
        .px-56 { padding-left: 14rem; padding-right: 14rem }
        .px-60 { padding-left: 15rem; padding-right: 15rem }
        .px-64 { padding-left: 16rem; padding-right: 16rem }
        .px-72 { padding-left: 18rem; padding-right: 18rem }
        .px-80 { padding-left: 20rem; padding-right: 20rem }
        .px-96 { padding-left: 24rem; padding-right: 24rem }
        .py-px { padding-top: 1px; padding-bottom: 1px }
        .py-0 { padding-top: 0px; padding-bottom: 0px }
        .py-0\\.5 { padding-top: 0.125rem; padding-bottom: 0.125rem }
        .py-1 { padding-top: 0.25rem; padding-bottom: 0.25rem }
        .py-1\\.5 { padding-top: 0.375rem; padding-bottom: 0.375rem }
        .py-2 { padding-top: 0.5rem; padding-bottom: 0.5rem }
        .py-2\\.5 { padding-top: 0.625rem; padding-bottom: 0.625rem }
        .py-3 { padding-top: 0.75rem; padding-bottom: 0.75rem }
        .py-3\\.5 { padding-top: 0.875rem; padding-bottom: 0.875rem }
        .py-4 { padding-top: 1rem; padding-bottom: 1rem }
        .py-5 { padding-top: 1.25rem; padding-bottom: 1.25rem }
        .py-6 { padding-top: 1.5rem; padding-bottom: 1.5rem }
        .py-7 { padding-top: 1.75rem; padding-bottom: 1.75rem }
        .py-8 { padding-top: 2rem; padding-bottom: 2rem }
        .py-9 { padding-top: 2.25rem; padding-bottom: 2.25rem }
        .py-10 { padding-top: 2.5rem; padding-bottom: 2.5rem }
        .py-11 { padding-top: 2.75rem; padding-bottom: 2.75rem }
        .py-12 { padding-top: 3rem; padding-bottom: 3rem }
        .py-14 { padding-top: 3.5rem; padding-bottom: 3.5rem }
        .py-16 { padding-top: 4rem; padding-bottom: 4rem }
        .py-20 { padding-top: 5rem; padding-bottom: 5rem }
        .py-24 { padding-top: 6rem; padding-bottom: 6rem }
        .py-28 { padding-top: 7rem; padding-bottom: 7rem }
        .py-32 { padding-top: 8rem; padding-bottom: 8rem }
        .py-36 { padding-top: 9rem; padding-bottom: 9rem }
        .py-40 { padding-top: 10rem; padding-bottom: 10rem }
        .py-44 { padding-top: 11rem; padding-bottom: 11rem }
        .py-48 { padding-top: 12rem; padding-bottom: 12rem }
        .py-52 { padding-top: 13rem; padding-bottom: 13rem }
        .py-56 { padding-top: 14rem; padding-bottom: 14rem }
        .py-60 { padding-top: 15rem; padding-bottom: 15rem }
        .py-64 { padding-top: 16rem; padding-bottom: 16rem }
        .py-72 { padding-top: 18rem; padding-bottom: 18rem }
        .py-80 { padding-top: 20rem; padding-bottom: 20rem }
        .py-96 { padding-top: 24rem; padding-bottom: 24rem }
        .pt-px { padding-top: 1px }
        .pt-0 { padding-top: 0px }
        .pt-0\\.5 { padding-top: 0.125rem }
        .pt-1 { padding-top: 0.25rem }
        .pt-1\\.5 { padding-top: 0.375rem }
        .pt-2 { padding-top: 0.5rem }
        .pt-2\\.5 { padding-top: 0.625rem }
        .pt-3 { padding-top: 0.75rem }
        .pt-3\\.5 { padding-top: 0.875rem }
        .pt-4 { padding-top: 1rem }
        .pt-5 { padding-top: 1.25rem }
        .pt-6 { padding-top: 1.5rem }
        .pt-7 { padding-top: 1.75rem }
        .pt-8 { padding-top: 2rem }
        .pt-9 { padding-top: 2.25rem }
        .pt-10 { padding-top: 2.5rem }
        .pt-11 { padding-top: 2.75rem }
        .pt-12 { padding-top: 3rem }
        .pt-14 { padding-top: 3.5rem }
        .pt-16 { padding-top: 4rem }
        .pt-20 { padding-top: 5rem }
        .pt-24 { padding-top: 6rem }
        .pt-28 { padding-top: 7rem }
        .pt-32 { padding-top: 8rem }
        .pt-36 { padding-top: 9rem }
        .pt-40 { padding-top: 10rem }
        .pt-44 { padding-top: 11rem }
        .pt-48 { padding-top: 12rem }
        .pt-52 { padding-top: 13rem }
        .pt-56 { padding-top: 14rem }
        .pt-60 { padding-top: 15rem }
        .pt-64 { padding-top: 16rem }
        .pt-72 { padding-top: 18rem }
        .pt-80 { padding-top: 20rem }
        .pt-96 { padding-top: 24rem }
        .pr-px { padding-right: 1px }
        .pr-0 { padding-right: 0px }
        .pr-0\\.5 { padding-right: 0.125rem }
        .pr-1 { padding-right: 0.25rem }
        .pr-1\\.5 { padding-right: 0.375rem }
        .pr-2 { padding-right: 0.5rem }
        .pr-2\\.5 { padding-right: 0.625rem }
        .pr-3 { padding-right: 0.75rem }
        .pr-3\\.5 { padding-right: 0.875rem }
        .pr-4 { padding-right: 1rem }
        .pr-5 { padding-right: 1.25rem }
        .pr-6 { padding-right: 1.5rem }
        .pr-7 { padding-right: 1.75rem }
        .pr-8 { padding-right: 2rem }
        .pr-9 { padding-right: 2.25rem }
        .pr-10 { padding-right: 2.5rem }
        .pr-11 { padding-right: 2.75rem }
        .pr-12 { padding-right: 3rem }
        .pr-14 { padding-right: 3.5rem }
        .pr-16 { padding-right: 4rem }
        .pr-20 { padding-right: 5rem }
        .pr-24 { padding-right: 6rem }
        .pr-28 { padding-right: 7rem }
        .pr-32 { padding-right: 8rem }
        .pr-36 { padding-right: 9rem }
        .pr-40 { padding-right: 10rem }
        .pr-44 { padding-right: 11rem }
        .pr-48 { padding-right: 12rem }
        .pr-52 { padding-right: 13rem }
        .pr-56 { padding-right: 14rem }
        .pr-60 { padding-right: 15rem }
        .pr-64 { padding-right: 16rem }
        .pr-72 { padding-right: 18rem }
        .pr-80 { padding-right: 20rem }
        .pr-96 { padding-right: 24rem }
        .pb-px { padding-bottom: 1px }
        .pb-0 { padding-bottom: 0px }
        .pb-0\\.5 { padding-bottom: 0.125rem }
        .pb-1 { padding-bottom: 0.25rem }
        .pb-1\\.5 { padding-bottom: 0.375rem }
        .pb-2 { padding-bottom: 0.5rem }
        .pb-2\\.5 { padding-bottom: 0.625rem }
        .pb-3 { padding-bottom: 0.75rem }
        .pb-3\\.5 { padding-bottom: 0.875rem }
        .pb-4 { padding-bottom: 1rem }
        .pb-5 { padding-bottom: 1.25rem }
        .pb-6 { padding-bottom: 1.5rem }
        .pb-7 { padding-bottom: 1.75rem }
        .pb-8 { padding-bottom: 2rem }
        .pb-9 { padding-bottom: 2.25rem }
        .pb-10 { padding-bottom: 2.5rem }
        .pb-11 { padding-bottom: 2.75rem }
        .pb-12 { padding-bottom: 3rem }
        .pb-14 { padding-bottom: 3.5rem }
        .pb-16 { padding-bottom: 4rem }
        .pb-20 { padding-bottom: 5rem }
        .pb-24 { padding-bottom: 6rem }
        .pb-28 { padding-bottom: 7rem }
        .pb-32 { padding-bottom: 8rem }
        .pb-36 { padding-bottom: 9rem }
        .pb-40 { padding-bottom: 10rem }
        .pb-44 { padding-bottom: 11rem }
        .pb-48 { padding-bottom: 12rem }
        .pb-52 { padding-bottom: 13rem }
        .pb-56 { padding-bottom: 14rem }
        .pb-60 { padding-bottom: 15rem }
        .pb-64 { padding-bottom: 16rem }
        .pb-72 { padding-bottom: 18rem }
        .pb-80 { padding-bottom: 20rem }
        .pb-96 { padding-bottom: 24rem }
        .pl-px { padding-left: 1px }
        .pl-0 { padding-left: 0px }
        .pl-0\\.5 { padding-left: 0.125rem }
        .pl-1 { padding-left: 0.25rem }
        .pl-1\\.5 { padding-left: 0.375rem }
        .pl-2 { padding-left: 0.5rem }
        .pl-2\\.5 { padding-left: 0.625rem }
        .pl-3 { padding-left: 0.75rem }
        .pl-3\\.5 { padding-left: 0.875rem }
        .pl-4 { padding-left: 1rem }
        .pl-5 { padding-left: 1.25rem }
        .pl-6 { padding-left: 1.5rem }
        .pl-7 { padding-left: 1.75rem }
        .pl-8 { padding-left: 2rem }
        .pl-9 { padding-left: 2.25rem }
        .pl-10 { padding-left: 2.5rem }
        .pl-11 { padding-left: 2.75rem }
        .pl-12 { padding-left: 3rem }
        .pl-14 { padding-left: 3.5rem }
        .pl-16 { padding-left: 4rem }
        .pl-20 { padding-left: 5rem }
        .pl-24 { padding-left: 6rem }
        .pl-28 { padding-left: 7rem }
        .pl-32 { padding-left: 8rem }
        .pl-36 { padding-left: 9rem }
        .pl-40 { padding-left: 10rem }
        .pl-44 { padding-left: 11rem }
        .pl-48 { padding-left: 12rem }
        .pl-52 { padding-left: 13rem }
        .pl-56 { padding-left: 14rem }
        .pl-60 { padding-left: 15rem }
        .pl-64 { padding-left: 16rem }
        .pl-72 { padding-left: 18rem }
        .pl-80 { padding-left: 20rem }
        .pl-96 { padding-left: 24rem }
        """
    );
  }

  @Test
  public void pointerEvents() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("pointer-events-none pointer-events-auto");
      }
    }

    test(
        Subject.class,

        """
        .pointer-events-none { pointer-events: none }
        .pointer-events-auto { pointer-events: auto }
        """
    );
  }

  @Test
  public void position() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("static fixed absolute relative sticky");
      }
    }

    test(
        Subject.class,

        """
        .static { position: static }
        .fixed { position: fixed }
        .absolute { position: absolute }
        .relative { position: relative }
        .sticky { position: sticky }
        """
    );
  }

  @Test
  public void ringColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("ring-inherit ring-current ring-transparent ring-black ring-white");
      }
    }

    test(
        Subject.class,

        """
        .ring-inherit { --tw-ring-color: inherit }
        .ring-current { --tw-ring-color: currentColor }
        .ring-transparent { --tw-ring-color: transparent }
        .ring-black { --tw-ring-color: #000000 }
        .ring-white { --tw-ring-color: #ffffff }
        """
    );
  }

  @Test
  public void ringOffset() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("ring-offset-0 ring-offset-1 ring-offset-2 ring-offset-4 ring-offset-8 ring-offset-[10px]");
      }
    }

    test(
        Subject.class,

        """
        .ring-offset-0 { --tw-ring-offset-width: 0px }
        .ring-offset-1 { --tw-ring-offset-width: 1px }
        .ring-offset-2 { --tw-ring-offset-width: 2px }
        .ring-offset-4 { --tw-ring-offset-width: 4px }
        .ring-offset-8 { --tw-ring-offset-width: 8px }
        .ring-offset-\\[10px\\] { --tw-ring-offset-width: 10px }
        """
    );
  }

  @Test
  public void ringOffsetColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("ring-offset-inherit ring-offset-current ring-offset-transparent ring-offset-black ring-offset-white");
      }
    }

    test(
        Subject.class,

        """
        .ring-offset-inherit { --tw-ring-offset-color: inherit }
        .ring-offset-current { --tw-ring-offset-color: currentColor }
        .ring-offset-transparent { --tw-ring-offset-color: transparent }
        .ring-offset-black { --tw-ring-offset-color: #000000 }
        .ring-offset-white { --tw-ring-offset-color: #ffffff }
        """
    );
  }

  @Test
  public void ringWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("ring-0 ring-1 ring-2 ring ring-4 ring-8 ring-inset ring-[10px]");
      }
    }

    test(
        Subject.class,

        """
        .ring-0 {
          --tw-ring-offset-shadow: var(--tw-ring-inset, ) 0 0 0 var(--tw-ring-offset-width, 0px) var(--tw-ring-offset-color, #fff);
          --tw-ring-shadow: var(--tw-ring-inset, ) 0 0 0 calc(0px + var(--tw-ring-offset-width, 0px)) var(--tw-ring-color, rgb(59 130 246 / 0.5));
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .ring-1 {
          --tw-ring-offset-shadow: var(--tw-ring-inset, ) 0 0 0 var(--tw-ring-offset-width, 0px) var(--tw-ring-offset-color, #fff);
          --tw-ring-shadow: var(--tw-ring-inset, ) 0 0 0 calc(1px + var(--tw-ring-offset-width, 0px)) var(--tw-ring-color, rgb(59 130 246 / 0.5));
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .ring-2 {
          --tw-ring-offset-shadow: var(--tw-ring-inset, ) 0 0 0 var(--tw-ring-offset-width, 0px) var(--tw-ring-offset-color, #fff);
          --tw-ring-shadow: var(--tw-ring-inset, ) 0 0 0 calc(2px + var(--tw-ring-offset-width, 0px)) var(--tw-ring-color, rgb(59 130 246 / 0.5));
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .ring {
          --tw-ring-offset-shadow: var(--tw-ring-inset, ) 0 0 0 var(--tw-ring-offset-width, 0px) var(--tw-ring-offset-color, #fff);
          --tw-ring-shadow: var(--tw-ring-inset, ) 0 0 0 calc(3px + var(--tw-ring-offset-width, 0px)) var(--tw-ring-color, rgb(59 130 246 / 0.5));
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .ring-4 {
          --tw-ring-offset-shadow: var(--tw-ring-inset, ) 0 0 0 var(--tw-ring-offset-width, 0px) var(--tw-ring-offset-color, #fff);
          --tw-ring-shadow: var(--tw-ring-inset, ) 0 0 0 calc(4px + var(--tw-ring-offset-width, 0px)) var(--tw-ring-color, rgb(59 130 246 / 0.5));
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .ring-8 {
          --tw-ring-offset-shadow: var(--tw-ring-inset, ) 0 0 0 var(--tw-ring-offset-width, 0px) var(--tw-ring-offset-color, #fff);
          --tw-ring-shadow: var(--tw-ring-inset, ) 0 0 0 calc(8px + var(--tw-ring-offset-width, 0px)) var(--tw-ring-color, rgb(59 130 246 / 0.5));
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .ring-\\[10px\\] {
          --tw-ring-offset-shadow: var(--tw-ring-inset, ) 0 0 0 var(--tw-ring-offset-width, 0px) var(--tw-ring-offset-color, #fff);
          --tw-ring-shadow: var(--tw-ring-inset, ) 0 0 0 calc(10px + var(--tw-ring-offset-width, 0px)) var(--tw-ring-color, rgb(59 130 246 / 0.5));
          box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000);
        }
        .ring-inset { --tw-ring-inset: inset }
        """
    );
  }

  @Test
  public void screenReader() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("sr-only not-sr-only");
      }
    }

    test(
        Subject.class,

        """
        .sr-only {
          position: absolute;
          width: 1px;
          height: 1px;
          padding: 0;
          margin: -1px;
          overflow: hidden;
          clip: rect(0, 0, 0, 0);
          white-space: nowrap;
          border-width: 0;
        }
        .not-sr-only {
          position: static;
          width: auto;
          height: auto;
          padding: 0;
          margin: 0;
          overflow: visible;
          clip: auto;
          white-space: normal;
        }
        """
    );
  }

  @Test
  public void size() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("size-auto size-px size-0 size-0.5 size-1 size-1.5 size-2 size-2.5 size-3 size-3.5 size-4 size-5 size-6 size-7 size-8 size-9 size-10 size-11 size-12 size-14 size-16 size-20 size-24 size-28 size-32 size-36 size-40 size-44 size-48 size-52 size-56 size-60 size-64 size-72 size-80 size-96 size-1/2 size-1/3 size-2/3 size-1/4 size-2/4 size-3/4 size-1/5 size-2/5 size-3/5 size-4/5 size-1/6 size-2/6 size-3/6 size-4/6 size-5/6 size-1/12 size-2/12 size-3/12 size-4/12 size-5/12 size-6/12 size-7/12 size-8/12 size-9/12 size-10/12 size-11/12 size-full size-min size-max size-fit");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .size-auto { height: auto; width: auto }
        .size-px { height: 1px; width: 1px }
        .size-0 { height: 0px; width: 0px }
        .size-0\\.5 { height: 0.125rem; width: 0.125rem }
        .size-1 { height: 0.25rem; width: 0.25rem }
        .size-1\\.5 { height: 0.375rem; width: 0.375rem }
        .size-2 { height: 0.5rem; width: 0.5rem }
        .size-2\\.5 { height: 0.625rem; width: 0.625rem }
        .size-3 { height: 0.75rem; width: 0.75rem }
        .size-3\\.5 { height: 0.875rem; width: 0.875rem }
        .size-4 { height: 1rem; width: 1rem }
        .size-5 { height: 1.25rem; width: 1.25rem }
        .size-6 { height: 1.5rem; width: 1.5rem }
        .size-7 { height: 1.75rem; width: 1.75rem }
        .size-8 { height: 2rem; width: 2rem }
        .size-9 { height: 2.25rem; width: 2.25rem }
        .size-10 { height: 2.5rem; width: 2.5rem }
        .size-11 { height: 2.75rem; width: 2.75rem }
        .size-12 { height: 3rem; width: 3rem }
        .size-14 { height: 3.5rem; width: 3.5rem }
        .size-16 { height: 4rem; width: 4rem }
        .size-20 { height: 5rem; width: 5rem }
        .size-24 { height: 6rem; width: 6rem }
        .size-28 { height: 7rem; width: 7rem }
        .size-32 { height: 8rem; width: 8rem }
        .size-36 { height: 9rem; width: 9rem }
        .size-40 { height: 10rem; width: 10rem }
        .size-44 { height: 11rem; width: 11rem }
        .size-48 { height: 12rem; width: 12rem }
        .size-52 { height: 13rem; width: 13rem }
        .size-56 { height: 14rem; width: 14rem }
        .size-60 { height: 15rem; width: 15rem }
        .size-64 { height: 16rem; width: 16rem }
        .size-72 { height: 18rem; width: 18rem }
        .size-80 { height: 20rem; width: 20rem }
        .size-96 { height: 24rem; width: 24rem }
        .size-1\\/2 { height: 50%; width: 50% }
        .size-1\\/3 { height: 33.333333%; width: 33.333333% }
        .size-2\\/3 { height: 66.666667%; width: 66.666667% }
        .size-1\\/4 { height: 25%; width: 25% }
        .size-2\\/4 { height: 50%; width: 50% }
        .size-3\\/4 { height: 75%; width: 75% }
        .size-1\\/5 { height: 20%; width: 20% }
        .size-2\\/5 { height: 40%; width: 40% }
        .size-3\\/5 { height: 60%; width: 60% }
        .size-4\\/5 { height: 80%; width: 80% }
        .size-1\\/6 { height: 16.666667%; width: 16.666667% }
        .size-2\\/6 { height: 33.333333%; width: 33.333333% }
        .size-3\\/6 { height: 50%; width: 50% }
        .size-4\\/6 { height: 66.666667%; width: 66.666667% }
        .size-5\\/6 { height: 83.333333%; width: 83.333333% }
        .size-1\\/12 { height: 8.333333%; width: 8.333333% }
        .size-2\\/12 { height: 16.666667%; width: 16.666667% }
        .size-3\\/12 { height: 25%; width: 25% }
        .size-4\\/12 { height: 33.333333%; width: 33.333333% }
        .size-5\\/12 { height: 41.666667%; width: 41.666667% }
        .size-6\\/12 { height: 50%; width: 50% }
        .size-7\\/12 { height: 58.333333%; width: 58.333333% }
        .size-8\\/12 { height: 66.666667%; width: 66.666667% }
        .size-9\\/12 { height: 75%; width: 75% }
        .size-10\\/12 { height: 83.333333%; width: 83.333333% }
        .size-11\\/12 { height: 91.666667%; width: 91.666667% }
        .size-full { height: 100%; width: 100% }
        .size-min { height: min-content; width: min-content }
        .size-max { height: max-content; width: max-content }
        .size-fit { height: fit-content; width: fit-content }
        """
    );
  }

  @Test
  public void stroke() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("stroke-none");
        className("stroke-inherit stroke-current stroke-transparent stroke-black stroke-white");
      }
    }

    test(
        Subject.class,

        """
        .stroke-none { stroke: none }
        .stroke-inherit { stroke: inherit }
        .stroke-current { stroke: currentColor }
        .stroke-transparent { stroke: transparent }
        .stroke-black { stroke: #000000 }
        .stroke-white { stroke: #ffffff }
        """
    );
  }

  @Test
  public void strokeWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("stroke-0 stroke-1 stroke-2");
      }
    }

    test(
        Subject.class,

        """
        .stroke-0 { stroke-width: 0 }
        .stroke-1 { stroke-width: 1 }
        .stroke-2 { stroke-width: 2 }
        """
    );
  }

  @Test
  public void tableLayout() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("table-auto table-fixed");
      }
    }

    test(
        Subject.class,

        """
        .table-auto { table-layout: auto }
        .table-fixed { table-layout: fixed }
        """
    );
  }

  @Test
  public void textAlign() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-left text-center text-right text-justify text-start text-end");
      }
    }

    test(
        Subject.class,

        """
        .text-left { text-align: left }
        .text-center { text-align: center }
        .text-right { text-align: right }
        .text-justify { text-align: justify }
        .text-start { text-align: start }
        .text-end { text-align: end }
        """
    );
  }

  @Test
  public void textColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-inherit text-current text-transparent text-black text-white");
      }
    }

    test(
        Subject.class,

        """
        .text-inherit { color: inherit }
        .text-current { color: currentColor }
        .text-transparent { color: transparent }
        .text-black { color: #000000 }
        .text-white { color: #ffffff }
        """
    );
  }

  @Test
  public void textDecoration() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("underline overline line-through no-underline");
      }
    }

    test(
        Subject.class,

        """
        .underline { text-decoration-line: underline }
        .overline { text-decoration-line: overline }
        .line-through { text-decoration-line: line-through }
        .no-underline { text-decoration-line: none }
        """
    );
  }

  @Test
  public void textOverflow() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("truncate text-ellipsis text-clip");
      }
    }

    test(
        Subject.class,

        """
        .truncate {
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        .text-ellipsis { text-overflow: ellipsis }
        .text-clip { text-overflow: clip }
        """
    );
  }

  @Test
  public void textWrap() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-wrap text-nowrap text-balance text-pretty");
      }
    }

    test(
        Subject.class,

        """
        .text-wrap { text-wrap: wrap }
        .text-nowrap { text-wrap: nowrap }
        .text-balance { text-wrap: balance }
        .text-pretty { text-wrap: pretty }
        """
    );
  }

  @Test
  public void transform() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("transform-none");
      }
    }

    test(
        Subject.class,

        """
        .transform-none { transform: none }
        """
    );
  }

  @Test
  public void transitionProperty() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("transition-none transition-all transition transition-colors transition-opacity transition-shadow transition-transform");
      }
    }

    test(
        Subject.class,

        """
        .transition-none { transition-property: none }
        .transition-all {
          transition-property: all;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
        }
        .transition {
          transition-property: color, background-color, border-color, text-decoration-color, fill, stroke, opacity, box-shadow, transform, filter, backdrop-filter;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
        }
        .transition-colors {
          transition-property: color, background-color, border-color, text-decoration-color, fill, stroke;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
        }
        .transition-opacity {
          transition-property: opacity;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
        }
        .transition-shadow {
          transition-property: box-shadow;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
        }
        .transition-transform {
          transition-property: transform;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
        }
        """
    );
  }

  @Test
  public void transitionDuration() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("duration-0 duration-75 duration-100 duration-150 duration-200 duration-300 duration-500 duration-700 duration-1000");
      }
    }

    test(
        Subject.class,

        """
        .duration-0 { transition-duration: 0s }
        .duration-75 { transition-duration: 75ms }
        .duration-100 { transition-duration: 100ms }
        .duration-150 { transition-duration: 150ms }
        .duration-200 { transition-duration: 200ms }
        .duration-300 { transition-duration: 300ms }
        .duration-500 { transition-duration: 500ms }
        .duration-700 { transition-duration: 700ms }
        .duration-1000 { transition-duration: 1000ms }
        """
    );
  }

  @Test
  public void translate() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("translate-x-1/2 translate-x-1/3 translate-x-2/3 translate-x-1/4 translate-x-2/4 translate-x-3/4 translate-x-full");
        className("translate-x-px translate-x-0 translate-x-0.5 translate-x-1");
        className("translate-y-1/2 translate-y-1/3 translate-y-2/3 translate-y-1/4 translate-y-2/4 translate-y-3/4 translate-y-full");
        className("translate-y-px translate-y-0 translate-y-0.5 translate-y-1");
        className("-translate-x-6 -translate-y-8");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .translate-x-1\\/2 { transform: translateX(50%) }
        .translate-x-1\\/3 { transform: translateX(33.333333%) }
        .translate-x-2\\/3 { transform: translateX(66.666667%) }
        .translate-x-1\\/4 { transform: translateX(25%) }
        .translate-x-2\\/4 { transform: translateX(50%) }
        .translate-x-3\\/4 { transform: translateX(75%) }
        .translate-x-full { transform: translateX(100%) }
        .translate-x-px { transform: translateX(1px) }
        .translate-x-0 { transform: translateX(0px) }
        .translate-x-0\\.5 { transform: translateX(0.125rem) }
        .translate-x-1 { transform: translateX(0.25rem) }
        .-translate-x-6 { transform: translateX(-1.5rem) }
        .translate-y-1\\/2 { transform: translateY(50%) }
        .translate-y-1\\/3 { transform: translateY(33.333333%) }
        .translate-y-2\\/3 { transform: translateY(66.666667%) }
        .translate-y-1\\/4 { transform: translateY(25%) }
        .translate-y-2\\/4 { transform: translateY(50%) }
        .translate-y-3\\/4 { transform: translateY(75%) }
        .translate-y-full { transform: translateY(100%) }
        .translate-y-px { transform: translateY(1px) }
        .translate-y-0 { transform: translateY(0px) }
        .translate-y-0\\.5 { transform: translateY(0.125rem) }
        .translate-y-1 { transform: translateY(0.25rem) }
        .-translate-y-8 { transform: translateY(-2rem) }
        """
    );
  }

  @Test
  public void userSelect() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("select-none select-text select-all select-auto");
      }
    }

    test(
        Subject.class,

        """
        .select-none { user-select: none }
        .select-text { user-select: text }
        .select-all { user-select: all }
        .select-auto { user-select: auto }
        """
    );
  }

  @Test
  public void verticalAlign() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("align-baseline align-top align-middle align-bottom align-text-top align-text-bottom align-sub align-super");
      }
    }

    test(
        Subject.class,

        """
        .align-baseline { vertical-align: baseline }
        .align-top { vertical-align: top }
        .align-middle { vertical-align: middle }
        .align-bottom { vertical-align: bottom }
        .align-text-top { vertical-align: text-top }
        .align-text-bottom { vertical-align: text-bottom }
        .align-sub { vertical-align: sub }
        .align-super { vertical-align: super }
        """
    );
  }

  @Test
  public void visibility() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("visible invisible collapse");
      }
    }

    test(
        Subject.class,

        """
        .visible { visibility: visible }
        .invisible { visibility: hidden }
        .collapse { visibility: collapse }
        """
    );
  }

  @Test
  public void whitespace() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("whitespace-normal");
        className("whitespace-nowrap");
        className("whitespace-pre");
        className("whitespace-pre-line");
        className("whitespace-pre-wrap");
        className("whitespace-break-spaces");
      }
    }

    test(
        Subject.class,

        """
        .whitespace-normal { white-space: normal }
        .whitespace-nowrap { white-space: nowrap }
        .whitespace-pre { white-space: pre }
        .whitespace-pre-line { white-space: pre-line }
        .whitespace-pre-wrap { white-space: pre-wrap }
        .whitespace-break-spaces { white-space: break-spaces }
        """
    );
  }

  @Test
  public void width() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("w-auto w-1/2 w-1/3 w-2/3 w-1/4 w-2/4 w-3/4 w-1/5 w-2/5 w-3/5 w-4/5 w-1/6 w-2/6 w-3/6 w-4/6 w-5/6 w-1/12 w-2/12 w-3/12 w-4/12 w-5/12 w-6/12 w-7/12 w-8/12 w-9/12 w-10/12 w-11/12 w-full w-screen w-svw w-lvw w-dvw w-min w-max w-fit");
        className("w-px w-0 w-0.5 w-1 w-1.5 w-2 w-2.5 w-3 w-3.5 w-4 w-5 w-6 w-7 w-8 w-9 w-10 w-11 w-12 w-14 w-16 w-20 w-24 w-28 w-32 w-36 w-40 w-44 w-48 w-52 w-56 w-60 w-64 w-72 w-80 w-96");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .w-auto { width: auto }
        .w-1\\/2 { width: 50% }
        .w-1\\/3 { width: 33.333333% }
        .w-2\\/3 { width: 66.666667% }
        .w-1\\/4 { width: 25% }
        .w-2\\/4 { width: 50% }
        .w-3\\/4 { width: 75% }
        .w-1\\/5 { width: 20% }
        .w-2\\/5 { width: 40% }
        .w-3\\/5 { width: 60% }
        .w-4\\/5 { width: 80% }
        .w-1\\/6 { width: 16.666667% }
        .w-2\\/6 { width: 33.333333% }
        .w-3\\/6 { width: 50% }
        .w-4\\/6 { width: 66.666667% }
        .w-5\\/6 { width: 83.333333% }
        .w-1\\/12 { width: 8.333333% }
        .w-2\\/12 { width: 16.666667% }
        .w-3\\/12 { width: 25% }
        .w-4\\/12 { width: 33.333333% }
        .w-5\\/12 { width: 41.666667% }
        .w-6\\/12 { width: 50% }
        .w-7\\/12 { width: 58.333333% }
        .w-8\\/12 { width: 66.666667% }
        .w-9\\/12 { width: 75% }
        .w-10\\/12 { width: 83.333333% }
        .w-11\\/12 { width: 91.666667% }
        .w-full { width: 100% }
        .w-screen { width: 100vw }
        .w-svw { width: 100svw }
        .w-lvw { width: 100lvw }
        .w-dvw { width: 100dvw }
        .w-min { width: min-content }
        .w-max { width: max-content }
        .w-fit { width: fit-content }
        .w-px { width: 1px }
        .w-0 { width: 0px }
        .w-0\\.5 { width: 0.125rem }
        .w-1 { width: 0.25rem }
        .w-1\\.5 { width: 0.375rem }
        .w-2 { width: 0.5rem }
        .w-2\\.5 { width: 0.625rem }
        .w-3 { width: 0.75rem }
        .w-3\\.5 { width: 0.875rem }
        .w-4 { width: 1rem }
        .w-5 { width: 1.25rem }
        .w-6 { width: 1.5rem }
        .w-7 { width: 1.75rem }
        .w-8 { width: 2rem }
        .w-9 { width: 2.25rem }
        .w-10 { width: 2.5rem }
        .w-11 { width: 2.75rem }
        .w-12 { width: 3rem }
        .w-14 { width: 3.5rem }
        .w-16 { width: 4rem }
        .w-20 { width: 5rem }
        .w-24 { width: 6rem }
        .w-28 { width: 7rem }
        .w-32 { width: 8rem }
        .w-36 { width: 9rem }
        .w-40 { width: 10rem }
        .w-44 { width: 11rem }
        .w-48 { width: 12rem }
        .w-52 { width: 13rem }
        .w-56 { width: 14rem }
        .w-60 { width: 15rem }
        .w-64 { width: 16rem }
        .w-72 { width: 18rem }
        .w-80 { width: 20rem }
        .w-96 { width: 24rem }
        """
    );
  }

  @Test
  public void zIndex() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("z-0 z-10 z-20 z-30 z-40 z-50 z-auto");
      }
    }

    test(
        Subject.class,

        """
        .z-0 { z-index: 0 }
        .z-10 { z-index: 10 }
        .z-20 { z-index: 20 }
        .z-30 { z-index: 30 }
        .z-40 { z-index: 40 }
        .z-50 { z-index: 50 }
        .z-auto { z-index: auto }
        """
    );
  }

  // prefixes

  @Test
  public void responsive() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("m-0 sm:block sm:m-1 md:m-2 lg:m-3 xl:m-4 2xl:m-5");
      }
    }

    test(
        Subject.class,

        """
        .m-0 { margin: 0px }

        @media (min-width: 640px) {
          .sm\\:m-1 { margin: 0.25rem }
          .sm\\:block { display: block }
        }

        @media (min-width: 768px) {
          .md\\:m-2 { margin: 0.5rem }
        }

        @media (min-width: 1024px) {
          .lg\\:m-3 { margin: 0.75rem }
        }

        @media (min-width: 1280px) {
          .xl\\:m-4 { margin: 1rem }
        }

        @media (min-width: 1536px) {
          .\\32xl\\:m-5 { margin: 1.25rem }
        }
        """
    );
  }

  @Test
  public void multiplePrefixes() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("m-0 lg:before:m-1");
      }
    }

    test(
        Subject.class,

        """
        .m-0 { margin: 0px }

        @media (min-width: 1024px) {
          .lg\\:before\\:m-1::before { margin: 0.25rem }
        }
        """
    );
  }

  // customization

  @Test
  public void baseLayer() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border");
      }
    }

    test(
        Css.baseLayer("""
        :root {
          --ui-zero: 0px;
          --ui-one: 1px;
        }
        """),

        Subject.class,

        """
        :root {
          --ui-zero: 0px;
          --ui-one: 1px;
        }

        .border { border-width: 1px }
        """
    );
  }

  @Test
  public void breakpoints() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("w-0 sm:w-1 md:w-2 lg:w-3 xl:w-4 max:w-5");
      }
    }

    test(
        Css.breakpoints("""
        sm: 20rem
        md: 40rem
        lg: 66rem
        xl: 82rem
        max: 99rem
        """),

        Subject.class,

        """
        .w-0 { width: 0px }

        @media (min-width: 20rem) {
          .sm\\:w-1 { width: 0.25rem }
        }

        @media (min-width: 40rem) {
          .md\\:w-2 { width: 0.5rem }
        }

        @media (min-width: 66rem) {
          .lg\\:w-3 { width: 0.75rem }
        }

        @media (min-width: 82rem) {
          .xl\\:w-4 { width: 1rem }
        }

        @media (min-width: 99rem) {
          .max\\:w-5 { width: 1.25rem }
        }
        """
    );
  }

  @Test
  public void component01() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("carbon-grid");
      }
    }

    test(
        Css.components("""
        # carbon-grid
        mx-auto grid w-full max-w-screen-max grid-cols-4
        px-0
        md:grid-cols-8 md:px-4
        lg:grid-cols-12
        2xl:px-6
        *:mx-4
        """),

        Subject.class,

        """
        .carbon-grid {
          margin-left: auto;
          margin-right: auto;
          display: grid;
          width: 100%;
          grid-template-columns: repeat(4, minmax(0, 1fr));
          padding-left: 0px;
          padding-right: 0px;
        }
        .carbon-grid > * {
          margin-left: 1rem;
          margin-right: 1rem;
        }
        @media (min-width: 768px) {
          .carbon-grid {
            grid-template-columns: repeat(8, minmax(0, 1fr));
            padding-left: 1rem;
            padding-right: 1rem;
          }
        }
        @media (min-width: 1024px) {
          .carbon-grid {
            grid-template-columns: repeat(12, minmax(0, 1fr));
          }
        }
        @media (min-width: 1536px) {
          .carbon-grid {
            padding-left: 1.5rem;
            padding-right: 1.5rem;
          }
        }
        """
    );
  }

  @Test(description = "component: pseudo class/elem test")
  public void component02() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("foo");
      }
    }

    test(
        Css.components("""
        # foo
        hover:bg-transparent after:block after:border
        """),

        Subject.class,

        """
        .foo:hover {
          background-color: transparent;
        }
        .foo::after {
          display: block;
          border-width: 1px;
        }
        """
    );
  }

  @Test(description = "component: use cached utility")
  public void component03() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border foo");
      }
    }

    test(
        Css.components("""
        # foo
        border
        """),

        Subject.class,

        """
        .foo {
          border-width: 1px;
        }

        .border { border-width: 1px }
        """
    );
  }

  @Test(description = "component: do not generate unused component")
  public void component04() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("used");
      }
    }

    test(
        Css.components("""
        # used
        border

        # not-used
        bg-transparent
        """),

        Subject.class,

        """
        .used {
          border-width: 1px;
        }
        """
    );
  }

  @Test(description = "component: build on top of another component")
  public void component05() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("foo");
      }
    }

    test(
        Css.components("""
        #base
        block

        hover:border
        #foo
        base bg-transparent
        """),

        Subject.class,

        """
        .foo {
          display: block;
          background-color: transparent;
        }
        .foo:hover {
          border-width: 1px;
        }
        """
    );
  }

  @Test(
      description = "component: detect cycles on components",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Cycle detected @ component: b"
  )
  public void component06() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("b");
      }
    }

    test(
        Css.components("""
        # a
        block b
        #b
        a bg-transparent
        """),

        Subject.class,

        """
        .foo {
          display: block;
          background-color: transparent;
        }
        .foo:hover {
          border-width: 1px;
        }
        """
    );
  }

  @Test
  public void component07() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("foo");
      }
    }

    test(
        Css.components("""
        # bar
        block
        svg:bg-transparent

        # foo
        bar
        more:hidden
        """),

        Css.variants("""
        more: &:not(:root)
        svg: & svg
        """),

        Subject.class,

        """
        .foo {
          display: block;
        }
        .foo svg {
          background-color: transparent;
        }
        .foo:not(:root) {
          display: none;
        }
        """
    );
  }

  @Test
  public void component08() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("foo");
      }
    }

    test(
        Css.components("""
        # foo
        visited:block
        visited:hover:grid
        visited:hover:flex-wrap
        visited:hover:active:inline
        md:visited:hover:focus:flex
        """),

        Subject.class,

        """
        .foo:visited {
          display: block;
        }
        .foo:visited:hover {
          display: grid;
          flex-wrap: wrap;
        }
        .foo:visited:hover:active {
          display: inline;
        }
        @media (min-width: 768px) {
          .foo:visited:hover:focus {
            display: flex;
          }
        }
        """
    );
  }

  @Test
  public void extendColors() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-border-subtle border-black");
      }
    }

    test(
        Css.extendColors("""
        border-subtle: var(--ui-border-subtle)
        """),

        Subject.class,

        """
        .border-border-subtle { border-color: var(--ui-border-subtle) }
        .border-black { border-color: #000000 }
        """
    );
  }

  @Test
  public void extendSpacing() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("w-1 h-spacing-01");
      }
    }

    test(
        Css.extendSpacing("""
        spacing-01: var(--ui-spacing-01)
        """),

        Subject.class,

        """
        .h-spacing-01 { height: var(--ui-spacing-01) }
        .w-1 { width: 0.25rem }
        """
    );
  }

  @Test
  public void overrideBackgroundColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("bg bg-hover bg-slate-100");
      }
    }

    test(
        Css.overrideBackgroundColor("""
        : #ffffff
        hover: #c0c0c0
        """),

        Subject.class,

        """
        .bg { background-color: #ffffff }
        .bg-hover { background-color: #c0c0c0 }
        """
    );
  }

  @Test
  public void overrideBorderColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-subtle border-black");
      }
    }

    test(
        Css.overrideColors("""
        subtle: var(--ui-border-subtle)
        """),

        Subject.class,

        """
        .border-subtle { border-color: var(--ui-border-subtle) }
        """
    );
  }

  @Test
  public void overrideBorderWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-b-4 border-t-2px border-1px");
      }
    }

    test(
        Css.overrideBorderWidth("""
        2px: 2px
        1px: 1px
        """),

        Subject.class,

        """
        .border-1px { border-width: 1px }
        .border-t-2px { border-top-width: 2px }
        """
    );
  }

  @Test
  public void overrideColors() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-border-subtle border-black");
      }
    }

    test(
        Css.overrideColors("""
        border-subtle: var(--ui-border-subtle)
        """),

        Subject.class,

        """
        .border-border-subtle { border-color: var(--ui-border-subtle) }
        """
    );
  }

  @Test
  public void overrideContent() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("before:content-empty content-foo");
      }
    }

    test(
        Css.overrideContent("""
        empty: ""
        foo: url(foo.png)
        """),

        Subject.class,

        """
        .content-foo { content: url(foo.png) }
        .before\\:content-empty::before { content: "" }
        """
    );
  }

  @Test
  public void overrideFontSize() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-sm text-base text-body-compact-01");
      }
    }

    test(
        Css.overrideFontSize("""
        sm: 0.8rem
        base: 16px/24px
        body-compact-01: var(--ui-body-compact-01-font-size, 0.875rem)/var(--ui-body-compact-01-line-height, 1.28572)/var(--ui-body-compact-01-letter-spacing, 0.16px)/var(--ui-body-compact-01-font-weight, 400)
        """),

        Subject.class,

        """
        .text-sm { font-size: 0.8rem }
        .text-base { font-size: 16px; line-height: 24px }
        .text-body-compact-01 {
          font-size: var(--ui-body-compact-01-font-size, 0.875rem);
          line-height: var(--ui-body-compact-01-line-height, 1.28572);
          letter-spacing: var(--ui-body-compact-01-letter-spacing, 0.16px);
          font-weight: var(--ui-body-compact-01-font-weight, 400);
        }
        """
    );
  }

  @Test
  public void overrideFill() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("fill-primary fill-secondary fill-slate-100");
      }
    }

    test(
        Css.overrideFill("""
        primary: #ccddee
        secondary: #aabbcc
        """),

        Subject.class,

        """
        .fill-primary { fill: #ccddee }
        .fill-secondary { fill: #aabbcc }
        """
    );
  }

  @Test
  public void overrideGridColumn() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("col-span-12 col-span-14 col-span-16");
      }
    }

    test(
        Css.overrideGridColumn("""
        auto: auto
        span-12: span 12 / span 12
        span-16: span 12 / span 12
        """),

        Subject.class,

        """
        .col-span-12 { grid-column: span 12 / span 12 }
        .col-span-16 { grid-column: span 12 / span 12 }
        """
    );
  }

  @Test
  public void overrideGridColumnEnd() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("col-end-12 col-end-14 col-end-16");
      }
    }

    test(
        Css.overrideGridColumnEnd("""
        auto: auto
        12: 12
        16: 16
        """),

        Subject.class,

        """
        .col-end-12 { grid-column-end: 12 }
        .col-end-16 { grid-column-end: 16 }
        """
    );
  }

  @Test
  public void overrideGridColumnStart() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("col-start-12 col-start-14 col-start-16");
      }
    }

    test(
        Css.overrideGridColumnStart("""
        auto: auto
        12: 12
        16: 16
        """),

        Subject.class,

        """
        .col-start-12 { grid-column-start: 12 }
        .col-start-16 { grid-column-start: 16 }
        """
    );
  }

  @Test
  public void overrideGridTemplateColumns() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("grid-cols-12 grid-cols-14 grid-cols-16");
      }
    }

    test(
        Css.overrideGridTemplateColumns("""
        none: none
        12: repeat(12, minmax(0, 1fr))
        16: repeat(16, minmax(0, 1fr))
        """),

        Subject.class,

        """
        .grid-cols-12 { grid-template-columns: repeat(12, minmax(0, 1fr)) }
        .grid-cols-16 { grid-template-columns: repeat(16, minmax(0, 1fr)) }
        """
    );
  }

  @Test
  public void overrideGridTemplateRows() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("grid-rows-5 grid-rows-foo");
      }
    }

    test(
        Css.overrideGridTemplateRows("""
        foo: 48px auto
        """),

        Subject.class,

        """
        .grid-rows-foo { grid-template-rows: 48px auto }
        """
    );
  }

  @Test
  public void overrideSpacing() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("m-0px h-1px w-2px p-4px focus:py-16px");
      }
    }

    test(
        Css.overrideSpacing("""
        0px: 0px
        1px: 0.0625rem
        2px: 0.125rem
        4px: 0.25rem
        16px: 1rem
        """),

        Subject.class,

        """
        .m-0px { margin: 0px }
        .h-1px { height: 0.0625rem }
        .w-2px { width: 0.125rem }
        .p-4px { padding: 0.25rem }
        .focus\\:py-16px:focus { padding-top: 1rem; padding-bottom: 1rem }
        """
    );
  }

  @Test
  public void overrideZIndex() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("z-a z-foo z-20");
      }
    }

    test(
        Css.overrideZIndex("""
        a: 100
        foo: 9000
        """),

        Subject.class,

        """
        .z-a { z-index: 100 }
        .z-foo { z-index: 9000 }
        """
    );
  }

  @Test
  public void useLogicalProperties() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // border color
        className("border-white");
        className("border-x-white border-y-white");
        className("border-t-white border-r-white border-b-white border-l-white");
        // border radius
        className("rounded-none");
        className("rounded-t-none");
        className("rounded-r-none");
        className("rounded-b-none");
        className("rounded-l-none");
        className("rounded-tl-none");
        className("rounded-tr-none");
        className("rounded-br-none");
        className("rounded-bl-none");
        // border width
        className("border");
        className("border-x border-y");
        className("border-t border-r border-b border-l");
        // height
        className("h-4");
        className("min-h-8");
        className("max-h-12");
        // float
        className("float-right float-left");
        // clear
        className("clear-right clear-left");
        // margin
        className("m-auto");
        className("mx-auto");
        className("my-auto");
        className("mt-auto");
        className("mr-auto");
        className("mb-auto");
        className("ml-auto");
        // padding
        className("p-px");
        className("px-px");
        className("py-px");
        className("pt-px");
        className("pr-px");
        className("pb-px");
        className("pl-px");
        // width
        className("w-4");
        className("min-w-8");
        className("max-w-12");
        // inset
        className("inset-px");
        className("inset-x-px");
        className("inset-y-px");
        className("top-px");
        className("right-px");
        className("bottom-px");
        className("left-px");
        // size
        className("size-px");
      }
    }

    test(
        Css.useLogicalProperties(),

        Subject.class,

        """
        .inset-px { inset: 1px }
        .inset-x-px { inset-inline-start: 1px; inset-inline-end: 1px }
        .inset-y-px { inset-block-start: 1px; inset-block-end: 1px }
        .top-px { inset-block-start: 1px }
        .right-px { inset-inline-end: 1px }
        .bottom-px { inset-block-end: 1px }
        .left-px { inset-inline-start: 1px }
        .float-right { float: inline-end }
        .float-left { float: inline-start }
        .clear-right { clear: inline-end }
        .clear-left { clear: inline-start }
        .m-auto { margin: auto }
        .mx-auto { margin-inline-start: auto; margin-inline-end: auto }
        .my-auto { margin-block-start: auto; margin-block-end: auto }
        .mt-auto { margin-block-start: auto }
        .mr-auto { margin-inline-end: auto }
        .mb-auto { margin-block-end: auto }
        .ml-auto { margin-inline-start: auto }
        .size-px { block-size: 1px; inline-size: 1px }
        .h-4 { block-size: 1rem }
        .max-h-12 { max-block-size: 3rem }
        .min-h-8 { min-block-size: 2rem }
        .w-4 { inline-size: 1rem }
        .max-w-12 { max-inline-size: 3rem }
        .min-w-8 { min-inline-size: 2rem }
        .rounded-none { border-radius: 0px }
        .rounded-t-none { border-start-start-radius: 0px; border-start-end-radius: 0px }
        .rounded-r-none { border-start-end-radius: 0px; border-end-end-radius: 0px }
        .rounded-b-none { border-end-end-radius: 0px; border-end-start-radius: 0px }
        .rounded-l-none { border-end-start-radius: 0px; border-start-start-radius: 0px }
        .rounded-tl-none { border-start-start-radius: 0px }
        .rounded-tr-none { border-start-end-radius: 0px }
        .rounded-br-none { border-end-end-radius: 0px }
        .rounded-bl-none { border-end-start-radius: 0px }
        .border { border-width: 1px }
        .border-x { border-inline-start-width: 1px; border-inline-end-width: 1px }
        .border-y { border-block-start-width: 1px; border-block-end-width: 1px }
        .border-t { border-block-start-width: 1px }
        .border-r { border-inline-end-width: 1px }
        .border-b { border-block-end-width: 1px }
        .border-l { border-inline-start-width: 1px }
        .border-white { border-color: #ffffff }
        .border-x-white { border-inline-start-color: #ffffff; border-inline-end-color: #ffffff }
        .border-y-white { border-block-start-color: #ffffff; border-block-end-color: #ffffff }
        .border-t-white { border-block-start-color: #ffffff }
        .border-r-white { border-inline-end-color: #ffffff }
        .border-b-white { border-block-end-color: #ffffff }
        .border-l-white { border-inline-start-color: #ffffff }
        .p-px { padding: 1px }
        .px-px { padding-inline-start: 1px; padding-inline-end: 1px }
        .py-px { padding-block-start: 1px; padding-block-end: 1px }
        .pt-px { padding-block-start: 1px }
        .pr-px { padding-inline-end: 1px }
        .pb-px { padding-block-end: 1px }
        .pl-px { padding-inline-start: 1px }
        """
    );
  }

  @Test
  public void utility() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("theme-white md:theme-white");
      }
    }

    test(
        Css.utility(
            "theme-white",

            """
            --ui-border-subtle-00: #e0e0e0
            --ui-border-subtle-01: #c6c6c6
            --ui-border-subtle-02: #e0e0e0
            --ui-border-subtle-03: #c6c6c6
            --ui-border-subtle: var(--ui-border-subtle-00, #e0e0e0)
            """
        ),

        Subject.class,

        """
        .theme-white {
          --ui-border-subtle-00: #e0e0e0;
          --ui-border-subtle-01: #c6c6c6;
          --ui-border-subtle-02: #e0e0e0;
          --ui-border-subtle-03: #c6c6c6;
          --ui-border-subtle: var(--ui-border-subtle-00, #e0e0e0);
        }

        @media (min-width: 768px) {
          .md\\:theme-white {
            --ui-border-subtle-00: #e0e0e0;
            --ui-border-subtle-01: #c6c6c6;
            --ui-border-subtle-02: #e0e0e0;
            --ui-border-subtle-03: #c6c6c6;
            --ui-border-subtle: var(--ui-border-subtle-00, #e0e0e0);
          }
        }
        """
    );
  }

  @Test
  public void variants() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("thead:bg-black thead:hover:bg-white");
        className("thead:tr:border");
      }
    }

    test(
        Css.variants("""
        thead: & thead
        tr: & tr
        """),

        Subject.class,

        """
        .thead\\:tr\\:border thead tr { border-width: 1px }
        .thead\\:bg-black thead { background-color: #000000 }
        .thead\\:hover\\:bg-white thead:hover { background-color: #ffffff }
        """
    );
  }

  @Test
  public void variantsDefault() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("focus:not-sr-only active:bg-white hover:bg-black *:rounded-full");
        className("visited:text-white visited:hover:text-black");
        className("before:block after:text-black");
        className("ltr:ml-1 rtl:mr-1");
        className("disabled:bg-black");
      }
    }

    test(
        Subject.class,

        """
        .focus\\:not-sr-only:focus {
          position: static;
          width: auto;
          height: auto;
          padding: 0;
          margin: 0;
          overflow: visible;
          clip: auto;
          white-space: normal;
        }
        .rtl\\:mr-1:where([dir="rtl"], [dir="rtl"] *) { margin-right: 0.25rem }
        .ltr\\:ml-1:where([dir="ltr"], [dir="ltr"] *) { margin-left: 0.25rem }
        .before\\:block::before { display: block }
        .\\*\\:rounded-full > * { border-radius: 9999px }
        .hover\\:bg-black:hover { background-color: #000000 }
        .active\\:bg-white:active { background-color: #ffffff }
        .disabled\\:bg-black:disabled { background-color: #000000 }
        .after\\:text-black::after { color: #000000 }
        .visited\\:text-white:visited { color: #ffffff }
        .visited\\:hover\\:text-black:visited:hover { color: #000000 }
        """
    );
  }

  @Test
  public void resetTest() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("hidden");
      }
    }

    String result;
    result = Css.generateCss(
        Css.classes(Subject.class),

        Css.noteSink(TestingNoteSink.INSTANCE)
    );

    assertTrue(result.contains("sans"));
    assertTrue(result.contains("mono"));
    assertTrue(result.endsWith(".hidden { display: none }\n"));
  }

  // edge cases

  @Test
  public void negativeMargin() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("-ml-2 md:hidden");
      }
    }

    test(
        Subject.class,

        """
        .-ml-2 { margin-left: -0.5rem }

        @media (min-width: 768px) {
          .md\\:hidden { display: none }
        }
        """
    );
  }

  // scan directory

  private static final String SOURCE1 = """
  cafebabe00000041004007000201001874657374696e672f736974652f7765622f536f757263653107000401001a6f626a6563746f732f7761792f48746d6c2454656d706c617465010009434f4e5441494e455201001d4c6f626a6563746f732f7761792f48746d6c24436c6173734e616d653b0100083c\
  636c696e69743e010003282956010004436f646508000b0100176d782d6175746f20772d66756c6c2070782d313670780a0a000d000f07000e0100116f626a6563746f732f7761792f48746d6c0c00100011010009636c61737354657874010031284c6a6176612f6c616e672f537472696e673b294c6f62\
  6a6563746f732f7761792f48746d6c24436c6173734e616d653b09000100130c0005000601000f4c696e654e756d6265725461626c650100124c6f63616c5661726961626c655461626c650100063c696e69743e0a000300180c001600080100047468697301001a4c74657374696e672f736974652f7765\
  622f536f75726365313b01000672656e64657207001d01001d6f626a6563746f732f7761792f48746d6c24496e737472756374696f6e08001f01000862672d77686974650a000100210c00220023010009636c6173734e616d6501003c284c6a6176612f6c616e672f537472696e673b294c6f626a656374\
  6f732f7761792f48746d6c24417474726962757465496e737472756374696f6e3b0a000100250c00260027010004626f6479010048285b4c6f626a6563746f732f7761792f48746d6c24496e737472756374696f6e3b294c6f626a6563746f732f7761792f48746d6c24456c656d656e74496e7374727563\
  74696f6e3b0a000100290c002a002701000468746d6c01000a536f7572636546696c6501000c536f75726365312e6a61766101001b52756e74696d65496e76697369626c65416e6e6f746174696f6e730100194c6f626a6563746f732f7761792f43737324536f757263653b01000c496e6e6572436c6173\
  7365730700310100176f626a6563746f732f7761792f43737324536f757263650700330100106f626a6563746f732f7761792f437373010006536f757263650700360100266f626a6563746f732f7761792f48746d6c24417474726962757465496e737472756374696f6e01001441747472696275746549\
  6e737472756374696f6e07003901001b6f626a6563746f732f7761792f48746d6c24436c6173734e616d65010009436c6173734e616d6507003c0100246f626a6563746f732f7761792f48746d6c24456c656d656e74496e737472756374696f6e010012456c656d656e74496e737472756374696f6e0100\
  0b496e737472756374696f6e01000854656d706c61746500300001000300000001001a0005000600000003000800070008000100090000002d0001000000000009120ab8000cb30012b10000000200140000000a0002000000180008001a0015000000020000000000160008000100090000002f00010001\
  000000052ab70017b10000000200140000000600010000001600150000000c0001000000050019001a00000014001b00080001000900000066000a0001000000242a04bd001c59032a05bd001c5903b200125359042a121eb6002053b6002453b6002857b10000000200140000001e00070000001e000700\
  1f000e002000140022001b001f001f001e0023002500150000000c0001000000240019001a00000003002b00000002002c002d000000060001002e0000002f00000032000600300032003426090035000d003706090038000d003a0609003b000d003d0609001c000d003e06090003000d003f0409\
  """;

  private static final String SOURCE2 = """
  cafebabe00000041002807000201001874657374696e672f736974652f7765622f536f757263653207000401001a6f626a6563746f732f7761792f48746d6c2454656d706c6174650100063c696e69743e010003282956010004436f64650a000300090c0005000601000f4c696e654e756d626572546162\
  6c650100124c6f63616c5661726961626c655461626c650100047468697301001a4c74657374696e672f736974652f7765622f536f75726365323b01000672656e64657207001001001d6f626a6563746f732f7761792f48746d6c24496e737472756374696f6e080012010004677269640a000100140c00\
  150016010009636c6173734e616d6501003c284c6a6176612f6c616e672f537472696e673b294c6f626a6563746f732f7761792f48746d6c24417474726962757465496e737472756374696f6e3b0a000100180c0019001a010003646976010048285b4c6f626a6563746f732f7761792f48746d6c24496e\
  737472756374696f6e3b294c6f626a6563746f732f7761792f48746d6c24456c656d656e74496e737472756374696f6e3b01000a536f7572636546696c6501000c536f75726365322e6a61766101000c496e6e6572436c617373657307001f0100266f626a6563746f732f7761792f48746d6c2441747472\
  6962757465496e737472756374696f6e0700210100116f626a6563746f732f7761792f48746d6c010014417474726962757465496e737472756374696f6e0700240100246f626a6563746f732f7761792f48746d6c24456c656d656e74496e737472756374696f6e010012456c656d656e74496e73747275\
  6374696f6e01000b496e737472756374696f6e01000854656d706c617465003000010003000000000002000000050006000100070000002f00010001000000052ab70008b100000002000a00000006000100000014000b0000000c000100000005000c000d00000014000e00060001000700000041000600\
  01000000132a04bd000f59032a1211b6001353b6001757b100000002000a0000000a00020000001800120019000b0000000c000100000013000c000d00000002001b00000002001c001d000000220004001e0020002206090023002000250609000f0020002606090003002000270409\
  """;

  @Test
  public void scanDirectory01() throws IOException {
    Path directory;
    directory = TestingDir.next();

    TestingDir.hexDump(directory.resolve("testing/site/Source1.class"), SOURCE1);
    TestingDir.hexDump(directory.resolve("testing/site/Source2.class"), SOURCE2);

    String result;
    result = Css.generateCss(
        Css.scanDirectory(directory),

        Css.noteSink(TestingNoteSink.INSTANCE),

        Css.skipReset()
    );

    assertEquals(
        result,

        """
        .mx-auto { margin-left: auto; margin-right: auto }
        .w-full { width: 100% }
        .bg-white { background-color: #ffffff }
        """
    );
  }

  private static final Css.Option COLORS = Css.overrideColors("""
  inherit: inherit
  current: currentColor
  transparent: transparent

  black: #000000
  white: #ffffff
  """);

  private void test(Class<?> type, String expected) {
    String result;
    result = Css.generateCss(
        Css.classes(type),

        Css.noteSink(TestingNoteSink.INSTANCE),

        COLORS,

        Css.skipReset()
    );

    assertEquals(result, expected);
  }

  private void test(Css.Option extraOption, Class<?> type, String expected) {
    String result;
    result = Css.generateCss(
        Css.classes(type),

        Css.noteSink(TestingNoteSink.INSTANCE),

        COLORS,

        Css.skipReset(),

        extraOption
    );

    assertEquals(result, expected);
  }

  private void test(Css.Option extra01, Css.Option extra02, Class<?> type, String expected) {
    String result;
    result = Css.generateCss(
        Css.classes(type),

        Css.noteSink(TestingNoteSink.INSTANCE),

        COLORS,

        Css.skipReset(),

        extra01, extra02
    );

    assertEquals(result, expected);
  }

}