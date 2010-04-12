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
import noop.model.Edge.EdgeType;
import noop.model.LanguageNode;
import noop.model.Workspace;

import java.util.Map.Entry;

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
    int newNodeId = workspace.nodes.size() - 1;
    addEdge(newNodeId, operation.container, EdgeType.CONTAIN, true);
    for (Entry<EdgeType, LanguageNode> edgeTypeLanguageNodeEntry : operation.edges.entries()) {
      LanguageNode destNode = edgeTypeLanguageNodeEntry.getValue();
      EdgeType edgeType = edgeTypeLanguageNodeEntry.getKey();
      addEdge(newNodeId, destNode, edgeType, false);
    }
  }

  private void addEdge(int newNodeId, LanguageNode destNode, EdgeType edgeType, boolean backwards) {
    int destId = workspace.nodes.indexOf(destNode);
    if (destId < 0) {
      throw new IllegalStateException(String.format("Cannot add edge [%s -> %s] due to non-existant dest",
          newNodeId, destId));
    }
    Edge newEdge = backwards ? new Edge(destId, edgeType, newNodeId) : new Edge(newNodeId, edgeType, destId);
    workspace.edges.add(newEdge);
  }

  public void applyAll(NewNodeOperation... operations) {
    for (NewNodeOperation operation : operations) {
      apply(operation);
    }
  }

  public void apply(EditNodeOperation operation) {
    LanguageNode currentValue = workspace.nodes.get(operation.id);
    if (currentValue.getClass() != operation.newValue.getClass()) {
      throw new IllegalArgumentException(String.format("Cannot edit node %d with %s because the current type is %s",
          operation.id, operation.newValue, currentValue.getClass()));
    }
    
    operation.newValue.setPreviousVersion(currentValue);
    workspace.nodes.set(operation.id, operation.newValue);
  }
}
