/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.code.tmpl;

/**
 * TODO
 *
 * @since 0.4
 */
public interface Include
    extends
    ArgsPart, BlockElement, BodyElement, ExpressionPart, ParameterElement, VariableInitializer,

    ClassDeclarationInstruction,
    EnumDeclarationInstruction,
    FieldDeclarationInstruction,
    InterfaceDeclarationInstruction,
    MethodDeclarationInstruction {}