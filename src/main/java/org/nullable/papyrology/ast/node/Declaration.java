package org.nullable.papyrology.ast.node;

/**
 * Denotes a top-level script element definition.
 *
 * <p>Specifically one of the following:
 *
 * <ul>
 *   <li>{@link Import}
 *   <li>{@link ScriptVariable}
 *   <li>{@link Property}
 *   <li>{@link State}
 *   <li>{@link Function}
 *   <li>{@link Event}
 * </ul>
 */
public interface Declaration extends Construct {}
