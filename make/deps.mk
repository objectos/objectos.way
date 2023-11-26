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
# Dependencies related options & functions
#

## remote repository URL
REMOTE_REPO_URL := https://repo.maven.apache.org/maven2

## remote repository curl
REMOTE_REPO_CURLX := $(CURL)
REMOTE_REPO_CURLX += --create-dirs

## dependency function
## 
## syntax:
## $(call dependency,[GROUP_ID],[ARTIFACT_ID],[VERSION])
colon := :
dot := .
solidus := /

mk-dependency = $(subst $(dot),$(solidus),$(1))/$(2)/$(3)/$(2)-$(3).$(4)

dependency = $(LOCAL_REPO_PATH)/$(subst $(dot),$(solidus),$(1))/$(2)/$(3)/$(2)-$(3).jar

## class-path function
##
## syntax:
## $(call class-path,[list of deps])
ifeq ($(OS),Windows_NT)
CLASS_PATH_SEPARATOR := ;
else
CLASS_PATH_SEPARATOR := :
endif
empty :=
space := $(empty) $(empty)

class-path = $(subst $(space),$(CLASS_PATH_SEPARATOR),$(1))

## module-path function
##
## syntax:
## $(call module-path,[list of deps])
MODULE_PATH_SEPARATOR := :

module-path = $(subst $(space),$(MODULE_PATH_SEPARATOR),$(1))

ifndef RESOLUTION_DIR
$(error The required variable RESOLUTION_DIR was not defined)
endif

## to-resolutions

mk-resolution = $(RESOLUTION_DIR)/$(1)

to-resolutions = $(foreach dep,$(1),$(call mk-resolution,$(dep)))

## to-jars

to-jars-paths = $(foreach res,$(call to-resolutions,$(1)),$(file < $(res)))

to-jars = $(sort $(foreach jar,$(call to-jars-paths,$(1)),$(LOCAL_REPO_PATH)/$(jar)))

#
# Gets the dependency from the remote repository
#

$(LOCAL_REPO_PATH)/%.jar:	
	$(REMOTE_REPO_CURLX) --output $@ $(@:$(LOCAL_REPO_PATH)/%.jar=$(REMOTE_REPO_URL)/%.jar)
