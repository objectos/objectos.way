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
# install task
#

ifndef JAR_FILE
$(error Required java-jar.mk was not included)
endif

ifndef GROUP_ID
$(error The required variable GROUP_ID was not defined)
endif

## install location (jar)
INSTALL := $(LOCAL_REPO_PATH)/$(call mk-dependency,$(GROUP_ID),$(ARTIFACT_ID),$(VERSION),jar)

## install location (resolution file)
INSTALL_RESOLUTION_FILE := $(call mk-resolution-file,$(GROUP_ID)/$(ARTIFACT_ID)/$(VERSION))

## tmp resolution file
INSTALL_RESOLUTION_TMP := $(WORK)/install-resolution-file

## install target reqs
INSTALL_REQS := $(INSTALL)
INSTALL_REQS += $(INSTALL_RESOLUTION_FILE)

#
# install target
#

.PHONY: install
install: $(INSTALL_REQS)

.PHONY: install@clean
install@clean:
	rm -f $(INSTALL) $(INSTALL_RESOLUTION_FILE)

$(INSTALL): $(JAR_FILE)
	@mkdir --parents $(@D)
	cp $< $@

$(INSTALL_RESOLUTION_TMP)::
	echo $(INSTALL) > $@
ifdef COMPILE_RESOLUTION_FILES
	$(call uniq-resolution-files,$(COMPILE_RESOLUTION_FILES)) >> $@
endif

$(INSTALL_RESOLUTION_FILE): $(INSTALL_RESOLUTION_TMP)
	@mkdir --parents $(@D)
	cp $< $@
