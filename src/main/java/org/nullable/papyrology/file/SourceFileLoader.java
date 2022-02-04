package org.nullable.papyrology.file;

/** A utility for creating {@link SourceFile SourceFiles} from basic {@code String} paths. */
public interface SourceFileLoader {

  /**
   * Loads a {@link SourceFile} that has the given {@code identifier}.
   *
   * @throws SourceFileLoadException if the {@code SourceFile} is unable to be loaded for any number
   *     of reasons (e.g. the file doesn't exist).
   */
  SourceFile load(String identifier);
}
