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

define JAR_TASK

## license 'artifact'
$(1)LICENSE = $$($(1)CLASS_OUTPUT)/META-INF/LICENSE

## jar file path
$(1)JAR_FILE = $$($(1)WORK)/$$($(1)ARTIFACT_ID)-$$($(1)VERSION).jar

## jar command
$(1)JARX = $$(JAR)
$(1)JARX += --create
$(1)JARX += --file $$($(1)JAR_FILE)
$(1)JARX += --module-version $$($(1)VERSION)
$(1)JARX += -C $$($(1)CLASS_OUTPUT)
$(1)JARX += .

## requirements of the $(1)JAR_FILE target
$(1)JAR_FILE_REQS  = $$($(1)COMPILE_MARKER)
$(1)JAR_FILE_REQS += $$($(1)LICENSE)
ifdef $(1)JAR_FILE_REQS_MORE
$(1)JAR_FILE_REQS += $$($(1)JAR_FILE_REQS_MORE)
endif

#
# jar targets
#

.PHONY: $(2)jar
$(2)jar: $$($(1)JAR_FILE)

$$($(1)JAR_FILE): $$($(1)JAR_FILE_REQS)
	$$($(1)JARX)

$$($(1)LICENSE): LICENSE
	mkdir --parents $$(@D)
	cp LICENSE $$(@D)

endef
