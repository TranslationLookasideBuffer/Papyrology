package org.nullable.papyrology.ast.node;

import org.nullable.papyrology.common.SourceReference;

/** The construct in the Papyrus language. */
public interface Construct {

  /** Returns the {@link SourceReference} for this construct. */
  SourceReference getSourceReference();
}
