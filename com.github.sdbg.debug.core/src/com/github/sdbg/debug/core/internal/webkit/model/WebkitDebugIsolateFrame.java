/*
 * Copyright (c) 2013, the Dart project authors.
 * 
 * Licensed under the Eclipse Public License v1.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.sdbg.debug.core.internal.webkit.model;

import java.util.Collections;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;

import com.github.sdbg.debug.core.internal.webkit.protocol.WebkitCallFrame;
import com.github.sdbg.debug.core.internal.webkit.protocol.WebkitRemoteObject;

/**
 * A pseudo stack frame for the current isolate. This frame will be able to enumerate all the
 * libraries in the current isolate, and each library's top-level variables.
 */
public class WebkitDebugIsolateFrame extends WebkitDebugElement implements IStackFrame {
  private WebkitDebugThread thread;

  public WebkitDebugIsolateFrame(WebkitDebugThread thread) {
    super(thread.getTarget());

    this.thread = thread;
  }

  @Override
  public boolean canResume() {
    return thread.canResume();
  }

  @Override
  public boolean canStepInto() {
    return thread.canStepInto();
  }

  @Override
  public boolean canStepOver() {
    return thread.canStepOver();
  }

  @Override
  public boolean canStepReturn() {
    return thread.canStepReturn();
  }

  @Override
  public boolean canSuspend() {
    return thread.canSuspend();
  }

  @Override
  public boolean canTerminate() {
    return thread.canTerminate();
  }

  @Override
  public int getCharEnd() throws DebugException {
    // not called
    return 0;
  }

  @Override
  public int getCharStart() throws DebugException {
    // not called
    return 0;
  }

  @Override
  public int getLineNumber() throws DebugException {
    // not called
    return 0;
  }

  @Override
  public String getName() throws DebugException {
    return thread.getName();
  }

  @Override
  public IRegisterGroup[] getRegisterGroups() throws DebugException {
    return null;
  }

  @Override
  public IThread getThread() {
    return thread;
  }

  @Override
  public IVariable[] getVariables() throws DebugException {
    IVariable[] variables = new IVariable[0];

    if (!isSuspended()) {
      return variables;
    }

    WebkitCallFrame frame = getWebkitFrame();

    if (frame != null) {
      WebkitRemoteObject librariesScope = frame.getLibrariesScope();

      if (librariesScope != null) {
        VariableCollector variableCollector = VariableCollector.createCollector(
            getTarget(),
            null,
            Collections.singletonList(librariesScope));

        try {
          variables = variableCollector.getVariables();
        } catch (InterruptedException e) {

        }
      }
    }

    return variables;
  }

  private WebkitCallFrame getWebkitFrame() throws DebugException {
    IStackFrame topFrame = thread.getTopStackFrame();

    if (topFrame instanceof WebkitDebugStackFrame) {
      return ((WebkitDebugStackFrame) topFrame).getWebkitFrame();
    } else {
      return null;
    }
  }

  @Override
  public boolean hasRegisterGroups() throws DebugException {
    return false;
  }

  @Override
  public boolean hasVariables() throws DebugException {
    return true;
  }

  @Override
  public boolean isStepping() {
    return thread.isStepping();
  }

  @Override
  public boolean isSuspended() {
    return thread.isSuspended();
  }

  @Override
  public boolean isTerminated() {
    return thread.isTerminated();
  }

  @Override
  public void resume() throws DebugException {
    thread.resume();
  }

  @Override
  public void stepInto() throws DebugException {
    thread.stepInto();
  }

  @Override
  public void stepOver() throws DebugException {
    thread.stepOver();
  }

  @Override
  public void stepReturn() throws DebugException {
    thread.stepReturn();
  }

  @Override
  public void suspend() throws DebugException {
    thread.suspend();
  }

  @Override
  public void terminate() throws DebugException {
    thread.terminate();
  }

}
