#
# Copyright (C) 2023-2024 Objectos Software LTDA.
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
# source-jar options
#

ifndef JAR
$(error Required java-core.mk was not included)
endif

ifndef COMPILE_MARKER
$(error Required java-compile.mk was not included)
endif

ifndef ARTIFACT_ID
$(error The required variable ARTIFACT_ID was not defined)
endif

ifndef VERSION
$(error The required variable VERSION was not defined)
endif

## source-jar file name
SOURCE_JAR_FILE_NAME := $(ARTIFACT_ID)-$(VERSION)-sources.jar

## source-jar file
SOURCE_JAR_FILE := $(WORK)/$(SOURCE_JAR_FILE_NAME)

## source-jar command
SOURCE_JARX := $(JAR)
SOURCE_JARX += --create
SOURCE_JARX += --file $(SOURCE_JAR_FILE)
SOURCE_JARX += -C $(MAIN)
SOURCE_JARX += .

## requirements of the SOURCE_JAR_FILE target
SOURCE_JAR_FILE_REQS := | $(WORK)

#
# source-jar targets
#

.PHONY: source-jar
source-jar: $(SOURCE_JAR_FILE)

$(SOURCE_JAR_FILE): $(SOURCE_JAR_FILE_REQS)
	$(SOURCE_JARX)
	