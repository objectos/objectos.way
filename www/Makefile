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

## use same JAVA_RELEASE of parent
ifndef JAVA_RELEASE
$(error Required JAVA_RELEASE was not defined)
endif

include make/java-compile.mk
