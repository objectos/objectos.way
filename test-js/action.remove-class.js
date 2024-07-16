suite("Action::removeClass test", function() {

	setup(function() {
		clearWorkArea();
		this.server = makeServer();
	});

	teardown(function() {
		this.server.restore();
	});

	test("it should remove the class if it is present", function() {
		const clickId = "f";
		const onClick = JSON.stringify([{
			cmd: "remove-class",
			args: ["subject", "remove-me"]
		}]);
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
	});

	test("it should not error if the class is not present", function() {
		const clickId = "f";
		const onClick = JSON.stringify([{
			cmd: "remove-class",
			args: ["subject", "not-present"]
		}]);
		make(`
		<div id='${clickId}' data-on-click='${onClick}'>
		<div id='subject' class='foo present'></div>
		</div>		
		`);

		byId(clickId).click();

		const subject = byId("subject");

		const l = subject.classList;

		assert.equal(l.length, 2);
		assert.isTrue(l.contains("foo"));
		assert.isTrue(l.contains("present"));
	});

	test("it should remove all of the classes", function() {
		const clickId = "f";
		const onClick = JSON.stringify([{
			cmd: "remove-class",
			args: ["subject", "c1", "c2", "c3"]
		}]);
		make(`
		<div id='${clickId}' data-on-click='${onClick}'>
		<div id='subject' class='foo c1 c3'></div>
		</div>		
		`);

		byId(clickId).click();

		const subject = byId("subject");

		const l = subject.classList;

		assert.equal(l.length, 1);
		assert.isTrue(l.contains("foo"));
	});

});