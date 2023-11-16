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
# objectos.selfgen options
#

## selfgen directory
SELFGEN := objectos.selfgen

## selfgen module
SELFGEN_MODULE := $(SELFGEN)

## selfgen module version
SELFGEN_VERSION := $(VERSION)

## selfgen javac --release option
SELFGEN_JAVA_RELEASE := $(JAVA_RELEASE)

## selfgen --enable-preview ?
SELFGEN_ENABLE_PREVIEW := 1

## selfgen compile deps
SELFGEN_COMPILE_DEPS = $(CODE_JAR_FILE) 

## selfgen jar name
SELFGEN_JAR_NAME := $(SELFGEN)

## selfgen test compile deps
SELFGEN_TEST_COMPILE_DEPS = $(CODE_JAR_FILE)
SELFGEN_TEST_COMPILE_DEPS += $(SELFGEN_JAR_FILE)
SELFGEN_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## selfgen test runtime dependencies
SELFGEN_TEST_RUNTIME_DEPS = $(SELFGEN_TEST_COMPILE_DEPS)
SELFGEN_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
SELFGEN_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
SELFGEN_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## seflgen test runtime exports
SELFGEN_TEST_JAVAX_EXPORTS := objectos.selfgen.css
SELFGEN_TEST_JAVAX_EXPORTS += objectos.selfgen.html
SELFGEN_TEST_JAVAX_EXPORTS += selfgen.css.util

#
# objectos.selfgen targets
#

SELFGEN_TASKS = COMPILE_TASK
SELFGEN_TASKS += JAR_TASK
SELFGEN_TASKS += TEST_COMPILE_TASK
SELFGEN_TASKS += TEST_RUN_TASK

$(foreach task,$(SELFGEN_TASKS),$(eval $(call $(task),SELFGEN_)))

.PHONY: selfgen@clean
selfgen@clean:
	rm -rf $(SELFGEN_WORK)/*

.PHONY: selfgen@compile
selfgen@compile: $(SELFGEN_COMPILE_MARKER)

.PHONY: selfgen@jar
selfgen@jar: $(SELFGEN_JAR_FILE)

.PHONY: selfgen@test
selfgen@test: $(SELFGEN_TEST_RUN_MARKER)

## selfgen runtime deps
SELFGEN_RUNTIME_DEPS = $(SELFGEN_JAR_FILE)
SELFGEN_RUNTIME_DEPS += $(SELFGEN_COMPILE_DEPS)
