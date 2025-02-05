suite("Action::stopPropagation test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("WITHOUT stopPropagation it SHOULD fire the parent action", function() {
    const parentId = "parent";

    const toggleClass = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "bar"]
    ]);

    const childId = "child";

    make(`
		<div id='${parentId}' data-on-click='${toggleClass}'>
		<div id='${childId}'></div>
		<div id='subject' class='foo'></div>
		</div>
		`);

    byId(childId).click();

    const subject = byId("subject");

    const l = subject.classList;

    assert.equal(l.length, 2);
    assert.isTrue(l.contains("foo"));
    assert.isTrue(l.contains("bar"));
  });

  test("WITH stopPropagation it SHOULD NOT fire the parent action", function() {
    const parentId = "parent";

    const toggleClass = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "bar"]
    ]);

    const childId = "child";

    const stopPropagation = JSON.stringify([
      ["stop-propagation-0"]
    ]);

    make(`
		<div id='${parentId}' data-on-click='${toggleClass}'>
		<div id='${childId}' data-on-click='${stopPropagation}'></div>
		<div id='subject' class='foo'></div>
		</div>
		`);

    byId(childId).click();

    const subject = byId("subject");

    const l = subject.classList;

    assert.equal(l.length, 1);
    assert.isTrue(l.contains("foo"));
  });

});