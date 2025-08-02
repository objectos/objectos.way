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
# jar options
#

ifndef COMPILE_MARKER
$(error Required java-compile.mk was not included)
endif

ifndef ARTIFACT_ID
$(error The required variable ARTIFACT_ID was not defined)
endif

ifndef VERSION
$(error The required variable VERSION was not defined)
endif

## META-INF directory
JAR_META_INF := $(CLASS_OUTPUT)/META-INF

## license 'artifact'
JAR_LICENSE := $(JAR_META_INF)/LICENSE

## jar file name
JAR_FILE_NAME := $(ARTIFACT_ID)-$(VERSION).jar

## jar file path
JAR_FILE := $(WORK)/$(JAR_FILE_NAME)

ifdef JAR_EXCLUDES
## where we'll find the classes for our JAR file
JAR_DIRECTORY := $(WORK)/jar

## jar rsync command
JAR_RSYNCX := rsync
JAR_RSYNCX += -a
JAR_RSYNCX += $(foreach exclude,$(JAR_EXCLUDES),--exclude=$(exclude))
JAR_RSYNCX += $(CLASS_OUTPUT)/
JAR_RSYNCX += $(JAR_DIRECTORY)/
endif

## jar command
JARX := $(JAR)
JARX += --create
JARX += --file $(JAR_FILE)
ifneq ($(JAR_MODE),class-path)
JARX += --module-version $(VERSION)
endif
ifndef JAR_EXCLUDES
JARX += -C $(CLASS_OUTPUT)
else
JARX += -C $(JAR_DIRECTORY)
endif
JARX += .

## requirements of the JAR_FILE target
JAR_FILE_REQS := $(COMPILE_MARKER)
JAR_FILE_REQS += $(JAR_LICENSE)
ifdef JAR_EXCLUDES
JAR_FILE_REQS += $(JAR_DIRECTORY)
endif

#
# jar targets
#

.PHONY: jar
jar: $(JAR_FILE)

.PHONY: jar-clean
ifndef JAR_EXCLUDES
jar-clean:
	rm -rf $(JAR_FILE)
else
jar-clean:
	rm -rf $(JAR_DIRECTORY) $(JAR_FILE)

$(JAR_DIRECTORY): $(CLASSES)
	@mkdir --parents $@
	$(JAR_RSYNCX)
endif

$(JAR_FILE): $(JAR_FILE_REQS)
	$(JARX)

$(JAR_META_INF):
	mkdir --parents $@

$(JAR_LICENSE): LICENSE | $(JAR_META_INF)
	cp LICENSE $(@D)