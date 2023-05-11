package objectos.code.internal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import objectos.code.JavaTemplate._Item;
import objectos.code.internal.InternalApi.OffsetKindResult;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InternalApiTest {

  @Test
  public void offsetKind_Item_is_0_local() {
    // given
    Object obj = _Item.INSTANCE;

    // when
    OffsetKindResult offsetKind = InternalApi.getOffsetKind(obj);

    // then
    assertEquals(offsetKind.offset(), 0);
    assertEquals(offsetKind.kind(), InternalApi.LOCAL);
  }

  @Test
  public void throws_exception_on_unknown_object_type() {
    // given
    Object obj = new BigDecimal(0);

    // expect
    var unsupportedOperationException = Assert.expectThrows(
        UnsupportedOperationException.class,
        () -> InternalApi.getOffsetKind(obj));
    assertTrue(unsupportedOperationException.getMessage().contains("Implement me :: obj="));
  }

  @Test
  public void modifyLambda_on_lambda() {
    // given
    var api = new InternalApi();
    var offsetKind = new OffsetKindResult(2, InternalApi.LAMBDA);

    // when
    api.modifyLambdaOrProto(offsetKind, 2);

    // then
    assertEquals(api.protoArray[0], 0);
    assertEquals(api.protoArray[1], 0);
    assertEquals(api.protoArray[2], 0);
  }
}
