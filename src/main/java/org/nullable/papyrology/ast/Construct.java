package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.source.SourceReference;

/** The construct in the Papyrus language. */
@Immutable
public interface Construct {

  /** Returns the {@link SourceReference} for this construct. */
  SourceReference getSourceReference();
}
