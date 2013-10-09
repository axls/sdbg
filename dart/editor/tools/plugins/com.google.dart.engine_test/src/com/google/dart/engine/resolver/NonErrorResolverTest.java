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
package com.google.dart.engine.resolver;

import com.google.dart.engine.error.HintCode;
import com.google.dart.engine.parser.ParserErrorCode;
import com.google.dart.engine.source.Source;

public class NonErrorResolverTest extends ResolverTestCase {
  public void test_ambiguousExport() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "export 'lib1.dart';",
        "export 'lib2.dart';"));
    addSource("/lib1.dart", createSource(//
        "library lib1;",
        "class M {}"));
    addSource("/lib2.dart", createSource(//
        "library lib2;",
        "class N {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_ambiguousExport_combinators_hide() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "export 'lib1.dart';",
        "export 'lib2.dart' hide B;"));
    addSource("/lib1.dart", createSource(//
        "library L1;",
        "class A {}",
        "class B {}"));
    addSource("/lib2.dart", createSource(//
        "library L2;",
        "class B {}",
        "class C {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_ambiguousExport_combinators_show() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "export 'lib1.dart';",
        "export 'lib2.dart' show C;"));
    addSource("/lib1.dart", createSource(//
        "library L1;",
        "class A {}",
        "class B {}"));
    addSource("/lib2.dart", createSource(//
        "library L2;",
        "class B {}",
        "class C {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_argumentDefinitionTestNonParameter_formalParameter() throws Exception {
    Source source = addSource(createSource(//
        "f(var v) {",
        "  return ?v;",
        "}"));
    resolve(source);
    assertErrors(source, ParserErrorCode.DEPRECATED_ARGUMENT_DEFINITION_TEST);
    verify(source);
  }

  public void test_argumentDefinitionTestNonParameter_namedParameter() throws Exception {
    Source source = addSource(createSource(//
        "f({var v : 0}) {",
        "  return ?v;",
        "}"));
    resolve(source);
    assertErrors(source, ParserErrorCode.DEPRECATED_ARGUMENT_DEFINITION_TEST);
    verify(source);
  }

  public void test_argumentDefinitionTestNonParameter_optionalParameter() throws Exception {
    Source source = addSource(createSource(//
        "f([var v]) {",
        "  return ?v;",
        "}"));
    resolve(source);
    assertErrors(source, ParserErrorCode.DEPRECATED_ARGUMENT_DEFINITION_TEST);
    verify(source);
  }

  public void test_argumentTypeNotAssignable_classWithCall_Function() throws Exception {
    Source source = addSource(createSource(//
        "  caller(Function callee) {",
        "    callee();",
        "  }",
        "",
        "  class CallMeBack {",
        "    call() => 0;",
        "  }",
        "",
        "  main() {",
        "    caller(new CallMeBack());",
        "  }"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_argumentTypeNotAssignable_invocation_functionParameter_generic()
      throws Exception {
    Source source = addSource(createSource(//
        "class A<K> {",
        "  m(f(K k), K v) {",
        "    f(v);",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_argumentTypeNotAssignable_invocation_typedef_generic() throws Exception {
    Source source = addSource(createSource(//
        "typedef A<T>(T p);",
        "f(A<int> a) {",
        "  a(1);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_argumentTypeNotAssignable_Object_Function() throws Exception {
    Source source = addSource(createSource(//
        "main() {",
        "  process(() {});",
        "}",
        "process(Object x) {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_argumentTypeNotAssignable_typedef_local() throws Exception {
    Source source = addSource(createSource(//
        "typedef A(int p1, String p2);",
        "A getA() => null;",
        "f() {",
        "  A a = getA();",
        "  a(1, '2');",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_argumentTypeNotAssignable_typedef_parameter() throws Exception {
    Source source = addSource(createSource(//
        "typedef A(int p1, String p2);",
        "f(A a) {",
        "  a(1, '2');",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_assignmentToFinal_prefixNegate() throws Exception {
    Source source = addSource(createSource(//
        "f() {",
        "  final x = 0;",
        "  -x;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_assignmentToFinals_importWithPrefix() throws Exception {
    Source source = addSource(createSource(//
        "library lib;",
        "import 'lib1.dart' as foo;",
        "main() {",
        "  foo.x = true;",
        "}"));
    addSource("/lib1.dart", createSource(//
        "library lib1;",
        "bool x = false;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_breakWithoutLabelInSwitch() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  void m(int i) {",
        "    switch (i) {",
        "      case 0:",
        "        break;",
        "    }",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_builtInIdentifierAsType_dynamic() throws Exception {
    Source source = addSource(createSource(//
        "f() {",
        "  dynamic x;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_caseBlockNotTerminated() throws Exception {
    Source source = addSource(createSource(//
        "f(int p) {",
        "  for (int i = 0; i < 10; i++) {",
        "    switch (p) {",
        "      case 0:",
        "        break;",
        "      case 1:",
        "        continue;",
        "      case 2:",
        "        return;",
        "      case 3:",
        "        throw new Object();",
        "      case 4:",
        "      case 5:",
        "        return;",
        "      case 6:",
        "      default:",
        "        return;",
        "    }",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_caseBlockNotTerminated_lastCase() throws Exception {
    Source source = addSource(createSource(//
        "f(int p) {",
        "  switch (p) {",
        "    case 0:",
        "      p = p + 1;",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_caseExpressionTypeImplementsEquals_int() throws Exception {
    Source source = addSource(createSource(//
        "f(int i) {",
        "  switch(i) {",
        "    case(1) : return 1;",
        "    default: return 0;",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_caseExpressionTypeImplementsEquals_Object() throws Exception {
    Source source = addSource(createSource(//
        "class IntWrapper {",
        "  final int value;",
        "  const IntWrapper(this.value);",
        "}",
        "",
        "f(IntWrapper intWrapper) {",
        "  switch(intWrapper) {",
        "    case(const IntWrapper(1)) : return 1;",
        "    default: return 0;",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_caseExpressionTypeImplementsEquals_String() throws Exception {
    Source source = addSource(createSource(//
        "f(String s) {",
        "  switch(s) {",
        "    case('1') : return 1;",
        "    default: return 0;",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_concreteClassWithAbstractMember() throws Exception {
    Source source = addSource(createSource(//
        "abstract class A {",
        "  m();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_conflictingInstanceGetterAndSuperclassMember_instance() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  get v => 0;",
        "}",
        "class B extends A {",
        "  get v => 1;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_conflictingStaticGetterAndInstanceSetter_thisClass() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  static get x => 0;",
        "  static set x(int p) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_conflictingStaticSetterAndInstanceMember_thisClass_method() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  static x() {}",
        "  static set x(int p) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constConstructorWithNonConstSuper_redirectingFactory() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A();",
        "}",
        "class B implements C {",
        "  const B();",
        "}",
        "class C extends A {",
        "  const factory C() = B;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constConstructorWithNonFinalField_finalInstanceVar() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final int x = 0;",
        "  const A();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constConstructorWithNonFinalField_mixin() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  a() {}",
        "}",
        "class B extends Object with A {",
        "  const B();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constConstructorWithNonFinalField_static() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  static int x;",
        "  const A();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constConstructorWithNonFinalField_syntheticField() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  const A();",
        "  set x(value) {}",
        "  get x {return 0;}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constEval_propertyExtraction_fieldStatic_targetType() throws Exception {
    addSource("/math.dart", createSource(//
        "library math;",
        "const PI = 3.14;"));
    Source source = addSource(createSource(//
        "import 'math.dart' as math;",
        "const C = math.PI;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constEval_propertyExtraction_methodStatic_targetType() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  const A();",
        "  static m() {}",
        "}",
        "const C = A.m;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constEval_symbol() throws Exception {
    addSource("/math.dart", createSource(//
        "library math;",
        "const PI = 3.14;"));
    Source source = addSource(createSource(//
        "const C = #foo;",
        "foo() {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constEvalTypeBoolNumString_equal() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  const A();",
        "}",
        "class B {",
        "  final v;",
        "  const B.a1(bool p) : v = p == true;",
        "  const B.a2(bool p) : v = p == false;",
        "  const B.a3(bool p) : v = p == 0;",
        "  const B.a4(bool p) : v = p == 0.0;",
        "  const B.a5(bool p) : v = p == '';",
        "  const B.b1(int p) : v = p == true;",
        "  const B.b2(int p) : v = p == false;",
        "  const B.b3(int p) : v = p == 0;",
        "  const B.b4(int p) : v = p == 0.0;",
        "  const B.b5(int p) : v = p == '';",
        "  const B.c1(String p) : v = p == true;",
        "  const B.c2(String p) : v = p == false;",
        "  const B.c3(String p) : v = p == 0;",
        "  const B.c4(String p) : v = p == 0.0;",
        "  const B.c5(String p) : v = p == '';",
        "  const B.n1(num p) : v = p == null;",
        "  const B.n2(num p) : v = null == p;",
        "}"));
    resolve(source);
    assertNoErrors(source);
  }

  public void test_constEvalTypeBoolNumString_notEqual() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  const A();",
        "}",
        "class B {",
        "  final v;",
        "  const B.a1(bool p) : v = p != true;",
        "  const B.a2(bool p) : v = p != false;",
        "  const B.a3(bool p) : v = p != 0;",
        "  const B.a4(bool p) : v = p != 0.0;",
        "  const B.a5(bool p) : v = p != '';",
        "  const B.b1(int p) : v = p != true;",
        "  const B.b2(int p) : v = p != false;",
        "  const B.b3(int p) : v = p != 0;",
        "  const B.b4(int p) : v = p != 0.0;",
        "  const B.b5(int p) : v = p != '';",
        "  const B.c1(String p) : v = p != true;",
        "  const B.c2(String p) : v = p != false;",
        "  const B.c3(String p) : v = p != 0;",
        "  const B.c4(String p) : v = p != 0.0;",
        "  const B.c5(String p) : v = p != '';",
        "  const B.n1(num p) : v = p != null;",
        "  const B.n2(num p) : v = null != p;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constNotInitialized_field() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  static const int x = 0;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constNotInitialized_local() throws Exception {
    Source source = addSource(createSource(//
        "main() {",
        "  const int x = 0;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constructorDeclaration_scope_signature() throws Exception {
    Source source = addSource(createSource(//
        "const app = 0;",
        "class A {",
        "  A(@app int app) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constWithNonConstantArgument_literals() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  const A(a, b, c, d);",
        "}",
        "f() { return const A(true, 0, 1.0, '2'); }"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constWithTypeParameters_direct() throws Exception {
    Source source = addSource(createSource(//
        "class A<T> {",
        "  static const V = const A<int>();",
        "  const A();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constWithUndefinedConstructor() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  const A.name();",
        "}",
        "f() {",
        "  return const A.name();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_constWithUndefinedConstructorDefault() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  const A();",
        "}",
        "f() {",
        "  return const A();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_defaultValueInFunctionTypeAlias() throws Exception {
    Source source = addSource(createSource(//
    "typedef F([x]);"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_defaultValueInFunctionTypedParameter_named() throws Exception {
    Source source = addSource(createSource(//
    "f(g({p})) {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_defaultValueInFunctionTypedParameter_optional() throws Exception {
    Source source = addSource(createSource(//
    "f(g([p])) {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_duplicateDefinition_emptyName() throws Exception {
    // Note: This code has two FunctionElements '() {}' with an empty name, this tests that the
    // empty string is not put into the scope (more than once).
    Source source = addSource(createSource(//
        "Map _globalMap = {",
        "  'a' : () {},",
        "  'b' : () {}",
        "};"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_duplicateDefinition_getter() throws Exception {
    Source source = addSource(createSource(//
    "bool get a => true;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_dynamicIdentifier() throws Exception {
    Source source = addSource(createSource(//
        "main() {",
        "  var v = dynamic;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_exportOfNonLibrary_libraryDeclared() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "export 'lib1.dart';"));
    addSource("/lib1.dart", createSource(//
        "library lib1;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_exportOfNonLibrary_libraryNotDeclared() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "export 'lib1.dart';"));
    addSource("/lib1.dart", createSource(//
        ""));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_extraPositionalArguments_function() throws Exception {
    Source source = addSource(createSource(//
        "f(p1, p2) {}",
        "main() {",
        "  f(1, 2);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_extraPositionalArguments_Function() throws Exception {
    Source source = addSource(createSource(//
        "f(Function a) {",
        "  a(1, 2);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_extraPositionalArguments_typedef_local() throws Exception {
    Source source = addSource(createSource(//
        "typedef A(p1, p2);",
        "A getA() => null;",
        "f() {",
        "  A a = getA();",
        "  a(1, 2);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_extraPositionalArguments_typedef_parameter() throws Exception {
    Source source = addSource(createSource(//
        "typedef A(p1, p2);",
        "f(A a) {",
        "  a(1, 2);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_fieldInitializedByMultipleInitializers() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  int x;",
        "  int y;",
        "  A() : x = 0, y = 0 {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_fieldInitializedInInitializerAndDeclaration_fieldNotFinal() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  int x = 0;",
        "  A() : x = 1 {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_fieldInitializedInInitializerAndDeclaration_finalFieldNotSet() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final int x;",
        "  A() : x = 1 {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_fieldInitializerOutsideConstructor() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  int x;",
        "  A(this.x) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_fieldInitializerOutsideConstructor_defaultParameters() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  int x;",
        "  A([this.x]) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_fieldInitializerRedirectingConstructor_super() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A() {}",
        "}",
        "class B extends A {",
        "  int x;",
        "  B(this.x) : super();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_finalInitializedInDeclarationAndConstructor_initializer() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final x;",
        "  A() : x = 1 {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_finalInitializedInDeclarationAndConstructor_initializingFormal()
      throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final x;",
        "  A(this.x) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_finalNotInitialized_atDeclaration() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final int x = 0;",
        "  A() {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_finalNotInitialized_fieldFormal() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final int x = 0;",
        "  A() {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_finalNotInitialized_functionTypedFieldFormal() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final Function x;",
        "  A(int this.x(int p)) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_finalNotInitialized_hasNativeClause_hasConstructor() throws Exception {
    Source source = addSource(createSource(//
        "class A native 'something' {",
        "  final int x;",
        "  A() {}",
        "}"));
    resolve(source);
    assertErrors(source, ParserErrorCode.NATIVE_CLAUSE_IN_NON_SDK_CODE);
    verify(source);
  }

  public void test_finalNotInitialized_hasNativeClause_noConstructor() throws Exception {
    Source source = addSource(createSource(//
        "class A native 'something' {",
        "  final int x;",
        "}"));
    resolve(source);
    assertErrors(source, ParserErrorCode.NATIVE_CLAUSE_IN_NON_SDK_CODE);
    verify(source);
  }

  public void test_finalNotInitialized_initializer() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final int x;",
        "  A() : x = 0 {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_finalNotInitialized_redirectingConstructor() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final int x;",
        "  A(this.x);",
        "  A.named() : this (42);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_functionDeclaration_scope_returnType() throws Exception {
    Source source = addSource(createSource(//
    "int f(int) {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_functionDeclaration_scope_signature() throws Exception {
    Source source = addSource(createSource(//
        "const app = 0;",
        "f(@app int app) {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_functionTypeAlias_scope_returnType() throws Exception {
    Source source = addSource(createSource(//
    "typedef int f(int);"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_functionTypeAlias_scope_signature() throws Exception {
    Source source = addSource(createSource(//
        "const app = 0;",
        "typedef int f(@app int app);"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_implicitThisReferenceInInitializer_constructorName() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A.named() {}",
        "}",
        "class B {",
        "  var v;",
        "  B() : v = new A.named();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_implicitThisReferenceInInitializer_prefixedIdentifier() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var f;",
        "}",
        "class B {",
        "  var v;",
        "  B(A a) : v = a.f;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_implicitThisReferenceInInitializer_qualifiedMethodInvocation() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  f() {}",
        "}",
        "class B {",
        "  var v;",
        "  B() : v = new A().f();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_implicitThisReferenceInInitializer_qualifiedPropertyAccess() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var f;",
        "}",
        "class B {",
        "  var v;",
        "  B() : v = new A().f;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_implicitThisReferenceInInitializer_staticField_thisClass() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var v;",
        "  A() : v = f;",
        "  static var f;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_implicitThisReferenceInInitializer_staticGetter() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var v;",
        "  A() : v = f;",
        "  static get f => 42;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_implicitThisReferenceInInitializer_staticMethod() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var v;",
        "  A() : v = f();",
        "  static f() => 42;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_implicitThisReferenceInInitializer_topLevelField() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var v;",
        "  A() : v = f;",
        "}",
        "var f = 42;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_implicitThisReferenceInInitializer_topLevelFunction() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var v;",
        "  A() : v = f();",
        "}",
        "f() => 42;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_implicitThisReferenceInInitializer_topLevelGetter() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var v;",
        "  A() : v = f;",
        "}",
        "get f => 42;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_implicitThisReferenceInInitializer_typeParameter() throws Exception {
    Source source = addSource(createSource(//
        "class A<T> {",
        "  var v;",
        "  A(p) : v = (p is T);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_importDuplicatedLibraryName() throws Exception {
    Source source = addSource(createSource(//
        "library test;",
        "import 'lib.dart';",
        "import 'lib.dart';"));
    addSource("/lib.dart", "library lib;");
    resolve(source);
    assertErrors(source, HintCode.UNUSED_IMPORT, HintCode.UNUSED_IMPORT, HintCode.DUPLICATE_IMPORT);
    verify(source);
  }

  public void test_importOfNonLibrary_libraryDeclared() throws Exception {
    Source source = addSource(createSource(//
        "library lib;",
        "import 'part.dart';",
        "A a;"));
    addSource("/part.dart", createSource(//
        "library lib1;",
        "class A {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_importOfNonLibrary_libraryNotDeclared() throws Exception {
    Source source = addSource(createSource(//
        "library lib;",
        "import 'part.dart';",
        "A a;"));
    addSource("/part.dart", createSource(//
        "class A {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_importPrefixes_withFirstLetterDifference() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "import 'lib1.dart' as math;",
        "import 'lib2.dart' as path;",
        "main() {",
        "  math.test1();",
        "  path.test2();",
        "}"));
    addSource("/lib1.dart", createSource(//
        "library lib1;",
        "test1() {}"));
    addSource("/lib2.dart", createSource(//
        "library lib2;",
        "test2() {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_inconsistentCaseExpressionTypes() throws Exception {
    Source source = addSource(createSource(//
        "f(var p) {",
        "  switch (p) {",
        "    case 1:",
        "      break;",
        "    case 2:",
        "      break;",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_inconsistentMethodInheritance_accessors_typeParameter2() throws Exception {
    Source source = addSource(createSource(//
        "abstract class A<E> {",
        "  E get x {return 1;}",
        "}",
        "class B<E> {",
        "  E get x {return 1;}",
        "}",
        "class C<E> extends A<E> implements B<E> {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_inconsistentMethodInheritance_accessors_typeParameters_diamond()
      throws Exception {
    Source source = addSource(createSource(//
        "abstract class F<E> extends B<E> {}",
        "class D<E> extends F<E> {",
        "  external E get g;",
        "}",
        "abstract class C<E> {",
        "  E get g;",
        "}",
        "abstract class B<E> implements C<E> {",
        "  E get g { return null; }",
        "}",
        "class A<E> extends B<E> implements D<E> {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_inconsistentMethodInheritance_accessors_typeParameters1() throws Exception {
    Source source = addSource(createSource(//
        "abstract class A<E> {",
        "  E get x;",
        "}",
        "abstract class B<E> {",
        "  E get x;",
        "}",
        "class C<E> implements A<E>, B<E> {",
        "  E get x => 1;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_inconsistentMethodInheritance_methods_typeParameter2() throws Exception {
    Source source = addSource(createSource(//
        "class A<E> {",
        "  x(E e) {}",
        "}",
        "class B<E> {",
        "  x(E e) {}",
        "}",
        "class C<E> extends A<E> implements B<E> {",
        "  x(E e) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_inconsistentMethodInheritance_methods_typeParameters1() throws Exception {
    Source source = addSource(createSource(//
        "class A<E> {",
        "  x(E e) {}",
        "}",
        "class B<E> {",
        "  x(E e) {}",
        "}",
        "class C<E> implements A<E>, B<E> {",
        "  x(E e) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_inconsistentMethodInheritance_simple() throws Exception {
    Source source = addSource(createSource(//
        "abstract class A {",
        "  x();",
        "}",
        "abstract class B {",
        "  x();",
        "}",
        "class C implements A, B {",
        "  x() {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_initializingFormalForNonExistantField() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  int x;",
        "  A(this.x) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_instanceAccessToStaticMember_fromComment() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  static m() {}",
        "}",
        "/// [A.m]",
        "main() {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_instanceAccessToStaticMember_topLevel() throws Exception {
    Source source = addSource(createSource(//
        "m() {}",
        "main() {",
        "  m();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_instanceMemberAccessFromStatic_fromComment() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m() {}",
        "  /// [m]",
        "  static foo() {",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_instanceMethodNameCollidesWithSuperclassStatic_field() throws Exception {
    Source source = addSource(createSource(//
        "import 'lib.dart';",
        "class B extends A {",
        "  _m() {}",
        "}"));
    addSource("/lib.dart", createSource(//
        "library L;",
        "class A {",
        "  static var _m;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_instanceMethodNameCollidesWithSuperclassStatic_method() throws Exception {
    Source source = addSource(createSource(//
        "import 'lib.dart';",
        "class B extends A {",
        "  _m() {}",
        "}"));
    addSource("/lib.dart", createSource(//
        "library L;",
        "class A {",
        "  static _m() {}",
        "}"));
    resolve(source);
    assertErrors(source, HintCode.OVERRIDDING_PRIVATE_MEMBER);
    verify(source);
  }

  public void test_invalidAnnotation_constantVariable() throws Exception {
    Source source = addSource(createSource(//
        "const C = 0;",
        "@C",
        "main() {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidAnnotation_importWithPrefix_constantVariable() throws Exception {
    addSource("/lib.dart", createSource(//
        "library lib;",
        "const C = 0;"));
    Source source = addSource(createSource(//
        "import 'lib.dart' as p;",
        "@p.C",
        "main() {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidAnnotation_importWithPrefix_constConstructor() throws Exception {
    addSource("/lib.dart", createSource(//
        "library lib;",
        "class A {",
        "  const A.named(int p);",
        "}"));
    Source source = addSource(createSource(//
        "import 'lib.dart' as p;",
        "@p.A.named(42)",
        "main() {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidAssignment() throws Exception {
    Source source = addSource(createSource(//
        "f() {",
        "  var x;",
        "  var y;",
        "  x = y;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidAssignment_compoundAssignment() throws Exception {
    Source source = addSource(createSource(//
        "class byte {",
        "  int _value;",
        "  byte(this._value);",
        "  byte operator +(int val) {}",
        "}",
        "",
        "void main() {",
        "  byte b = new byte(52);",
        "  b += 3;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidAssignment_defaultValue_named() throws Exception {
    Source source = addSource(createSource(//
        "f({String x: '0'}) {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidAssignment_defaultValue_optional() throws Exception {
    Source source = addSource(createSource(//
        "f([String x = '0']) {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidAssignment_toDynamic() throws Exception {
    Source source = addSource(createSource(//
        "f() {",
        "  var g;",
        "  g = () => 0;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidFactoryNameNotAClass() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  factory A() {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidMethodOverrideNamedParamType() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m({int a}) {}",
        "}",
        "class B implements A {",
        "  m({int a, int b}) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideDifferentDefaultValues_named() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m({int p : 0}) {}",
        "}",
        "class B extends A {",
        "  m({int p : 0}) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideDifferentDefaultValues_positional() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m([int p = 0]) {}",
        "}",
        "class B extends A {",
        "  m([int p = 0]) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideDifferentDefaultValues_positional_changedOrder() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m([int a = 0, String b = '0']) {}",
        "}",
        "class B extends A {",
        "  m([int b = 0, String a = '0']) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideNamed_unorderedNamedParameter() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m({a, b}) {}",
        "}",
        "class B extends A {",
        "  m({b, a}) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideRequired_less() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m(a, b) {}",
        "}",
        "class B extends A {",
        "  m(a) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideRequired_same() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m(a) {}",
        "}",
        "class B extends A {",
        "  m(a) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideReturnType_returnType_interface() throws Exception {
    Source source = addSource("/test.dart", createSource(//
        "abstract class A {",
        "  num m();",
        "}",
        "class B implements A {",
        "  int m() { return 1; }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideReturnType_returnType_interface2() throws Exception {
    Source source = addSource("/test.dart", createSource(//
        "abstract class A {",
        "  num m();",
        "}",
        "abstract class B implements A {",
        "}",
        "class C implements B {",
        "  int m() { return 1; }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideReturnType_returnType_mixin() throws Exception {
    Source source = addSource("/test.dart", createSource(//
        "class A {",
        "  num m() { return 0; }",
        "}",
        "class B extends Object with A {",
        "  int m() { return 1; }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideReturnType_returnType_parameterizedTypes() throws Exception {
    Source source = addSource(createSource(//
        "abstract class A<E> {",
        "  List<E> m();",
        "}",
        "class B extends A<dynamic> {",
        "  List<dynamic> m() { return new List<dynamic>(); }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideReturnType_returnType_sameType() throws Exception {
    Source source = addSource("/test.dart", createSource(//
        "class A {",
        "  int m() { return 0; }",
        "}",
        "class B extends A {",
        "  int m() { return 1; }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideReturnType_returnType_superclass() throws Exception {
    Source source = addSource("/test.dart", createSource(//
        "class A {",
        "  num m() { return 0; }",
        "}",
        "class B extends A {",
        "  int m() { return 1; }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideReturnType_returnType_superclass2() throws Exception {
    Source source = addSource("/test.dart", createSource(//
        "class A {",
        "  num m() { return 0; }",
        "}",
        "class B extends A {",
        "}",
        "class C extends B {",
        "  int m() { return 1; }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidOverrideReturnType_returnType_void() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  void m() {}",
        "}",
        "class B extends A {",
        "  int m() {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidReferenceToThis_constructor() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A() {",
        "    var v = this;",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidReferenceToThis_instanceMethod() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m() {",
        "    var v = this;",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidTypeArgumentForKey() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m() {",
        "    return const <int, int>{};",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidTypeArgumentInConstList() throws Exception {
    Source source = addSource(createSource(//
        "class A<E> {",
        "  m() {",
        "    return <E>[];",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invalidTypeArgumentInConstMap() throws Exception {
    Source source = addSource(createSource(//
        "class A<E> {",
        "  m() {",
        "    return <String, E>{};",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invocationOfNonFunction_dynamic() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var f;",
        "}",
        "class B extends A {",
        "  g() {",
        "    f();",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invocationOfNonFunction_getter() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var g;",
        "}",
        "f() {",
        "  A a;",
        "  a.g();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invocationOfNonFunction_localVariable() throws Exception {
    Source source = addSource(createSource(//
        "f() {",
        "  var g;",
        "  g();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invocationOfNonFunction_localVariable_dynamic() throws Exception {
    Source source = addSource(createSource(//
        "f() {}",
        "main() {",
        "  var v = f;",
        "  v();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invocationOfNonFunction_localVariable_dynamic2() throws Exception {
    Source source = addSource(createSource(//
        "f() {}",
        "main() {",
        "  var v = f;",
        "  v = 1;",
        "  v();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_invocationOfNonFunction_Object() throws Exception {
    Source source = addSource(createSource(//
        "main() {",
        "  Object v = null;",
        "  v();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_listElementTypeNotAssignable() throws Exception {
    Source source = addSource(createSource(//
        "var v1 = <int> [42];",
        "var v2 = const <int> [42];"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_mapKeyTypeNotAssignable() throws Exception {
    Source source = addSource(createSource(//
    "var v = <String, int > {'a' : 1};"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_memberWithClassName_setter() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  set A(v) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_methodDeclaration_scope_signature() throws Exception {
    Source source = addSource(createSource(//
        "const app = 0;",
        "class A {",
        "  foo(@app int app) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_misMatchedGetterAndSetterTypes_instance_sameTypes() throws Exception {
    Source source = addSource(createSource(//
        "class C {",
        "  int get x => 0;",
        "  set x(int v) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_misMatchedGetterAndSetterTypes_instance_unspecifiedGetter() throws Exception {
    Source source = addSource(createSource(//
        "class C {",
        "  get x => 0;",
        "  set x(String v) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_misMatchedGetterAndSetterTypes_instance_unspecifiedSetter() throws Exception {
    Source source = addSource(createSource(//
        "class C {",
        "  int get x => 0;",
        "  set x(v) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_misMatchedGetterAndSetterTypes_topLevel_sameTypes() throws Exception {
    Source source = addSource(createSource(//
        "int get x => 0;",
        "set x(int v) {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_misMatchedGetterAndSetterTypes_topLevel_unspecifiedGetter() throws Exception {
    Source source = addSource(createSource(//
        "get x => 0;",
        "set x(String v) {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_misMatchedGetterAndSetterTypes_topLevel_unspecifiedSetter() throws Exception {
    Source source = addSource(createSource(//
        "int get x => 0;",
        "set x(v) {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_mixinDeclaresConstructor() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m() {}",
        "}",
        "class B extends Object with A {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_mixinDeclaresConstructor_factory() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  factory A() {}",
        "}",
        "class B extends Object with A {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_mixinInheritsFromNotObject_classDeclaration_mixTypeAlias() throws Exception {
    Source source = addSource(createSource(//
        "class A {}",
        "class B = Object with A;",
        "class C extends Object with B {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_mixinInheritsFromNotObject_typedef_mixTypeAlias() throws Exception {
    Source source = addSource(createSource(//
        "class A {}",
        "class B = Object with A;",
        "class C = Object with B;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_multipleSuperInitializers_no() throws Exception {
    Source source = addSource(createSource(//
        "class A {}",
        "class B extends A {",
        "  B() {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_multipleSuperInitializers_single() throws Exception {
    Source source = addSource(createSource(//
        "class A {}",
        "class B extends A {",
        "  B() : super() {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_newWithAbstractClass_factory() throws Exception {
    Source source = addSource(createSource(//
        "abstract class A {",
        "  factory A() { return new B(); }",
        "}",
        "class B implements A {",
        "  B() {}",
        "}",
        "A f() {",
        "  return new A();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_newWithUndefinedConstructor() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A.name() {}",
        "}",
        "f() {",
        "  new A.name();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_newWithUndefinedConstructorDefault() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A() {}",
        "}",
        "f() {",
        "  new A();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonAbstractClassInheritsAbstractMemberOne_abstractOverridesConcrete_accessor()
      throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  int get g => 0;",
        "}",
        "abstract class B extends A {",
        "  int get g;",
        "}",
        "class C extends B {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonAbstractClassInheritsAbstractMemberOne_abstractOverridesConcrete_method()
      throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m(p) {}",
        "}",
        "abstract class B extends A {",
        "  m(p);",
        "}",
        "class C extends B {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonBoolExpression_functionType() throws Exception {
    Source source = addSource(createSource(//
        "bool makeAssertion() => true;",
        "f() {",
        "  assert(makeAssertion);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonBoolExpression_interfaceType() throws Exception {
    Source source = addSource(createSource(//
        "f() {",
        "  assert(true);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstantDefaultValue_function_named() throws Exception {
    Source source = addSource(createSource(//
    "f({x : 2 + 3}) {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstantDefaultValue_function_positional() throws Exception {
    Source source = addSource(createSource(//
    "f([x = 2 + 3]) {}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstantDefaultValue_inConstructor_named() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A({x : 2 + 3}) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstantDefaultValue_inConstructor_positional() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A([x = 2 + 3]) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstantDefaultValue_method_named() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m({x : 2 + 3}) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstantDefaultValue_method_positional() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m([x = 2 + 3]) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstantValueInInitializer_namedArgument() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final a;",
        "  const A({this.a});",
        "}",
        "class B extends A {",
        "  const B({b}) : super(a: b);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstCaseExpression() throws Exception {
    Source source = addSource(createSource(//
        "f(Type t) {",
        "  switch (t) {",
        "    case bool:",
        "    case int:",
        "      return true;",
        "    default:",
        "      return false;",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstMapAsExpressionStatement_const() throws Exception {
    Source source = addSource(createSource(//
        "f() {",
        "  const {'a' : 0, 'b' : 1};",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstMapAsExpressionStatement_notExpressionStatement() throws Exception {
    Source source = addSource(createSource(//
        "f() {",
        "  var m = {'a' : 0, 'b' : 1};",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstMapAsExpressionStatement_typeArguments() throws Exception {
    Source source = addSource(createSource(//
        "f() {",
        "  <String, int> {'a' : 0, 'b' : 1};",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstValueInInitializer_binary_bool() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final v;",
        "  const A.a1(bool p) : v = p && true;",
        "  const A.a2(bool p) : v = true && p;",
        "  const A.b1(bool p) : v = p || true;",
        "  const A.b2(bool p) : v = true || p;",
        "}"));
    resolve(source);
    assertErrors(source, HintCode.DEAD_CODE);
    verify(source);
  }

  public void test_nonConstValueInInitializer_binary_dynamic() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final v;",
        "  const A.a1(p) : v = p + 5;",
        "  const A.a2(p) : v = 5 + p;",
        "  const A.b1(p) : v = p - 5;",
        "  const A.b2(p) : v = 5 - p;",
        "  const A.c1(p) : v = p * 5;",
        "  const A.c2(p) : v = 5 * p;",
        "  const A.d1(p) : v = p / 5;",
        "  const A.d2(p) : v = 5 / p;",
        "  const A.e1(p) : v = p ~/ 5;",
        "  const A.e2(p) : v = 5 ~/ p;",
        "  const A.f1(p) : v = p > 5;",
        "  const A.f2(p) : v = 5 > p;",
        "  const A.g1(p) : v = p < 5;",
        "  const A.g2(p) : v = 5 < p;",
        "  const A.h1(p) : v = p >= 5;",
        "  const A.h2(p) : v = 5 >= p;",
        "  const A.i1(p) : v = p <= 5;",
        "  const A.i2(p) : v = 5 <= p;",
        "  const A.j1(p) : v = p % 5;",
        "  const A.j2(p) : v = 5 % p;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    // operations on "p" are not resolved
  }

  public void test_nonConstValueInInitializer_binary_int() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final v;",
        "  const A.a1(int p) : v = p ^ 5;",
        "  const A.a2(int p) : v = 5 ^ p;",
        "  const A.b1(int p) : v = p & 5;",
        "  const A.b2(int p) : v = 5 & p;",
        "  const A.c1(int p) : v = p | 5;",
        "  const A.c2(int p) : v = 5 | p;",
        "  const A.d1(int p) : v = p >> 5;",
        "  const A.d2(int p) : v = 5 >> p;",
        "  const A.e1(int p) : v = p << 5;",
        "  const A.e2(int p) : v = 5 << p;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstValueInInitializer_binary_num() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final v;",
        "  const A.a1(num p) : v = p + 5;",
        "  const A.a2(num p) : v = 5 + p;",
        "  const A.b1(num p) : v = p - 5;",
        "  const A.b2(num p) : v = 5 - p;",
        "  const A.c1(num p) : v = p * 5;",
        "  const A.c2(num p) : v = 5 * p;",
        "  const A.d1(num p) : v = p / 5;",
        "  const A.d2(num p) : v = 5 / p;",
        "  const A.e1(num p) : v = p ~/ 5;",
        "  const A.e2(num p) : v = 5 ~/ p;",
        "  const A.f1(num p) : v = p > 5;",
        "  const A.f2(num p) : v = 5 > p;",
        "  const A.g1(num p) : v = p < 5;",
        "  const A.g2(num p) : v = 5 < p;",
        "  const A.h1(num p) : v = p >= 5;",
        "  const A.h2(num p) : v = 5 >= p;",
        "  const A.i1(num p) : v = p <= 5;",
        "  const A.i2(num p) : v = 5 <= p;",
        "  const A.j1(num p) : v = p % 5;",
        "  const A.j2(num p) : v = 5 % p;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstValueInInitializer_field() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final int a;",
        "  const A() : a = 5;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstValueInInitializer_redirecting() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  const A.named(p);",
        "  const A() : this.named(42);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstValueInInitializer_super() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  const A(p);",
        "}",
        "class B extends A {",
        "  const B() : super(42);",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonConstValueInInitializer_unary() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  final v;",
        "  const A.a(bool p) : v = !p;",
        "  const A.b(int p) : v = ~p;",
        "  const A.c(num p) : v = -p;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonGenerativeConstructor() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A.named() {}",
        "  factory A() {}",
        "}",
        "class B extends A {",
        "  B() : super.named();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonTypeInCatchClause_isClass() throws Exception {
    Source source = addSource(createSource(//
        "f() {",
        "  try {",
        "  } on String catch (e) {",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonTypeInCatchClause_isFunctionTypeAlias() throws Exception {
    Source source = addSource(createSource(//
        "typedef F();",
        "f() {",
        "  try {",
        "  } on F catch (e) {",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonTypeInCatchClause_isTypeParameter() throws Exception {
    Source source = addSource(createSource(//
        "class A<T> {",
        "  f() {",
        "    try {",
        "    } on T catch (e) {",
        "    }",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonTypeInCatchClause_noType() throws Exception {
    Source source = addSource(createSource(//
        "f() {",
        "  try {",
        "  } catch (e) {",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonVoidReturnForOperator_no() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  operator []=(a, b) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonVoidReturnForOperator_void() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  void operator []=(a, b) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonVoidReturnForSetter_function_no() throws Exception {
    Source source = addSource("set x(v) {}");
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonVoidReturnForSetter_function_void() throws Exception {
    Source source = addSource("void set x(v) {}");
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonVoidReturnForSetter_method_no() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  set x(v) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_nonVoidReturnForSetter_method_void() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  void set x(v) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_null_callMethod() throws Exception {
    Source source = addSource(createSource(//
        "main() {",
        "  null.m();",
        "}"));
    resolve(source);
    assertNoErrors(source);
  }

  public void test_null_callOperator() throws Exception {
    Source source = addSource(createSource(//
        "main() {",
        "  null + 5;",
        "  null == 5;",
        "  null[0];",
        "}"));
    resolve(source);
    assertNoErrors(source);
  }

  public void test_optionalParameterInOperator_required() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  operator +(p) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_prefixCollidesWithTopLevelMembers() throws Exception {
    addSource("/lib.dart", createSource(//
        "library lib;",
        "class A {}"));
    Source source = addSource(createSource(//
        "import 'lib.dart' as p;",
        "typedef P();",
        "p2() {}",
        "var p3;",
        "class p4 {}",
        "p.A a;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_proxy_annotation_prefixed() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "import 'meta.dart';",
        "@proxy",
        "class A {}",
        "f(A a) {",
        "  a.m();",
        "  var x = a.g;",
        "  a.s = 1;",
        "  var y = a + a;",
        "  a++;",
        "  ++a;",
        "}"));
    addSource("/meta.dart", createSource(//
        "library meta;",
        "const proxy = const _Proxy();",
        "class _Proxy { const _Proxy(); }"));
    resolve(source);
    assertNoErrors(source);
  }

  public void test_proxy_annotation_prefixed2() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "import 'meta.dart';",
        "@proxy",
        "class A {}",
        "class B {",
        "  f(A a) {",
        "    a.m();",
        "    var x = a.g;",
        "    a.s = 1;",
        "    var y = a + a;",
        "    a++;",
        "    ++a;",
        "  }",
        "}"));
    addSource("/meta.dart", createSource(//
        "library meta;",
        "const proxy = const _Proxy();",
        "class _Proxy { const _Proxy(); }"));
    resolve(source);
    assertNoErrors(source);
  }

  public void test_proxy_annotation_prefixed3() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "import 'meta.dart';",
        "class B {",
        "  f(A a) {",
        "    a.m();",
        "    var x = a.g;",
        "    a.s = 1;",
        "    var y = a + a;",
        "    a++;",
        "    ++a;",
        "  }",
        "}",
        "@proxy",
        "class A {}"));
    addSource("/meta.dart", createSource(//
        "library meta;",
        "const proxy = const _Proxy();",
        "class _Proxy { const _Proxy(); }"));
    resolve(source);
    assertNoErrors(source);
  }

  public void test_proxy_annotation_simple() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "import 'meta.dart';",
        "@proxy",
        "class B {",
        "  m() {",
        "    n();",
        "    var x = g;",
        "    s = 1;",
        "    var y = this + this;",
        "  }",
        "}"));
    addSource("/meta.dart", createSource(//
        "library meta;",
        "const proxy = const _Proxy();",
        "class _Proxy { const _Proxy(); }"));
    resolve(source);
    assertNoErrors(source);
  }

  public void test_recursiveConstructorRedirect() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A.a() : this.b();",
        "  A.b() : this.c();",
        "  A.c() {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_recursiveFactoryRedirect() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  factory A() = B;",
        "}",
        "class B implements A {",
        "  factory B() = C;",
        "}",
        "class C implements B {",
        "  factory C() {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_redirectToInvalidFunctionType() throws Exception {
    Source source = addSource(createSource(//
        "class A implements B {",
        "  A(int p) {}",
        "}",
        "class B {",
        "  factory B(int p) = A;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_redirectToInvalidReturnType() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A() {}",
        "}",
        "class B extends A {",
        "  factory B() = A;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_redirectToNonConstConstructor() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  const A.a();",
        "  const factory A.b() = A.a;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_referenceToDeclaredVariableInInitializer_constructorName() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A.x() {}",
        "}",
        "f() {",
        "  var x = new A.x();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_referenceToDeclaredVariableInInitializer_methodName() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  x() {}",
        "}",
        "f(A a) {",
        "  var x = a.x();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_referenceToDeclaredVariableInInitializer_propertyName() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var x;",
        "}",
        "f(A a) {",
        "  var x = a.x;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_rethrowOutsideCatch() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  void m() {",
        "    try {} catch (e) {rethrow;}",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_returnInGenerativeConstructor() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A() { return; }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_returnOfInvalidType_dynamic() throws Exception {
    Source source = addSource(createSource(//
        "class TypeError {}",
        "class A {",
        "  static void testLogicalOp() {",
        "    testOr(a, b, onTypeError) {",
        "      try {",
        "        return a || b;",
        "      } on TypeError catch (t) {",
        "        return onTypeError;",
        "      }",
        "    }",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_returnOfInvalidType_dynamicAsTypeArgument() throws Exception {
    Source source = addSource(createSource(//
        "class I<T> {",
        "  factory I() => new A<T>();",
        "}",
        "class A<T> implements I {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_returnOfInvalidType_subtype() throws Exception {
    Source source = addSource(createSource(//
        "class A {}",
        "class B extends A {}",
        "A f(B b) { return b; }"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_returnOfInvalidType_supertype() throws Exception {
    Source source = addSource(createSource(//
        "class A {}",
        "class B extends A {}",
        "B f(A a) { return a; }"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_returnOfInvalidType_void() throws Exception {
    Source source = addSource(createSource(//
        "void f1() {}",
        "void f2() { return; }",
        "void f3() { return null; }",
        "void f4() { return g1(); }",
        "void f5() { return g2(); }",
        "g1() {}",
        "void g2() {}",
        ""));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_returnWithoutValue_noReturnType() throws Exception {
    Source source = addSource(createSource(//
    "f() { return; }"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_returnWithoutValue_void() throws Exception {
    Source source = addSource(createSource(//
    "void f() { return; }"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_staticAccessToInstanceMember_annotation() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  const A.name();",
        "}",
        "@A.name()",
        "main() {",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_staticAccessToInstanceMember_method() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  static m() {}",
        "}",
        "main() {",
        "  A.m;",
        "  A.m();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_staticAccessToInstanceMember_propertyAccess_field() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  static var f;",
        "}",
        "main() {",
        "  A.f;",
        "  A.f = 1;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_staticAccessToInstanceMember_propertyAccess_propertyAccessor() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  static get f => 42;",
        "  static set f(x) {}",
        "}",
        "main() {",
        "  A.f;",
        "  A.f = 1;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_superInInvalidContext() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m() {}",
        "}",
        "class B extends A {",
        "  B() {",
        "    var v = super.m();",
        "  }",
        "  n() {",
        "    var v = super.m();",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_typeAliasCannotReferenceItself_returnClass_withTypeAlias() throws Exception {
    Source source = addSource(createSource(//
        "typedef B A();",
        "class B {",
        "  A a;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_typeAliasCannotReferenceItself_typeParameterBounds() throws Exception {
    Source source = addSource(createSource(//
    "typedef A<T extends A>();"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_typeArgumentNotMatchingBounds_const() throws Exception {
    Source source = addSource(createSource(//
        "class A {}",
        "class B extends A {}",
        "class G<E extends A> {",
        "  const G();",
        "}",
        "f() { return const G<B>(); }"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_typeArgumentNotMatchingBounds_new() throws Exception {
    Source source = addSource(createSource(//
        "class A {}",
        "class B extends A {}",
        "class G<E extends A> {}",
        "f() { return new G<B>(); }"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_typeArgumentNotMatchingBounds_typeArgumentList_0() throws Exception {
    Source source = addSource(createSource(//
    "abstract class A<T extends A>{}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_typeArgumentNotMatchingBounds_typeArgumentList_1() throws Exception {
    Source source = addSource(createSource(//
    "abstract class A<T extends A<A>>{}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_typeArgumentNotMatchingBounds_typeArgumentList_20() throws Exception {
    Source source = addSource(createSource(//
    "abstract class A<T extends A<A<A<A<A<A<A<A<A<A<A<A<A<A<A<A<A<A<A<A<A>>>>>>>>>>>>>>>>>>>>>{}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedConstructorInInitializer_explicit_named() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A.named() {}",
        "}",
        "class B extends A {",
        "  B() : super.named();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedConstructorInInitializer_explicit_unnamed() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A() {}",
        "}",
        "class B extends A {",
        "  B() : super();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedConstructorInInitializer_hasOptionalParameters() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A([p]) {}",
        "}",
        "class B extends A {",
        "  B();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedConstructorInInitializer_implicit() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  A() {}",
        "}",
        "class B extends A {",
        "  B();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedConstructorInInitializer_implicit_typeAlias() throws Exception {
    Source source = addSource(createSource(//
        "class M {}",
        "class A = Object with M;",
        "class B extends A {",
        "  B();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedConstructorInInitializer_redirecting() throws Exception {
    Source source = addSource(createSource(//
        "class Foo {",
        "  Foo.ctor();",
        "}",
        "class Bar extends Foo {",
        "  Bar() : this.ctor();",
        "  Bar.ctor() : super.ctor();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedGetter_typeSubstitution() throws Exception {
    Source source = addSource(createSource(//
        "class A<E> {",
        "  E element;",
        "}",
        "class B extends A<List> {",
        "  m() {",
        "    element.last;",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedIdentifier_hide() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "export 'lib1.dart' hide a;"));
    addSource("/lib1.dart", createSource(//
        "library lib1;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedIdentifier_show() throws Exception {
    Source source = addSource(createSource(//
        "library L;",
        "export 'lib1.dart' show a;"));
    addSource("/lib1.dart", createSource(//
        "library lib1;"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedMethod_functionExpression_callMethod() throws Exception {
    Source source = addSource(createSource(//
        "main() {",
        "  (() => null).call();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    // A call to verify(source) fails as '.call()' isn't resolved.
  }

  public void test_undefinedMethod_functionExpression_directCall() throws Exception {
    Source source = addSource(createSource(//
        "main() {",
        "  (() => null)();",
        "}"));
    resolve(source);
    assertNoErrors(source);
    // A call to verify(source) fails as '(() => null)()' isn't resolved.
  }

  public void test_undefinedOperator_index() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  operator [](a) {}",
        "  operator []=(a, b) {}",
        "}",
        "f(A a) {",
        "  a[0];",
        "  a[0] = 1;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedOperator_tilde() throws Exception {
    Source source = addSource(createSource(//
        "const A = 3;",
        "const B = ~((1 << A) - 1);"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedSetter_importWithPrefix() throws Exception {
    addSource("/lib.dart", createSource(//
        "library lib;",
        "set y(int value) {}"));
    Source source = addSource(createSource(//
        "import 'lib.dart' as x;",
        "main() {",
        "  x.y = 0;",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedSuperMethod_field() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  var m;",
        "}",
        "class B extends A {",
        "  f() {",
        "    super.m();",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_undefinedSuperMethod_method() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  m() {}",
        "}",
        "class B extends A {",
        "  f() {",
        "    super.m();",
        "  }",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_wrongNumberOfParametersForOperator_index() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  operator []=(a, b) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  public void test_wrongNumberOfParametersForOperator_minus() throws Exception {
    check_wrongNumberOfParametersForOperator("-", "");
    check_wrongNumberOfParametersForOperator("-", "a");
  }

  public void test_wrongNumberOfParametersForOperator1() throws Exception {
    check_wrongNumberOfParametersForOperator1("<");
    check_wrongNumberOfParametersForOperator1(">");
    check_wrongNumberOfParametersForOperator1("<=");
    check_wrongNumberOfParametersForOperator1(">=");
    check_wrongNumberOfParametersForOperator1("+");
    check_wrongNumberOfParametersForOperator1("/");
    check_wrongNumberOfParametersForOperator1("~/");
    check_wrongNumberOfParametersForOperator1("*");
    check_wrongNumberOfParametersForOperator1("%");
    check_wrongNumberOfParametersForOperator1("|");
    check_wrongNumberOfParametersForOperator1("^");
    check_wrongNumberOfParametersForOperator1("&");
    check_wrongNumberOfParametersForOperator1("<<");
    check_wrongNumberOfParametersForOperator1(">>");
    check_wrongNumberOfParametersForOperator1("[]");
  }

  public void test_wrongNumberOfParametersForSetter() throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  set x(a) {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
  }

  private void check_wrongNumberOfParametersForOperator(String name, String parameters)
      throws Exception {
    Source source = addSource(createSource(//
        "class A {",
        "  operator " + name + "(" + parameters + ") {}",
        "}"));
    resolve(source);
    assertNoErrors(source);
    verify(source);
    reset();
  }

  private void check_wrongNumberOfParametersForOperator1(String name) throws Exception {
    check_wrongNumberOfParametersForOperator(name, "a");
  }
}
