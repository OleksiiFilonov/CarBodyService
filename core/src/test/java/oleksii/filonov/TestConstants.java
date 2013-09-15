package oleksii.filonov;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestConstants {
    public static final Path TARGET_RESOURCE = Paths.get("", "target", "resources");
    public static final Path LINKED_RESULT_FILE = TARGET_RESOURCE.resolve("resultLinks.xlsx");
    public static final Path RESULT_FILE = TARGET_RESOURCE.resolve("result.xlsx");
}
