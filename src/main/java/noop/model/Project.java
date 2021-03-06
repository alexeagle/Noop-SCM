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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Project extends LanguageNode<Project> {
  private final String name;
  private final String namespace;
  private final String copyright;

  public String getCopyright() {
    return copyright;
  }
  
  public String getName() {
    return name;
  }

  public String getNamespace() {
    return namespace;
  }

  public Project(String name, String namespace, String copyright) {
    this.name = name;
    this.namespace = namespace;
    this.copyright = copyright;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(name)
        .append(namespace)
        .toHashCode();
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
        .append(name, rhs.name)
        .append(namespace, rhs.namespace)
        .isEquals();
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
  }
}
