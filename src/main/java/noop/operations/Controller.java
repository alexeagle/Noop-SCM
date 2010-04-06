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

import noop.model.Edge;
import noop.model.Workspace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Controller {
  private Workspace workspace;

  public Controller(Workspace workspace) {
    this.workspace = workspace;
  }

  public void apply(NewNodeOperation operation) {
    workspace.nodes.add(operation.newNode);

    for (Edge edge : operation.edges) {
      if (!workspace.nodes.contains(edge.src)) {
        throw new IllegalStateException(String.format("Cannot add edge [%s -> %s] due to non-existant src",
            edge.src, edge.dest));
      }
      if (!workspace.nodes.contains(edge.dest)) {
        throw new IllegalStateException(String.format("Cannot add edge [%s -> %s] due to non-existant dest",
            edge.src, edge.dest));
      }
      workspace.edges.add(edge);
    }
  }

  public void applyAll(NewNodeOperation... operations) {
    for (NewNodeOperation operation : operations) {
      apply(operation);
    }
  }
}
