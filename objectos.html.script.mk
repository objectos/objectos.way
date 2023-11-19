#
# Copyright (C) 2023 Objectos Software LTDA.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# objectos.html.script options
#

## module directory
HTML_SCRIPT = objectos.html.script

## module
HTML_SCRIPT_MODULE = $(HTML_SCRIPT)

## module version
HTML_SCRIPT_VERSION = $(VERSION)

## javac --release option
HTML_SCRIPT_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
HTML_SCRIPT_ENABLE_PREVIEW = 0

## compile deps
HTML_SCRIPT_COMPILE_DEPS  = $(call module-gav,$(HTML))
HTML_SCRIPT_COMPILE_DEPS += $(call module-gav,$(UTIL_MAP))

## jar name
HTML_SCRIPT_JAR_NAME = $(HTML_SCRIPT)

## JS source
HTML_SCRIPT_JS_SRC = $(HTML_SCRIPT)/js/objectos.way.js

## JS artifact
HTML_SCRIPT_JS_ARTIFACT = $(HTML_SCRIPT_CLASS_OUTPUT)/objectos/html/script/objectos.way.js

## jar file reqs
HTML_SCRIPT_JAR_FILE_REQS_MORE = $(HTML_SCRIPT_JS_ARTIFACT)

## install coordinates
HTML_SCRIPT_GROUP_ID = $(GROUP_ID)
HTML_SCRIPT_ARTIFACT_ID = $(HTML_SCRIPT_MODULE)

## copyright years for javadoc
HTML_SCRIPT_COPYRIGHT_YEARS := 2022-2023

## pom description
HTML_SCRIPT_DESCRIPTION = Defines the types of the Objectos HTML domain specific language

#
# eval tasks
#

HTML_SCRIPT_TASKS = $(filter-out $(TEST_TASKS), $(MODULE_TASKS))

$(foreach task,$(HTML_SCRIPT_TASKS),$(eval $(call $(task),HTML_SCRIPT_,html.script@)))

#
# JS targets
#

$(HTML_SCRIPT_JS_ARTIFACT): $(HTML_SCRIPT_JS_SRC)
	mkdir --parents $(@D)
	cp $< $@

#
# objectos.html.script targets
#

.PHONY: html.script@jar
html.script@jar: $(HTML_SCRIPT_JAR_FILE)

.PHONY: html.script@test
html.script@test:

.PHONY: html.script@install
html.script@install: $(HTML_SCRIPT_INSTALL)

.PHONY: html.script@source-jar
html.script@source-jar: $(HTML_SCRIPT_SOURCE_JAR_FILE)

.PHONY: html.script@javadoc
html.script@javadoc: $(HTML_SCRIPT_JAVADOC_JAR_FILE)

.PHONY: html.script@pom
html.script@pom: $(HTML_SCRIPT_POM_FILE)

.PHONY: html.script@ossrh-prepare
html.script@ossrh-prepare: $(HTML_SCRIPT_OSSRH_PREPARE)
