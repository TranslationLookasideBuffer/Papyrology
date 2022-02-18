package org.nullable.papyrology.source.file;

import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;

/** Identifies that a {@link SourceFileLoader} was unable to load a {@link SourceFile}. */
public final class SourceFileLoadException extends RuntimeException {

  @FormatMethod
  public SourceFileLoadException(@FormatString String message, Object... args) {
    super(String.format(message, args));
  }

  @FormatMethod
  public SourceFileLoadException(Throwable cause, @FormatString String message, Object... args) {
    super(String.format(message, args), cause);
  }
}
