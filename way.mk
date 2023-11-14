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

@generated-msg@

#
# Objectos Way
#

GROUP_ID := br.com.objectos
ARTIFACT_ID := objectos.way
VERSION := 0.2.0-SNAPSHOT

## Deps versions

TESTNG_VERSION := 7.7.1
JCOMMANDER_VERSION := 1.82
SLF4J_VERSION := 1.7.36

JAVA_RELEASE := 21

# Delete the default suffixes
.SUFFIXES:

## common module tasks
MODULE_TASKS = COMPILE_TASK
MODULE_TASKS += JAR_TASK
MODULE_TASKS += TEST_COMPILE_TASK
MODULE_TASKS += TEST_RUN_TASK
MODULE_TASKS += INSTALL_TASK
MODULE_TASKS += SOURCE_JAR_TASK
MODULE_TASKS += JAVADOC_TASK
MODULE_TASKS += POM_TASK
MODULE_TASKS += OSSRH_PREPARE_TASK

## test-related tasks
TEST_TASKS = TEST_COMPILE_TASK
TEST_TASKS += TEST_RUN_TASK

#
# Default target
#

.PHONY: all
all: jar

.PHONY: jar
jar: way@jar

print-%::
	@echo $* = $($*)
