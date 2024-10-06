package fvd.archi.clean;

import fvd.archi.clean.technical.UseCasesComponents;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
  classes = UseCasesComponents.class))
public class CleanArchitectureApplication {
  public static void main(String[] args) {
    SpringApplication.run(CleanArchitectureApplication.class);
  }
}