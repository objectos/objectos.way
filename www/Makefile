#
# Copyright (C) 2022-2024 Objectos Software LTDA.
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
# Objectos Way :: WWW
#

## Coordinates (must be set by parent Makefile)
ifndef GROUP_ID
$(error Required GROUP_ID was not defined)
endif
ARTIFACT_ID := objectos.way.www
ifndef VERSION
$(error Required VERSION was not defined)
endif
MODULE := $(ARTIFACT_ID)

## Dependencies
WAY := $(GROUP_ID)/objectos.way/$(VERSION)
ifndef TESTNG
$(error Required TESTNG was not defined)
endif

# Delete the default suffixes
.SUFFIXES:

#
# www
#

.PHONY: all
all: compile

include make/java-core.mk

#
# www@clean
#

include make/common-clean.mk

#
# www@compile
#

## compile deps
COMPILE_DEPS := $(WAY)

## use same JAVA_RELEASE of parent
ifndef JAVA_RELEASE
$(error Required JAVA_RELEASE was not defined)
endif

include make/java-compile.mk

#
# www@test-compile
#

## test compile deps
TEST_COMPILE_DEPS += $(TESTNG)

include make/java-test-compile.mk

#
# www@dev
#

## dev main class
DEV_MAIN := objectos.way.www.StartDev

## dev jvm opts
DEV_JVM_OPTS := -Xmx64m
DEV_JVM_OPTS += -XX:+UseSerialGC
ifeq ($(ENABLE_DEBUG),1)
DEV_JVM_OPTS += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:7000
endif

## dev app args
DEV_APP_ARGS := --class-output $(CLASS_OUTPUT)

include make/java-dev.mk

#
# www@eclipse
#

ECLIPSE_PROJECT_NAME := objectos.way:www

include make/java-eclipse.mk
