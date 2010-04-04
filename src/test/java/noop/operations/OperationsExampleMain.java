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
import noop.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Lists.newArrayList;
import static noop.model.Edge.EdgeType.CONTAIN;
import static noop.model.Edge.EdgeType.INVOKE;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class OperationsExampleMain {

  public static void main(String[] args) throws FileNotFoundException {
    Workspace workspace = new Workspace();
    Controller controller = new Controller(workspace);
    Block block = createNoopStdLib(workspace, controller);
    createHelloWorldProgram(workspace, controller, block);
    workspace.accept(new DotGraphPrintingVisitor(new PrintStream(new FileOutputStream(
        new File("/Users/alexeagle/Documents/noop.dot")))));
    
  }

  private static Block createNoopStdLib(Workspace workspace, Controller controller) {
    Project project = new Project("Noop", "com.google.noop");
    Library library = new Library("io");
    Clazz console = new Clazz("Console");
    Block print = new Block("print");

    controller.applyAll(newArrayList(
        new NewNodeOperation(project, workspace),
        new NewNodeOperation(library, project),
        new NewNodeOperation(console, library),
        new NewNodeOperation(print, console)));
    return print;
  }

  private static void createHelloWorldProgram(Workspace workspace, Controller controller, Block print) {
    Project project = new Project("Hello World", "com.example");
    Library library = new Library("main");
    Block sayHello = new Block("say hello");
    Expression printHello = new MethodInvocation(new StringLiteral("Hello, World!"));

    controller.applyAll(newArrayList(
        new NewNodeOperation(project, workspace),
        new NewNodeOperation(library, project),
        new NewNodeOperation(sayHello, library),
        new NewNodeOperation(printHello, newArrayList(
            new Edge(sayHello, CONTAIN, printHello),
            new Edge(printHello, INVOKE, print)))));
  }

}
