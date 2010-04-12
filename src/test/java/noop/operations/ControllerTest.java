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

import noop.model.*;
import org.junit.Before;
import org.junit.Test;

import static noop.model.Edge.EdgeType.CONTAIN;
import static noop.model.Edge.EdgeType.TYPEOF;
import static org.junit.Assert.*;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class ControllerTest {
  private Controller controller;
  private Workspace workspace;

  @Before
  public void setUp() {
    workspace = new Workspace();
    controller = new Controller(workspace);
  }

  @Test public void shouldMakeNewProject() {
    LanguageNode newNode = new Project("helloWorld", "com.google", "");
    controller.apply(new NewNodeOperation(newNode, workspace));
    assertTrue(workspace.nodes.contains(newNode));
    assertEquals(1, workspace.edges.size());
    assertEquals(new Edge(0, CONTAIN, 1), workspace.edges.iterator().next());
  }

  @Test public void shouldCreateAdditionalEdges() {
    LanguageNode stringType = new Clazz("String");
    controller.apply(new NewNodeOperation(stringType, workspace));

    LanguageNode newNode = new StringLiteral("yes");
    controller.apply(new NewNodeOperation(newNode, workspace, TYPEOF, stringType));
    assertEquals(3, workspace.edges.size());
    assertTrue(workspace.edges.contains(new Edge(0, CONTAIN, 1)));
    assertTrue(workspace.edges.contains(new Edge(0, CONTAIN, 2)));
    assertTrue(workspace.edges.contains(new Edge(2, TYPEOF, 1)));
  }

  @Test public void shouldAllowEditingAStringLiteral() {
    StringLiteral aString = new StringLiteral("hello");
    controller.apply(new NewNodeOperation(aString, workspace));

    controller.apply(new EditNodeOperation(1, new StringLiteral("goodbye")));
    assertEquals(2, workspace.nodes.size());
    assertEquals("goodbye", ((StringLiteral) workspace.nodes.get(1)).value);
    assertEquals("hello", ((StringLiteral) workspace.nodes.get(1).getPreviousVersion()).value);
  }

  @Test public void shouldErrorWhenEditingWithWrongType() {
    IntegerLiteral anInt = new IntegerLiteral(12);
    controller.apply(new NewNodeOperation(anInt, workspace));

    try {
      controller.apply(new EditNodeOperation(1, new StringLiteral("String is not Int")));
      fail("should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage(), e.getMessage().contains("IntegerLiteral"));
    }
  }
}
