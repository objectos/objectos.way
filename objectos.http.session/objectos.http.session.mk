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
# objectos.http.session
#

## module directory
HTTP_SESSION = objectos.http.session

## module
HTTP_SESSION_MODULE = $(HTTP_SESSION)

## module coordinates
HTTP_SESSION_GROUP_ID = $(GROUP_ID)
HTTP_SESSION_ARTIFACT_ID = $(HTTP_SESSION)
HTTP_SESSION_VERSION = $(VERSION)

## javac --release option
HTTP_SESSION_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
HTTP_SESSION_ENABLE_PREVIEW = 0

## compile deps
HTTP_SESSION_COMPILE_DEPS  = $(call module-gav,$(HTTP))
HTTP_SESSION_COMPILE_DEPS += $(call module-gav,$(NOTES))

## test compile deps
HTTP_SESSION_TEST_COMPILE_DEPS  = $(HTTP_SESSION_COMPILE_DEPS)
HTTP_SESSION_TEST_COMPILE_DEPS += $(call module-gav,$(HTTP_SESSION))
HTTP_SESSION_TEST_COMPILE_DEPS += $(call module-gav,$(NOTES_CONSOLE))
HTTP_SESSION_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
HTTP_SESSION_TEST_RUNTIME_DEPS  = $(HTTP_SESSION_TEST_COMPILE_DEPS)
HTTP_SESSION_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## test runtime modules
HTTP_SESSION_TEST_JAVAX_MODULES  = org.testng
HTTP_SESSION_TEST_JAVAX_MODULES += objectos.notes.console

## way test runtime reads
HTTP_SESSION_TEST_JAVAX_READS  = objectos.notes.console

## copyright years for javadoc
HTTP_SESSION_COPYRIGHT_YEARS := 2023

## pom description
HTTP_SESSION_DESCRIPTION = Objectos HTTP session support

#
# eval tasks
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),HTTP_SESSION_,http.session@)))

#
# objectos.http.session targets
#

.PHONY: http.session@source-jar
http.session@source-jar: $(HTTP_SESSION_SOURCE_JAR_FILE)

.PHONY: http.session@javadoc
http.session@javadoc: $(HTTP_SESSION_JAVADOC_JAR_FILE)

.PHONY: http.session@ossrh-prepare
http.session@ossrh-prepare: $(HTTP_SESSION_OSSRH_PREPARE)
