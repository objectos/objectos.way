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
# Provides the pom target:
#
# - generates a pom.xml suitable for deploying to a maven repository
# 
# Requirements:
#
# - you must provide a mk-pom function

ifndef mk-pom
$(error The required mk-pom function was not defined)
endif

ifndef ARTIFACT_ID
$(error The required variable ARTIFACT_ID was not defined)
endif

ifndef VERSION
$(error The required variable VERSION was not defined)
endif

ifndef WORK
$(error Required common-clean.mk was not included)
endif

define POM_DEPENDENCY
		<dependency>
			<groupId>$(1)</groupId>
			<artifactId>$(2)</artifactId>
			<version>$(3)</version>
		</dependency>

endef

mk-pom-dep = $(call POM_DEPENDENCY,$(call word-solidus,$(1),1),$(call word-solidus,$(1),2),$(call word-solidus,$(1),3))

## pom file name
POM_FILE_NAME := $(ARTIFACT_ID)-$(VERSION).pom

## pom file
POM_FILE := $(WORK)/$(POM_FILE_NAME)

## deps
pom_gavs = $(COMPILE_DEPS:$(RESOLUTION_DIR)/%=%)
POM_DEPENDENCIES = $(foreach dep,$(pom_gavs),$(call mk-pom-dep,$(dep)))

## contents
POM_CONTENTS = $(call mk-pom)

#
# Targets
#

.PHONY: pom
pom: $(POM_FILE)

$(POM_FILE): Makefile
	$(file > $@,$(POM_CONTENTS))
