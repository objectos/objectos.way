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
# - you must provide the pom template $(MODULE)/pom.xml.tmpl

## pom source
POM_SOURCE = $(MODULE)/pom.xml.tmpl

## pom file
POM_ARTIFACT = $(WORK)/pom.xml

## pom external variables
# POM_VARIABLES = 

## ossrh pom sed command
POM_SEDX = $(SED)
POM_SEDX += $(foreach var,$(POM_VARIABLES),--expression='s/%%$(var)%%/$($(var))/g')
POM_SEDX += --expression='s/%%ARTIFACT_ID%%/$(ARTIFACT_ID)/g'
POM_SEDX += --expression='s/%%GROUP_ID%%/$(GROUP_ID)/g'
POM_SEDX += --expression='s/%%VERSION%%/$(VERSION)/g'
POM_SEDX += --expression='w $(POM_ARTIFACT)'
POM_SEDX += $(POM_SOURCE)

#
# Targets
#

.PHONY: pom
pom: $(POM_ARTIFACT)

$(POM_ARTIFACT): $(POM_SOURCE) Makefile
	$(POM_SEDX)
