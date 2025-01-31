suite("Action::navigate test", function() {

  setup(function() {
    clearWorkArea();
    this.errorStub = sinon.stub(console, 'error');
    this.server = makeServer();
    thrown = null;
  });

  teardown(function() {
    this.server.restore();
    this.errorStub.restore();
  });

  test("it should perform a soft navigation", function() {
    make(`
		<div id='before' data-frame='x:before'>
		<a id='link' href='/after' data-on-click='[["navigate-0"]]'>Before</a>
		</div>`);

    const after = makeElement(`
		<html>
		<div id='after' data-frame='x:after'>After</div>
		</html>`);

    this.server.respondWith(
      "GET", "/after",
      [200, { "Content-Type": "text/html" }, after.outerHTML]
    );

    const link = byId("link");

    link.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, after.outerHTML);

    const requests = this.server.requests;

    assert.equal(requests.length, 1);
    assert.propertyVal(requests[0].requestHeaders, "Way-Request", "true");
  });

  test("it should fail if element is not an anchor", function() {
    make(`
		<div id='before' data-frame='x:before'>
		<button id='link' href='/after' data-on-click='[["navigate-0"]]'>Before</button>
		</div>`);

    const link = byId("link");

    link.click();

    assert.isNotNull(thrown);
    assert.equal(thrown.message, "Illegal element: navigate-0 must be executed on an HTMLAnchorElement but got HTMLButtonElement");
  });

  test("it should fail if anchor has no 'href' attribute", function() {
    make(`
		<div id='before' data-frame='x:before'>
		<a id='link' data-on-click='[["navigate-0"]]'>Before</button>
		</div>`);

    const link = byId("link");

    link.click();

    assert.isNotNull(thrown);
    assert.equal(thrown.message, `Invalid type: expected String value but query [href] on <a id="link" data-on-click="[[&quot;navigate-0&quot;]]"></a> returned null`);
  });

});