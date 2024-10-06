package fvd.archi.clean;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication {
  private static int context_count = 0;
  private static final int max_context_count = 1;

  // Can be used to control number of test context build for test in different modules
  // test context creation are the bottleneck of test performance
  public TestApplication() {
    context_count++;
    if (context_count > max_context_count) {
      throw new RuntimeException(String.format(
        "Number of authorized test context exceeded. Only %d test context are authorized.",
        max_context_count
      ));
    }
  }
}
