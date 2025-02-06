suite("Query::byId test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("it should query an element by querying an attribute", function() {
    const clickId = "f";

    const onClick = JSON.stringify([
      ["id-2", ["id-1", "foo", "getAttribute", "data-item"], "toggle-class-0", "add-me"]
    ]);

    make(`
		<div id='${clickId}' data-on-click='${onClick}'>
    <div id='foo' data-item='subject'></div>
		<div id='subject' class='foo'></div>
		</div>		
		`);

    byId(clickId).click();

    const subject = byId("subject");

    const l = subject.classList;

    assert.equal(l.length, 2);
    assert.isTrue(l.contains("foo"));
    assert.isTrue(l.contains("add-me"));
  });

});