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
# objectos.http.server options
#

## module directory
HTTP_SERVER = objectos.http.server

## module
HTTP_SERVER_MODULE = $(HTTP_SERVER)

## module version
HTTP_SERVER_VERSION = $(VERSION)

## javac --release option
HTTP_SERVER_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
HTTP_SERVER_ENABLE_PREVIEW = 0

## compile deps
HTTP_SERVER_COMPILE_DEPS = $(LANG_OBJECT_JAR_FILE)
HTTP_SERVER_COMPILE_DEPS += $(NOTES_JAR_FILE)
HTTP_SERVER_COMPILE_DEPS += $(UTIL_ARRAY_JAR_FILE)
HTTP_SERVER_COMPILE_DEPS += $(UTIL_COLLECTION_JAR_FILE)
HTTP_SERVER_COMPILE_DEPS += $(UTIL_LIST_JAR_FILE)
HTTP_SERVER_COMPILE_DEPS += $(UTIL_MAP_JAR_FILE)
HTTP_SERVER_COMPILE_DEPS += $(HTTP_JAR_FILE)

## jar name
HTTP_SERVER_JAR_NAME = $(HTTP_SERVER)

## test compile deps
HTTP_SERVER_TEST_COMPILE_DEPS = $(HTTP_SERVER_COMPILE_DEPS)
HTTP_SERVER_TEST_COMPILE_DEPS += $(HTML_TMPL_JAR_FILE)
HTTP_SERVER_TEST_COMPILE_DEPS += $(HTML_JAR_FILE)
HTTP_SERVER_TEST_COMPILE_DEPS += $(NOTES_BASE_JAR_FILE)
HTTP_SERVER_TEST_COMPILE_DEPS += $(NOTES_CONSOLE_JAR_FILE)
HTTP_SERVER_TEST_COMPILE_DEPS += $(HTTP_SERVER_JAR_FILE)
HTTP_SERVER_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
HTTP_SERVER_TEST_RUNTIME_DEPS = $(HTTP_SERVER_TEST_COMPILE_DEPS)
HTTP_SERVER_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
HTTP_SERVER_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
HTTP_SERVER_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## test runtime modules
HTTP_SERVER_TEST_JAVAX_MODULES = org.testng
HTTP_SERVER_TEST_JAVAX_MODULES += objectos.html
HTTP_SERVER_TEST_JAVAX_MODULES += objectos.notes.console

## way test runtime reads
HTTP_SERVER_TEST_JAVAX_READS = objectos.html
HTTP_SERVER_TEST_JAVAX_READS += objectos.notes.console

## test runtime exports
HTTP_SERVER_TEST_JAVAX_EXPORTS = objectox.http.server

## install coordinates
HTTP_SERVER_GROUP_ID = $(GROUP_ID)
HTTP_SERVER_ARTIFACT_ID = $(HTTP_SERVER_MODULE)

## copyright years for javadoc
HTTP_SERVER_COPYRIGHT_YEARS := 2022-2023

## pom description
HTTP_SERVER_DESCRIPTION = Utility-first styling for Objectos HTML templates

#
# eval tasks
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),HTTP_SERVER_,http.server@)))

#
# objectos.http.server selfgen
#

## html selfgen java command
HTTP_SERVER_SELFGEN_JAVAX = $(JAVA)
HTTP_SERVER_SELFGEN_JAVAX += --module-path $(call module-path,$(SELFGEN_RUNTIME_DEPS))
ifeq ($(SELFGEN_ENABLE_PREVIEW), 1)
HTTP_SERVER_SELFGEN_JAVAX += --enable-preview
endif
HTTP_SERVER_SELFGEN_JAVAX += --module $(SELFGEN_MODULE)/$(SELFGEN_MODULE).CssUtilSpec
HTTP_SERVER_SELFGEN_JAVAX += $(HTTP_SERVER_MAIN)

$(HTTP_SERVER_SELFGEN_MARKER): $(SELFGEN_JAR_FILE)
	$(HTTP_SERVER_SELFGEN_JAVAX)
	mkdir --parents $(@D)
	touch $@

#
# objectos.http.server targets
#

.PHONY: http.server@compile
http.server@compile: $(HTTP_SERVER_SELFGEN_MARKER) $(HTTP_SERVER_COMPILE_MARKER)

.PHONY: http.server@jar
http.server@jar: $(HTTP_SERVER_JAR_FILE)

.PHONY: http.server@test
http.server@test: $(HTTP_SERVER_TEST_RUN_MARKER)

.PHONY: http.server@install
http.server@install: $(HTTP_SERVER_INSTALL)

.PHONY: http.server@source-jar
http.server@source-jar: $(HTTP_SERVER_SOURCE_JAR_FILE)

.PHONY: http.server@javadoc
http.server@javadoc: $(HTTP_SERVER_JAVADOC_JAR_FILE)

.PHONY: http.server@pom
http.server@pom: $(HTTP_SERVER_POM_FILE)

.PHONY: http.server@ossrh-prepare
http.server@ossrh-prepare: $(HTTP_SERVER_OSSRH_PREPARE)
