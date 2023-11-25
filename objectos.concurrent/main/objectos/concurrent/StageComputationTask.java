/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.concurrent;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import objectos.lang.object.Check;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;

/**
 * A base async computation implementation providing an imperative programming
 * style.
 *
 * <p>
 * This class is useful for computing a result that depends on many intermediate
 * asynchronous computations. It does so:
 *
 * <ul>
 * <li>offering a (mostly) imperative programming style;</li>
 * <li>supporting chaining without callbacks; and</li>
 * <li>in non-blocking way.</li>
 * </ul>
 *
 * <p>
 * The (mostly) imperative programming style is a design choice trying to
 * provide:
 *
 * <ul>
 * <li>code that is <em>somewhat</em> easy to read and reason about; and</li>
 * <li>a <em>somewhat</em> acceptable debugging process.</li>
 * </ul>
 *
 * <h2>Caveats</h2>
 *
 * <p>
 * Before using this class, please be aware of its many caveats. As mentioned
 * earlier, the imperative programming style is a design choice. It means the
 * (eventual) benefits it provides come with costs. In other words, there are
 * a number tradeoffs.
 *
 * <p>
 * The first tradeoff is that implementations are prone to
 * {@link ClassCastException}s. While the class provide ways to minimize their
 * occurrence, implementations are prone to them as the type of the result of a
 * triggering stage is always lost in the subsequent stage.
 *
 * <p>
 * A second tradeoff is regarding control flow. If a stage needs to be skipped
 * or if the programmer needs to loop through one or more stages then a
 * {@link #goTo(int) goto} must be used.
 *
 * <p>
 * A third tradeoff is that implementations must be written in a very specific
 * way, obeying a number of rules. This can be very error-prone.
 *
 * <h2>Programming style comparison</h2>
 *
 * <p>
 * This section implements the same use-case in different ways. This is to
 * compare the programming style of this class to the programming style using
 * classes from the {@link java.util.concurrent} package.
 *
 * <p>
 * The example is a service for obtaining quotes for a flight from all the
 * airlines flying from a given airport. After getting the responses from the
 * airlines, a HTML report is rendered.
 *
 * <p>
 * First, using this class, an example implementation could be (note that the
 * following classes are all made-up):
 *
 * <pre>{@code
 * public final ObjectosConcurrentExample
 *     extends StageComputationTask<String, QuoteException> {
 *   private final Service service;
 *
 *   private String fromAirportCode;
 *   private String toAirportCode;
 *   private Date preferredFlightTime;
 *
 *   ObjectosConcurrentExample(Service service) {
 *     this.service = service;
 *   }
 *
 *   protected final void executeStage(int stage) throws Exception {
 *     switch (stage) {
 *       case 0:
 *         final Computation<Collection<Airline>> airlinesQuery;
 *         airlinesQuery = service.airlinesFlyingFrom(fromAirportCode);
 *
 *         addComputation(airlinesQuery);
 *
 *       case 1:
 *         if (isComputing()) {
 *           return;
 *         }
 *
 *         final Collection<Airline> airlines;
 *         airlines = get();
 *
 *         for (Airline airline : airlines) {
 *           Computation<Quote> cq;
 *           cq = service.quoteFlight(airline, fromAirportCode, toAirportCode, preferredFlightTime);
 *
 *           addComputation(cq);
 *         }
 *
 *       case 2:
 *         if (isComputing()) {
 *           return;
 *         }
 *
 *         final List<Quote> quotes;
 *         quotes = getAll();
 *
 *         final Computation<String> htmlTask;
 *         htmlTask = service.renderHtmlResponse(quotes);
 *
 *         addComputation(htmlTask);
 *
 *       case 3:
 *         if (isComputing()) {
 *           return;
 *         }
 *
 *         final String html;
 *         html = get();
 *
 *         setResult(html);
 *
 *         return;
 *
 *       default:
 *         throw new AssertionError("Unexpected stage=" + stage);
 *     }
 *   }
 * }
 * }</pre>
 *
 * <p>
 * Next, an example using the {@link Future} class. This would work
 * in Java6+ environments. Please note that, in this
 * version, the methods of the made-up {@code Service} class return
 * {@link Future}s and not {@link Computation}s.
 *
 * <pre>{@code
 * public final FutureExample implements Callable<String> {
 *   private final Service service;
 *
 *   private String fromAirportCode;
 *   private String toAirportCode;
 *   private Date preferredFlightTime;
 *
 *   FutureExample(Service service) {
 *     this.service = service;
 *   }
 *
 *   public final void setRequest(String from, String to, Date time) {
 *     this.fromAirportCode = from;
 *     this.toAirportCode = to;
 *     this.preferredFlightTime = time;
 *   }
 *
 *   public final String call() throws Exception {
 *     Future<Collection<Airline>> airlinesQuery;
 *     airlinesQuery = service.airlinesFlyingFrom(fromAirportCode);
 *
 *     Collection<Airline> airlines;
 *     airlines = airlinesQuery.get();
 *
 *     List<Future<Quote>> quotesQuery;
 *     quotesQuery = new ArrayList<Future<Quote>>(airlines.size());
 *
 *     for (Airline airline : airlines) {
 *       Future<Quote> cq;
 *       cq = service.quoteFlight(airline, fromAirportCode, toAirportCode, preferredFlightTime);
 *
 *       quotesQuery.add(cq);
 *     }
 *
 *     List<Quote> quotes;
 *     quotes = new ArrayList<Quote>(quotesQuery.size());
 *
 *     for (Future<Quote> f : quotesQuery) {
 *       Quote q;
 *       q = f.get();
 *
 *       quotes.add(q);
 *     }
 *
 *     Future<String> htmlTask;
 *     htmlTask = service.renderHtmlResponse(quotes);
 *
 *     return htmlTask.get();
 *   }
 * }
 * }</pre>
 *
 * <p>
 * The first version is not as straightforward as the second version. The first
 * version requires a switch statement, the switch cases are fall-through and,
 * except for the first case, all subsequent cases start with an if statement.
 *
 * <p>
 * The benefit of the first version compared to the {@code FutureExample}, is
 * the absence of blocking calls such as {@link Future#get()}.
 *
 * <h2>Extending</h2>
 *
 * <p>
 * For more details on how to extend this class, read the javadoc for the
 * methods:
 *
 * <ul>
 * <li>{@link #executeStage(int)}</li>
 * <li>{@link #get()}</li>
 * <li>{@link #getAll()}</li>
 * <li>{@link #goTo(int)}</li>
 * <li>{@link #hasComputations()}</li>
 * <li>{@link #isComputing()}</li>
 * <li>{@link #setResult(Object)}</li>
 * </ul>
 *
 * @param <V>
 *        the type of the result of this computation
 *
 * @since 3
 */
public abstract class StageComputationTask<V> implements Computation<V>, CpuTask {

  private static final byte _FINALLY = 1;

  private static final byte _STAGE = 2;

  private static final byte _STOP = 0;

  private static final int FRESULT_CONSUMED = 1 << 1;

  private static final int FSTAGE_INCREMENTED = 1 << 2;

  private final GrowableList<Computation<?>> computations = new GrowableList<>();

  private Throwable error;

  private int flags = FRESULT_CONSUMED;

  private V result;

  private final GrowableList<Object> results = new GrowableList<>();

  private int stage;

  private volatile byte state;

  /**
   * Sole constructor.
   */
  protected StageComputationTask() {}

  /**
   * {@inheritDoc}
   */
  @Override
  public final void executeOne() {
    state = execute(state);
  }

  /**
   * {@inheritDoc}
   *
   * @throws NoSuchElementException
   *         if this computation completed normally but produced no result
   */
  @Override
  public final V getResult() throws IllegalStateException, ExecutionException {
    Check.state(!isActive(), "computation is active");
    Check.state(!isSet(FRESULT_CONSUMED), "result already consumed");

    set(FRESULT_CONSUMED);

    if (error != null) {
      throw new ExecutionException(error);
    }

    if (result == null) {
      throw new NoSuchElementException("Expected to have a result but no result was produced");
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isActive() {
    return state != _STOP;
  }

  /**
   * Adds the specified computation to the computations list. The result of the
   * supplied computation (if any) will be available in this task's next stage.
   *
   * <p>
   * Please note that adding computations of different types in the same stage
   * is highly discouraged as it will make it harder to consume the results in
   * the subsequent stage.
   *
   * @param computation
   *        the computation whose result should be available in the next stage
   */
  protected final void addComputation(Computation<?> computation) {
    computations.addWithNullMessage(computation, "computation == null");
  }

  /**
   * Method called before this task stops executing (either normally or
   * abruptly).
   */
  protected void executeFinally() {}

  /**
   * <p>
   * Executes the specified (zero-based) stage. Stage is incremented in the
   * following condition:
   *
   * <ul>
   * <li>one or more invocations of the {@link #addComputation(Computation)}
   * method; and</li>
   * <li>invocation of the {@link #isComputing()}.</li>
   * </ul>
   *
   * <p>
   * Execution will stop after a result has been set by calling the
   * {@link #setResult(Object)} method.
   *
   * <p>
   * Therefore, implementations are strongly advised to follow the general
   * template:
   *
   * <pre>{@code
   * protected final void executeStage(int stage) throws Exception {
   *   switch (stage) {
   *     case 0:
   *       ...
   *       // at least one computation
   *       addComputation(...);
   *
   *       // fall-through
   *     case 1:
   *       // isComputing() method will increment stage
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       SomeType resultStage0 = get();
   *
   *       // one or more computations
   *       addComputation(...);
   *
   *       // fall-through
   *     case 2:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       ...
   *
   *     case N:
   *       AnotherType result = get();
   *
   *       // signal stop
   *       setResult(result);
   *
   *       return;
   *
   *     default:
   *       // fail-fast in case of programming error
   *       throw new AssertionError("unexpected stage=" + stage);
   *   }
   * }
   * }</pre>
   *
   * <p>
   * For more complex control flow requirements the {@link #goTo(int)} method
   * must be used.
   *
   * @param stage
   *        the zero-based stage to be executed by this method
   *
   * @throws Exception
   *         if an error occurs
   */
  protected abstract void executeStage(int stage) throws Exception;

  /**
   * <p>
   * Returns the one (and only one) result from the computation submitted in the
   * previous stage.
   *
   * <p>
   * If the previous stage did not submit exactly one computation then the
   * {@link #getAll()} method should be called instead.
   *
   * <p>
   * This method should usually be called right after the {@link #isComputing()}
   * if statement like so:
   *
   * <pre>{@code
   * protected final void executeStage(int stage) throws Exception {
   *   switch (stage) {
   *       ...
   *     case 5:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       final Item item = get();
   *
   *       addComputation(process(item));
   *
   *       // fall-through
   *     case 6:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       ...
   *   }
   * }
   * }</pre>
   *
   * <p>
   * Please note that this method is generic only as a small convenience to the
   * programmer. For practical purposes, it mostly behaves as if
   * its return type was declared as {@link Object}. In other words, it is the
   * programmer responsibility to keep track of the correct types; the compiler
   * will not be able to help here. The reason being that the type of the
   * computation in a triggering stage is completely lost in the subsequent
   * stage. As mentioned in this class documentation, this is a design choice
   * and a concious tradeoff.
   *
   * @param <T>
   *        the type to cast the result to
   *
   * @return the one (and only one) result from the computation submitted in the
   *         previous stage
   *
   * @throws ClassCastException
   *         if the result cannot be cast to the type {@code <T>}
   * @throws IllegalStateException
   *         if there are zero results or more than one result.
   *
   * @see #getAll()
   */
  @SuppressWarnings("unchecked")
  protected final <T> T get() throws ClassCastException {
    Check.state(results.size() == 1,
        "More than one result: getAll() should have been called");

    Object o;
    o = results.get(0);

    return (T) o;
  }

  /**
   * <p>
   * Returns all of the results from all of the computations submitted in the
   * previous stage. The order of the results is, for all purposes, random.
   * Results are collected as they become available. In other
   * words, the result of the first computation submitted <b>is not</b>
   * guaranteed to be the first item of the returned list.
   *
   * <p>
   * This method should usually be called right after the {@link #isComputing()}
   * if statement like so:
   *
   * <pre>{@code
   * protected final void executeStage(int stage) throws Exception {
   *   switch (stage) {
   *       ...
   *     case 5:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       final UnmodifiableList<Item> items;
   *       items = getAll();
   *
   *       for (Item item : items) {
   *         addComputation(process(item));
   *       }
   *
   *       // fall-through
   *     case 6:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       ...
   *   }
   * }
   * }</pre>
   *
   * <p>
   * Please note that this method is generic only as a small convenience to the
   * programmer. For practical purposes, it mostly behaves as if
   * its return type was declared as {@code UnmodifiableList<Object>}. In other
   * words, it is the programmer responsibility to keep track of the correct
   * types; the compiler will not be able to help here. The reason being that
   * the type of the computation in a triggering stage is completely lost in the
   * subsequent stage. As mentioned in this class documentation, this is a
   * design choice and a concious tradeoff.
   *
   * <p>
   * As an extra warning regarding {@link ClassCastException}s, please note
   * that the exception will be thrown only when the list's elements are
   * accessed. This is different from the {@link #get()} method that
   * <em>fails-fast</em> with regards to cast exceptions.
   *
   * @param <T>
   *        the type to cast the result to
   *
   * @return all of the results from the all of the computations submitted in
   *         the previous stage
   *
   * @see #get()
   */
  @SuppressWarnings("unchecked")
  protected final <T> UnmodifiableList<T> getAll() {
    UnmodifiableList<Object> list;
    list = results.toUnmodifiableList();

    return (UnmodifiableList<T>) list;
  }

  /**
   * Sets the current stage to the specified value and sets the <em>stage
   * incremented</em> flag. As the name of the method suggests, this is
   * used for control flow.
   *
   * <p>
   * As this method sets the <em>stage incremented</em> flag, the next
   * {@link #isComputing()} invocation <b>will not</b> increment the stage
   * value.
   *
   * <p>
   * Programmers extending this class and using this method are recommend to
   * always write a {@code return} statement immediately after invoking this
   * method.
   *
   * @param stage
   *        the (zero-based) stage to branch to
   *
   * @see #isComputing()
   */
  protected final void goTo(int stage) {
    this.stage = stage;

    set(FSTAGE_INCREMENTED);

    results.clear();
  }

  /**
   * Returns {@code true} if the current stage has any computations;
   * {@code false} otherwise.
   *
   * <p>
   * This method is useful in situations like the following:
   *
   * <pre>{@code
   * protected final void executeStage(int stage) throws Exception {
   *   switch (stage) {
   *     // ...
   *     case 3:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       List<Item> items = getAll();
   *
   *       for (Item item : items) {
   *         if (item.shouldProcessed()) {
   *           addComputation(service.process(item));
   *         }
   *       }
   *
   *       if (!hasComputations()) {
   *         // no items to be processed
   *         // skip to stage 5
   *         goTo(5);
   *
   *         return;
   *       }
   *
   *       // else...
   *       // there are items to be processed so proceed
   *
   *     case 4:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       // ...
   *
   *     case 5:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       // ...
   *     // ...
   *   }
   * }
   * }</pre>
   *
   * @return {@code true} if the current stage has any computations;
   *         {@code false} otherwise
   *
   * @see #goTo(int)
   */
  protected final boolean hasComputations() {
    return !computations.isEmpty();
  }

  /**
   * <p>
   * Returns {@code true} if there are active computations (and no
   * computation completed exceptionally). If any computation completed
   * exceptionally then this method does not return and throws an exception
   * instead. If all of the computations completed normally then their results
   * are collected and can be obtained via either the {@link #get()} method or
   * the {@link #getAll()} method.
   *
   * <p>
   * Except for the first stage (stage {@code 0}), every stage must have a check
   * with this method:
   *
   * <pre>{@code
   * protected final void executeStage(int stage) throws Exception {
   *   switch (stage) {
   *     case 0:
   *       // no if statement
   *
   *       (...)
   *
   *     case 1:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       (...)
   *
   *     case 2:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       (...)
   *
   *     case 3:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       (...)
   *
   *     case N:
   *       if (isComputing()) {
   *         return;
   *       }
   *
   *       (...)
   *
   *     default:
   *       throw new AssertionError("Unexpected stage=" + stage);
   *   }
   * }
   * }</pre>
   *
   * <p>
   * This method participates in the stage increment control. The first time
   * this method is invoked in a stage, and if the <em>stage incremented</em> is
   * not set, the following occurs:
   *
   * <ul>
   * <li>the stage is incremented by one;</li>
   * <li>the <em>stage incremented</em> flag is set; and</li>
   * <li>all results from the previous stage are discarded. That is, they will
   * not be available anymore via the {@link #get()} method nor
   * {@link #getAll()} method.</li>
   * </ul>
   *
   * <p>
   * Next, for each computation:
   *
   * <ul>
   * <li>if the computation is active, it is added again to this task;</li>
   * <li>if the computation completed normally, its result is added to this
   * task; or</li>
   * <li>if the computation completed exceptionally, its exception is thrown
   * immediately.</li>
   * </ul>
   *
   * <p>
   * Lastly, if there is any computation active, the method returns
   * {@code true}. If not, that is, if all computations completed normally, then
   * the <em>stage incremented</em> flag is cleared and the method returns
   * {@code false}.
   *
   * @return {@code true} if there are active computations; {@code false}
   *         otherwise
   *
   * @throws Exception
   *         if any computation completes exceptionally
   *
   * @see #get()
   * @see #getAll()
   */
  protected final boolean isComputing() throws Exception {
    if (!isSet(FSTAGE_INCREMENTED)) {
      stage++;

      set(FSTAGE_INCREMENTED);

      results.clear();
    }

    Exception rethrow;
    rethrow = null;

    if (!computations.isEmpty()) {
      UnmodifiableList<Computation<?>> copy;
      copy = computations.toUnmodifiableList();

      computations.clear();

      int index;
      index = 0;

      for (int size = copy.size(); index < size;) {
        Computation<?> c;
        c = copy.get(index++);

        if (c.isActive()) {
          computations.add(c);

          continue;
        }

        Object r;

        try {
          r = c.getResult();
        } catch (Exception e) {
          rethrow = e;

          computations.clear();

          results.clear();

          break;
        }

        results.add(r);
      }
    }

    if (rethrow != null) {
      throw rethrow;
    }

    if (computations.isEmpty()) {
      unset(FSTAGE_INCREMENTED);

      return false;
    } else {
      return true;
    }
  }

  /**
   * Resets this task making it suitable to be executed again.
   *
   * @throws IllegalStateException
   *         if this task is active or if the result of this task has not been
   *         consumed
   */
  protected final void reset() {
    Check.state(state == _STOP, "task is active");

    Check.state(isSet(FRESULT_CONSUMED), "previous result was not consumed");

    computations.clear();

    error = null;

    flags = 0;

    result = null;

    results.clear();

    state = _STAGE;
  }

  /**
   * <p>
   * Sets this task's result and signals it to stop executing. The signaling to
   * stop executing works as follows:
   *
   * <p>
   * First, this method will set the internal state to the <em>finally</em>
   * state.
   *
   * <p>
   * Second, in the in the next {@link #executeOne()} invocation:
   *
   * <ul>
   * <li>any computation or result will be cleared;</li>
   * <li>stage will be reset to {@code 0}; and</li>
   * <li>the {@link #executeFinally()} method will be executed.</li>
   * </ul>
   *
   * <p>
   * Lastly, the task is stopped. That is, the {@link #isActive()} method
   * returns {@code false}.
   *
   * <p>
   * The result (if any) can then be obtained via the {@link #getResult()}
   * method.
   *
   * @param newResult
   *        the value to be set as the result of this task
   *
   * @see #executeFinally()
   */
  protected final void setResult(V newResult) {
    result = newResult;

    state = _FINALLY;
  }

  private byte execute(byte state) {
    switch (state) {
      case _FINALLY:
        return executeFinally0();
      case _STAGE:
        return executeStage();
      default:
        throw new UnsupportedOperationException("Implement me: state=" + state);
    }
  }

  private byte executeFinally0() {
    try {
      computations.clear();

      results.clear();

      stage = 0;

      executeFinally();
    } catch (Throwable e) {
      if (error != null) {
        error.addSuppressed(e);
      } else {
        error = e;
      }
    }

    return _STOP;
  }

  private byte executeStage() {
    try {
      executeStage(stage);

      return state;
    } catch (Throwable e) {
      error = e;

      return _FINALLY;
    }
  }

  private boolean isSet(int mask) {
    return (flags & mask) != 0;
  }

  private void set(int mask) {
    flags |= mask;
  }

  private void unset(int mask) {
    flags &= ~mask;
  }

}