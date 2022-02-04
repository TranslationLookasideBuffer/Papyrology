package org.nullable.papyrology.file;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FileSystemBasedSourceFileLoaderTest {

  @Parameters(name = "{0}")
  public static Configuration[] data() {
    return new Configuration[] {Configuration.unix(), Configuration.osX(), Configuration.windows()};
  }

  private static final String SCRIPT_ID = "Form";
  private static final String SCRIPT_CONTENT = "ScriptName " + SCRIPT_ID;

  @Parameter(0)
  public Configuration config;

  private FileSystemBasedSourceFileLoader loader;
  private FileSystem fs;
  private Path directory;
  private Path scriptPath;

  @Before
  public void setUp() throws IOException {
    fs = Jimfs.newFileSystem(config);
    directory = fs.getPath("test");
    Files.createDirectory(directory);
    // Create a script in the directory we're loading from.
    scriptPath = directory.resolve(SCRIPT_ID + ".psc");
    Files.writeString(scriptPath, SCRIPT_CONTENT, StandardCharsets.UTF_8);
    // Create a non-script file in the directory we're loading from (that is ignored).
    Path txtPath = directory.resolve(SCRIPT_ID + ".txt");
    Files.writeString(txtPath, "You shouldn't read me!", StandardCharsets.UTF_8);
    // Create a subdirectory and file in the directory we're loading from (that are ignored).
    Path subdirectory = directory.resolve("ignored");
    Files.createDirectory(subdirectory);
    Path ignoredScriptPath = subdirectory.resolve(SCRIPT_ID + ".psc");
    Files.writeString(ignoredScriptPath, "An ignored script file.", StandardCharsets.UTF_8);

    loader = FileSystemBasedSourceFileLoader.create(fs, "test");
  }

  @Test
  public void exactFileName_loaded() {
    SourceFile expected =
        SourceFile.builder().setPath(scriptPath).setContent(SCRIPT_CONTENT).build();

    SourceFile actual = loader.load(SCRIPT_ID);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void alteredFileName_loaded() {
    SourceFile expected =
        SourceFile.builder().setPath(scriptPath).setContent(SCRIPT_CONTENT).build();

    SourceFile actual = loader.load(SCRIPT_ID.toLowerCase(Locale.US));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void unknownFileName_exception() {
    SourceFileLoadException exception =
        assertThrows(SourceFileLoadException.class, () -> loader.load("unknown"));

    assertThat(exception).hasMessageThat().contains("Could not locate");
  }
}
