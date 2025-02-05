suite("Action::toggleClass test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("it should add the class if it is not present", function() {
    const clickId = "f";

    const onClick = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "add-me"]
    ]);

    make(`
		<div id='${clickId}' data-on-click='${onClick}'>
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

  test("it should remove the class if it is present", function() {
    const clickId = "f";

    const onClick = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "remove-me"]
    ]);

    make(`
    <div id='${clickId}' data-on-click='${onClick}'>
    <div id='subject' class='foo remove-me'></div>
    </div>		
    `);

    byId(clickId).click();

    const subject = byId("subject");

    const l = subject.classList;

    assert.equal(l.length, 1);
    assert.isTrue(l.contains("foo"));
    assert.isFalse(l.contains("remove-me"));
  });

  test("it should toggle all of the classes", function() {
    const clickId = "f";

    const onClick = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "add-me", "remove-me", "another-add"]
    ]);

    make(`
    <div id='${clickId}' data-on-click='${onClick}'>
    <div id='subject' class='foo remove-me'></div>
    </div>		
    `);

    byId(clickId).click();

    const subject = byId("subject");

    const l = subject.classList;

    assert.equal(l.length, 3);
    assert.isTrue(l.contains("foo"));
    assert.isTrue(l.contains("add-me"));
    assert.isTrue(l.contains("another-add"));
  });

});