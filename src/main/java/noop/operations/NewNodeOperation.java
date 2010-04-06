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

package noop.operations;

import com.google.common.collect.Lists;
import noop.model.Edge;
import noop.model.LanguageNode;

import java.util.Arrays;
import java.util.List;

import static noop.model.Edge.EdgeType.CONTAIN;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class NewNodeOperation implements MutationOperation {
  public final LanguageNode newNode;
  public final List<Edge> edges;

  public NewNodeOperation(LanguageNode newNode, LanguageNode container, Edge... edges) {
    this.newNode = newNode;
    this.edges = Arrays.asList(edges);
  }

  public NewNodeOperation(LanguageNode newNode, LanguageNode container) {
    this.newNode = newNode;
    this.edges = Lists.newArrayList(new Edge(container, CONTAIN, newNode));
  }
}
