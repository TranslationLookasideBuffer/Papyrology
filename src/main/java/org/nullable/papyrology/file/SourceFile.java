package org.nullable.papyrology.file;

import com.google.auto.value.AutoValue;
import java.nio.file.Path;

/** An immutable container for a Papyrus source file. */
@AutoValue
public abstract class SourceFile {

  /**
   * Returns whether or not this {@code SourceFile} is a supplemental file to be used in support of
   * compiling other {@code SourceFiles} (e.g. by static analysis).
   *
   * <p>When this method returns {@code true}, this file should not result in compiled output.
   */
  abstract boolean isSupplemental();

  /** Returns the {@code Path} of this {@code SourceFile}. */
  abstract Path getPath();

  /** Returns the file name of this {@code SourceFile}. */
  public final String getFileName() {
    return getPath().toString();
  }

  /** Returns the text content of this {@code SourceFile}. */
  public abstract String getContent();

  /** Returns a fresh {@code SourceFile} builder. */
  static Builder builder() {
    return new AutoValue_SourceFile.Builder();
  }

  /** A builder of {@code SourceFiles}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSupplemental(boolean isSupplemental);

    abstract Builder setPath(Path path);

    abstract Builder setContent(String content);

    abstract SourceFile build();
  }
}
