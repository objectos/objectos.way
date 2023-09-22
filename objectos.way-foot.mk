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
# Targets
#

.PHONY: clean
clean: code@clean way@clean

.PHONY: test
test:

# maybe use eval for module@target targets?

.PHONY: code@clean
code@clean:
	rm -rf $(CODE_WORK)/*

.PHONY: way@clean
way@clean:
	rm -rf $(WAY_WORK)/*

.PHONY: code@compile
code@compile: $(CODE_COMPILE_MARKER)

.PHONY: code@jar
code@jar: $(CODE_JAR_FILE)

.PHONY: way@compile
way@compile: $(WAY_COMPILE_MARKER)
