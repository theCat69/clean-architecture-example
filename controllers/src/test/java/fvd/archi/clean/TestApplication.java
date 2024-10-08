package fvd.archi.clean;

import fvd.archi.clean.technical.UseCasesComponents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@Slf4j
@SpringBootApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
  classes = UseCasesComponents.class))
public class TestApplication {
  private static int context_count = 0;
  private static final int max_context_count = 1;

  // Can be used to control number of test context build for test in different modules
  // test context creation are the bottleneck of test performance
  public TestApplication() {
    context_count++;
    if(context_count > max_context_count) {
      throw new RuntimeException(String.format(
        "Number of authorized test context exceeded. Only %d test context are authorized.",
        max_context_count
      ));
    }
  }

}
