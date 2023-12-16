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
# - you must provide the pom template $$(MODULE)/pom.xml.tmpl

define POM_DEPENDENCY
		<dependency>
			<groupId>$(1)</groupId>
			<artifactId>$(2)</artifactId>
			<version>$(3)</version>
		</dependency>

endef

mk-pom-dep = $(call POM_DEPENDENCY,$(call word-solidus,$(1),1),$(call word-solidus,$(1),2),$(call word-solidus,$(1),3))

define POM_TASK

## pom source
ifndef mk-pom
$$(error The required mk-pom function was not defined)
endif

## pom file
$(1)POM_FILE = $$($(1)WORK)/$$($(1)ARTIFACT_ID)-$$($(1)VERSION).pom

## deps
$(1)pom_gavs=$$($(1)COMPILE_DEPS:$$(RESOLUTION_DIR)/%=%)
$(1)POM_DEPENDENCIES = $$(foreach dep,$$($(1)pom_gavs),$$(call mk-pom-dep,$$(dep)))

## contents
$(1)POM_CONTENTS = $$(call mk-pom,$(1))

#
# Targets
#

.PHONY: $(2)pom
$(2)pom: $$($(1)POM_FILE)

$$($(1)POM_FILE): Makefile
	$$(file > $$@,$$($(1)POM_CONTENTS))

endef
