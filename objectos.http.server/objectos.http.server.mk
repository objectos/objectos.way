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

## module coordinates
HTTP_SERVER_GROUP_ID = $(GROUP_ID)
HTTP_SERVER_ARTIFACT_ID = $(HTTP_SERVER)
HTTP_SERVER_VERSION = $(VERSION)

## javac --release option
HTTP_SERVER_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
HTTP_SERVER_ENABLE_PREVIEW = 0

## compile deps
HTTP_SERVER_COMPILE_DEPS  = $(call module-gav,$(HTTP))
HTTP_SERVER_COMPILE_DEPS += $(call module-gav,$(NOTES))
HTTP_SERVER_COMPILE_DEPS += $(call module-gav,$(UTIL_LIST))
HTTP_SERVER_COMPILE_DEPS += $(call module-gav,$(UTIL_MAP))

## test compile deps
HTTP_SERVER_TEST_COMPILE_DEPS  = $(HTTP_SERVER_COMPILE_DEPS)
HTTP_SERVER_TEST_COMPILE_DEPS += $(call module-gav,$(HTTP_SERVER))
HTTP_SERVER_TEST_COMPILE_DEPS += $(call module-gav,$(HTML))
HTTP_SERVER_TEST_COMPILE_DEPS += $(call module-gav,$(NOTES_CONSOLE))
HTTP_SERVER_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
HTTP_SERVER_TEST_RUNTIME_DEPS  = $(HTTP_SERVER_TEST_COMPILE_DEPS)
HTTP_SERVER_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## test runtime modules
HTTP_SERVER_TEST_JAVAX_MODULES = org.testng
HTTP_SERVER_TEST_JAVAX_MODULES += objectos.html
HTTP_SERVER_TEST_JAVAX_MODULES += objectos.notes.console

## way test runtime reads
HTTP_SERVER_TEST_JAVAX_READS = objectos.html
HTTP_SERVER_TEST_JAVAX_READS += objectos.notes.console

## test runtime exports
HTTP_SERVER_TEST_JAVAX_EXPORTS = objectox.http.server

## copyright years for javadoc
HTTP_SERVER_COPYRIGHT_YEARS := 2022-2023

## pom description
HTTP_SERVER_DESCRIPTION = Utility-first styling for Objectos HTML templates

#
# eval tasks
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),HTTP_SERVER_,http.server@)))

#
# objectos.http.server targets
#

.PHONY: http.server@source-jar
http.server@source-jar: $(HTTP_SERVER_SOURCE_JAR_FILE)

.PHONY: http.server@javadoc
http.server@javadoc: $(HTTP_SERVER_JAVADOC_JAR_FILE)

.PHONY: http.server@ossrh-prepare
http.server@ossrh-prepare: $(HTTP_SERVER_OSSRH_PREPARE)
