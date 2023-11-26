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
# install task
#

define INSTALL_TASK

## install location
$(1)INSTALL = $$(call dependency,$$($(1)GROUP_ID),$$($(1)ARTIFACT_ID),$$($(1)VERSION))

#
# install target
#

.PHONY: $(2)install
$(2)install: $$($(1)INSTALL)

.PHONY: $(2)clean-install
$(2)clean-install:
	rm -f $$($(1)INSTALL)

$$($(1)INSTALL): $$($(1)JAR_FILE)
	@mkdir --parents $$(@D)
	cp $$< $$@
	
endef
