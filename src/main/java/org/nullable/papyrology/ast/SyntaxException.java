package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;
import org.nullable.papyrology.source.SourceReference;

/** Identifies that there exists a syntax error in the source. */
public final class SyntaxException extends RuntimeException {

  private final SourceReference sourceReference;

  @FormatMethod
  public SyntaxException(
      SourceReference sourceReference, @FormatString String message, Object... args) {
    super(String.format(message, args));
    this.sourceReference = sourceReference;
  }

  /** Returns the {@link SourceReference} that identifies where the syntax error was identified. */
  public SourceReference getSourceReference() {
    return sourceReference;
  }
}
