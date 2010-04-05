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

import java.io.PrintStream;

import static java.lang.System.identityHashCode;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class DotGraphPrintingVisitor extends ModelVisitor {
  private final PrintStream out;

  public DotGraphPrintingVisitor(PrintStream out) {
    this.out = out;
  }

  @Override
  public void visit(Workspace workspace) {
    out.format("digraph workspace\n{\n");
    out.format("%s [label=\"%s\", shape=house]\n", identityHashCode(workspace), "Workspace");
  }

  @Override
  public void visit(Project project) {
    out.format("%s [label=\"%s (%s)\", shape=box]\n",
        identityHashCode(project), project.getName(), project.getNamespace());
  }

  @Override
  public void visit(Library library) {
    out.format("%s [label=\"%s\", shape=hexagon]\n", identityHashCode(library), library.name);
  }

  @Override
  public void visit(Block block) {
    out.format("%s [label=\"%s {}\"]\n", identityHashCode(block), block.name);
    for (Parameter parameter : block.parameters) {
      out.format("%s -> %s [label=param, style=dotted]\n",
          identityHashCode(block), identityHashCode(parameter));
    }
    if (block.returnType != null) {
      out.format("%s -> %s [label=return, style=dotted]\n",
          identityHashCode(block), identityHashCode(block.returnType));
    }
  }

  @Override
  public void visit(Return aReturn) {
    out.format("%s [label=\"%s\"]\n", identityHashCode(aReturn), "[return]");
    out.format("%s -> %s [label=arg, style=dotted]\n",
          identityHashCode(aReturn), identityHashCode(aReturn.returned));
  }

  @Override
  public void visit(IntegerLiteral integerLiteral) {
    out.format("%s [label=\"%s\"]\n", identityHashCode(integerLiteral), integerLiteral.value);        
  }

  @Override
  public void visit(Parameter parameter) {
    out.format("%s [label=\"%s\"]\n", identityHashCode(parameter), parameter.name);    
  }

  @Override
  public void visit(MethodInvocation methodInvocation) {
    out.format("%s [label=\"%s\"]\n", identityHashCode(methodInvocation), "[invoke]");
    for (Expression argument : methodInvocation.arguments) {
      out.format("%s -> %s [label=arg, style=dotted]\n",
          identityHashCode(methodInvocation), identityHashCode(argument));
    }
  }

  @Override
  public void visit(Copyright copyright) {
    out.format("%s [label=\"%s\", shape=none]\n", identityHashCode(copyright), escape(copyright.value));
  }

  private String escape(String value) {
    return value.replaceAll("\n", "\\\\n");
  }

  @Override
  public void visit(StringLiteral stringLiteral) {
    out.format("%s [label=\"\\\"%s\\\"\"]\n", identityHashCode(stringLiteral), stringLiteral.value);
  }

  @Override
  public void visit(Clazz clazz) {
    out.format("%s [label=\"%s\"]\n", identityHashCode(clazz), clazz.name);
  }

  @Override
  public void leave(Workspace workspace) {
    out.println("}");
  }

  @Override
  public void visit(Edge edge) {
    out.format("%s -> %s ", identityHashCode(edge.src), identityHashCode(edge.dest));
    switch (edge.type) {
      case CONTAIN:
        out.print("\n");
        break;
      default:
        out.println("[label=\"" + edge.type.name().toLowerCase() + "\", style=dashed]");
        break;
    }
  }
}
