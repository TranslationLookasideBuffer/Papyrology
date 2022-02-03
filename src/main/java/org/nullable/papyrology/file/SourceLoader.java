package org.nullable.papyrology.file;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/** A utility for creating {@link SourceFile SourceFiles} from basic {@code String} paths. */
public final class SourceLoader {

  private final FileSystem fileSystem;
  private final PathMatcher sourceFileMatcher;

  private SourceLoader(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
    this.sourceFileMatcher = fileSystem.getPathMatcher(PATH_MATCHER);
  }

  private static final String PATH_MATCHER = "glob:*.psc";

  /** Returns a new {@code SourceLoader} based on the given {@link FileSystem}. */
  public static SourceLoader create(FileSystem fileSystem) {
    return new SourceLoader(fileSystem);
  }

  /**
   * Loads all Papyrus source files at the given paths as {@link SourceFile SourceFiles}.
   *
   * <p>If the path corresponds to a directory, the directory is walked recursively and all Papyrus
   * source files found are loaded.
   */
  public ImmutableList<SourceFile> loadSourceFiles(Iterable<String> rawSourcePaths)
      throws IOException {
    return loadSourceFiles(rawSourcePaths, /* isSupplemental= */ false);
  }

  /**
   * Loads all Papyrus source files at the given paths as <i>supplemental</i> {@link SourceFile
   * SourceFiles}; see {@link SourceFile#isSupplemental()}.
   *
   * <p>If the path corresponds to a directory, the directory is walked recursively and all Papyrus
   * source files found are loaded.
   */
  public ImmutableList<SourceFile> loadSupplementalSourceFiles(Iterable<String> rawSourcePaths)
      throws IOException {
    return loadSourceFiles(rawSourcePaths, /* isSupplemental= */ true);
  }

  private ImmutableList<SourceFile> loadSourceFiles(
      Iterable<String> rawSourcePaths, boolean isSupplemental) throws IOException {
    PathMatcher sourceFileMatcher = fileSystem.getPathMatcher(PATH_MATCHER);
    List<Path> sourcePaths = new ArrayList<>();
    for (String rawSourcePath : rawSourcePaths) {
      Path path = fileSystem.getPath(rawSourcePath);
      if (Files.isRegularFile(path)) {
        checkArgument(
            sourceFileMatcher.matches(path),
            "Path \"%s\" corresponds to a file that does not have the expected \".psc\" extension.",
            path);
        sourcePaths.add(path);
      } else if (Files.isDirectory(path)) {
        try (Stream<Path> paths = Files.walk(path)) {
          paths
              .filter(Files::isRegularFile)
              .filter(sourceFileMatcher::matches)
              .forEach(sourcePaths::add);
        }
      } else {
        throw new IOException(
            String.format("Path \"%s\" could not be read as a regular file or a directory.", path));
      }
    }
    ImmutableList.Builder<SourceFile> sourceFiles = ImmutableList.builder();
    for (Path sourcePath : sourcePaths) {
      sourceFiles.add(loadSourceFile(sourcePath, isSupplemental));
    }
    return sourceFiles.build();
  }

  private SourceFile loadSourceFile(Path path, boolean isSupplemental) throws IOException {
    return SourceFile.builder()
        .setSupplemental(isSupplemental)
        .setPath(path)
        .setContent(Files.readString(path))
        .build();
  }
}
