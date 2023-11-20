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
# objectos.html options
#

## module directory
HTML = objectos.html

## module
HTML_MODULE = $(HTML)

## module version
HTML_VERSION = $(VERSION)

## javac --release option
HTML_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
HTML_ENABLE_PREVIEW = 0

## compile deps
HTML_COMPILE_DEPS  = $(call module-gav,$(HTML_TMPL))
HTML_COMPILE_DEPS += $(call module-gav,$(UTIL_MAP))

## marker to indicate when selfgen was last run
HTML_SELFGEN_MARKER = $(HTML)/work/selfgen-marker

## compilation depends on selfgen
HTML_COMPILE_REQS_MORE = $(HTML_SELFGEN_MARKER)

## make selfgen a req for html compilation
HTML_RESOURCES = $(HTML_SELFGEN_MARKER)

## jar name
HTML_JAR_NAME = $(HTML)

## test compile deps
HTML_TEST_COMPILE_DEPS = $(HTML_COMPILE_DEPS)
HTML_TEST_COMPILE_DEPS += $(HTML_JAR_FILE)
HTML_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
HTML_TEST_RUNTIME_DEPS = $(HTML_TEST_COMPILE_DEPS)
HTML_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
HTML_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
HTML_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## test runtime exports
HTML_TEST_JAVAX_EXPORTS = objectos.html.internal

## install coordinates
HTML_GROUP_ID = $(GROUP_ID)
HTML_ARTIFACT_ID = $(HTML_MODULE)

## copyright years for javadoc
HTML_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
HTML_JAVADOC_SNIPPET_PATH := HTML_TEST

## pom description
HTML_DESCRIPTION = Generate HTML using pure Java

#
# eval tasks
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),HTML_,html@)))

#
# objectos.html selfgen
#

## html selfgen java command
HTML_SELFGEN_JAVAX = $(JAVA)
HTML_SELFGEN_JAVAX += --module-path $(call module-path,$(SELFGEN_RUNTIME_JARS))
ifeq ($(SELFGEN_ENABLE_PREVIEW), 1)
HTML_SELFGEN_JAVAX += --enable-preview
endif
HTML_SELFGEN_JAVAX += --module $(SELFGEN_MODULE)/$(SELFGEN_MODULE).HtmlSpec
HTML_SELFGEN_JAVAX += $(HTML_MAIN)
HTML_SELFGEN_JAVAX += html

$(HTML_SELFGEN_MARKER): $(SELFGEN_JAR_FILE)
	$(HTML_SELFGEN_JAVAX)
	mkdir --parents $(@D)
	touch $@

#
# objectos.html targets
#

.PHONY: html@jar
html@jar: $(HTML_JAR_FILE)

.PHONY: html@test
html@test: $(HTML_TEST_RUN_MARKER)

.PHONY: html@install
html@install: $(HTML_INSTALL)

.PHONY: html@source-jar
html@source-jar: $(HTML_SOURCE_JAR_FILE)

.PHONY: html@javadoc
html@javadoc: $(HTML_JAVADOC_JAR_FILE)

.PHONY: html@pom
html@pom: $(HTML_POM_FILE)

.PHONY: html@ossrh-prepare
html@ossrh-prepare: $(HTML_OSSRH_PREPARE)
