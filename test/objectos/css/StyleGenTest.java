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
package objectos.css;

import static org.testng.Assert.assertEquals;

import objectos.html.HtmlTemplate;
import objectos.way.TestingNoteSink;
import org.testng.annotations.Test;

public class StyleGenTest {

  private static abstract class AbstractSubject extends HtmlTemplate {
    @Override
    protected final void definition() {
      div(
          include(this::classes)
      );
    }

    abstract void classes();
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
        """
    );
  }

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

  private void test(Class<?> type, String expected) {
    WayStyleGen gen;
    gen = new WayStyleGen();

    gen.noteSink(TestingNoteSink.INSTANCE);

    gen.scan(type);

    assertEquals(gen.generate(), expected);
  }

}