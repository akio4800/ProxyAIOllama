package ee.carlrobert.codegpt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import com.intellij.DynamicBundle;

public class CodeGPTBundle extends DynamicBundle {

  private static final CodeGPTBundle INSTANCE = new CodeGPTBundle();

  private CodeGPTBundle() {
    super("messages.codegpt");
  }

  public static String get(
      @NotNull @PropertyKey(resourceBundle = "messages.codegpt") String key,
      Object... params) {
    return INSTANCE.getMessage(key, params);
  }
}
