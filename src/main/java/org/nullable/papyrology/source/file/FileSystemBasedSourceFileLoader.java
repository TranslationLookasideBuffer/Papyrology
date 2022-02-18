package org.nullable.papyrology.source.file;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Locale;
import java.util.stream.Stream;

/** A {@link SourceFileLoader} that is backed by a {@link FileSystem}. */
public final class FileSystemBasedSourceFileLoader implements SourceFileLoader {

  private final ImmutableMap<String, Path> identifiersToPaths;

  private FileSystemBasedSourceFileLoader(ImmutableMap<String, Path> identifiersToPaths) {
    this.identifiersToPaths = identifiersToPaths;
  }

  /**
   * Returns a new {@code FileSystemBasedSourceFileLoader} based on the given {@link FileSystem} and
   * {@code directory}.
   *
   * <p>This method will walk the directory (non-recursively) provided and look for all .psc files
   * (without loading their content).
   */
  public static FileSystemBasedSourceFileLoader create(FileSystem fileSystem, String directory) {
    PathMatcher sourceFileMatcher = fileSystem.getPathMatcher(PATH_MATCHER_PATTERN);
    ImmutableMap<String, Path> identifiersToPaths;
    try (Stream<Path> paths = Files.walk(fileSystem.getPath(directory), /* maxDepth= */ 1)) {
      identifiersToPaths =
          paths
              .filter(Files::isRegularFile)
              .filter(sourceFileMatcher::matches)
              .collect(toImmutableMap(p -> toIdentifier(p), p -> p));
    } catch (IOException e) {
      throw new SourceFileLoadException(e, "Failed to enumerate .psc files in \"%s\"", directory);
    }
    return new FileSystemBasedSourceFileLoader(identifiersToPaths);
  }

  private static final String PATH_MATCHER_PATTERN = "glob:**.psc";

  private static String toIdentifier(Path path) {
    String fileName = path.getFileName().toString();
    // All file names are guaranteed to end in ".psc" due to the filter above.
    return fileName.substring(0, fileName.length() - 4).toUpperCase(Locale.US);
  }

  @Override
  public final SourceFile load(String identifier) {
    Path path = identifiersToPaths.get(identifier.toUpperCase(Locale.US));
    if (path == null) {
      throw new SourceFileLoadException("Could not locate a script with name \"%s\"", identifier);
    }
    try {
      return SourceFile.builder().setPath(path).setContent(Files.readString(path)).build();
    } catch (IOException e) {
      throw new SourceFileLoadException(e, "Failed to load script at \"%s\"", path);
    }
  }
}
