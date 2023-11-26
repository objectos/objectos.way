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
# source-jar task
#

define SOURCE_JAR_TASK

## source-jar file
$(1)SOURCE_JAR_FILE = $$($(1)WORK)/$$($(1)JAR_NAME)-$$($(1)VERSION)-sources.jar

## source-jar command
$(1)SOURCE_JARX = $$(JAR)
$(1)SOURCE_JARX += --create
$(1)SOURCE_JARX += --file $$($(1)SOURCE_JAR_FILE)
$(1)SOURCE_JARX += -C $$($(1)MAIN)
$(1)SOURCE_JARX += .

#
# source-jar targets
#

$$($(1)SOURCE_JAR_FILE): $$($(1)SOURCES)
	$$($(1)SOURCE_JARX)
	
endef
