package org.nullable.papyrology.file;

import com.google.auto.value.AutoValue;
import java.nio.file.Path;

/** An immutable container for a Papyrus source file. */
@AutoValue
public abstract class SourceFile {

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
    abstract Builder setPath(Path path);

    abstract Builder setContent(String content);

    abstract SourceFile build();
  }
}
