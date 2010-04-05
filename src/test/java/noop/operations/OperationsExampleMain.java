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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static com.google.common.collect.Lists.newArrayList;
import static noop.model.Edge.EdgeType.*;
import static noop.model.Edge.EdgeType.INVOKE;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class OperationsExampleMain {
  private static Clazz stringClazz;
  private static Clazz consoleClazz;
  private static Clazz intClazz;
  private static Block printMethod;
  private static Parameter printArg;

  public static void main(String[] args) throws FileNotFoundException {
    Workspace workspace = new Workspace();
    Controller controller = new Controller(workspace);
    createNoopStdLib(workspace, controller);
    createHelloWorldProgram(workspace, controller);
    workspace.accept(new DotGraphPrintingVisitor(new PrintStream(new FileOutputStream(
        new File("/Users/alexeagle/Documents/noop.dot")))));
  }

  private static void createNoopStdLib(Workspace workspace, Controller controller) {
    Project project = new Project("Noop", "com.google.noop");
    Library lang = new Library("lang");
    stringClazz = new Clazz("String");
    Library io = new Library("io");
    consoleClazz = new Clazz("Console");
    printMethod = new Block("print", null);
    printArg = new Parameter("s");
    intClazz = new Clazz("Integer");

    controller.applyAll(newArrayList(
        new NewNodeOperation(project, workspace),
        new NewNodeOperation(lang, project),
        new NewNodeOperation(io, project),
        new NewNodeOperation(consoleClazz, io),
        new NewNodeOperation(stringClazz, lang),
        new NewNodeOperation(intClazz, lang),
        new NewNodeOperation(printMethod, consoleClazz),
        new NewNodeOperation(printArg,
            new Edge(printMethod, CONTAIN, printArg),
            new Edge(printArg, TYPEOF, stringClazz))));
  }

  private static void createHelloWorldProgram(Workspace workspace, Controller controller) {
    Project project = new Project("Hello World", "com.example");
    Copyright copyright = new Copyright("Copyright 2010\nExample Co.");
    Library library = new Library("main");
    Parameter consoleDep = new Parameter("console");
    Block sayHello = new Block("say hello", intClazz, consoleDep);
    StringLiteral helloWorld = new StringLiteral("Hello, World!");
    Expression printHello = new MethodInvocation(helloWorld);
    IntegerLiteral zero = new IntegerLiteral(0);
    Expression returnZero = new Return(zero);

    controller.applyAll(newArrayList(
        new NewNodeOperation(project, workspace),
        new NewNodeOperation(copyright, project),
        new NewNodeOperation(library, project),
        new NewNodeOperation(sayHello, library),
        new NewNodeOperation(consoleDep,
            new Edge(consoleDep, TYPEOF, consoleClazz)),
        new NewNodeOperation(printHello,
            new Edge(sayHello, CONTAIN, printHello),
            new Edge(printHello, TARGET, consoleDep),
            new Edge(printHello, INVOKE, printMethod),
            new Edge(helloWorld, TYPEOF, stringClazz)),
        new NewNodeOperation(returnZero,
            new Edge(sayHello, CONTAIN, returnZero),
            new Edge(zero, TYPEOF, intClazz))));
  }

}
