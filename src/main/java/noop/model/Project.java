/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package noop.model;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Project implements LanguageNode {
  private List<Library> libraries = Lists.newArrayList();

  public List<? extends LanguageNode> getChildren() {
    return libraries;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(libraries).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }
    Project rhs = (Project) obj;
    return new EqualsBuilder()
        .append(libraries, rhs.libraries)
        .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("libraries", libraries).toString();
  }
}