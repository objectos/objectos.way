suite("Action::addClass test", function() {

	setup(function() {
		clearWorkArea();
		this.server = makeServer();
	});

	teardown(function() {
		this.server.restore();
	});

	test("it should add the class if it is not present", function() {
		const clickId = "f";
		const onClick = JSON.stringify([{
			cmd: "add-class",
			args: ["subject", "add-me"]
		}]);
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

	test("it should not error if the class is already present", function() {
		const clickId = "f";
		const onClick = JSON.stringify([{
			cmd: "add-class",
			args: ["subject", "present"]
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

	test("it should add all of the classes", function() {
		const clickId = "f";
		const onClick = JSON.stringify([{
			cmd: "add-class",
			args: ["subject", "add-me", "present", "another-add"]
		}]);
		make(`
		<div id='${clickId}' data-on-click='${onClick}'>
		<div id='subject' class='foo present'></div>
		</div>		
		`);

		byId(clickId).click();

		const subject = byId("subject");

		const l = subject.classList;

		assert.equal(l.length, 4);
		assert.isTrue(l.contains("foo"));
		assert.isTrue(l.contains("add-me"));
		assert.isTrue(l.contains("another-add"));
		assert.isTrue(l.contains("present"));
	});

});