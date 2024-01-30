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
# clean task
#

ifndef RESOLUTION_DIR
$(error The required variable RESOLUTION_DIR was not defined)
endif

define CLEAN_TASK

ifndef $(1)BASEDIR
$(1)BASEDIR = $$($(1)MODULE)
endif

## work dir
$(1)WORK = $$($(1)BASEDIR)/work

## targets

.PHONY: $(2)clean
$(2)clean:
ifneq ($$($(1)WORK),)
	rm -rf $$($(1)WORK)/* $$($(1)COMPILE_MARKER)
else
	rm -f $$($(1)COMPILE_MARKER)
endif

$$($(1)WORK):
	mkdir --parents $$@
	
endef
