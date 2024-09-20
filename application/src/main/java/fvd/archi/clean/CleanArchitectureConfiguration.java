package fvd.archi.clean;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

@Configuration
public class CleanArchitectureConfiguration {
  @Bean
  public BeanFactoryPostProcessor beanFactoryPostProcessor(ApplicationContext beanRegistry) {
    return beanFactory -> {
      genericApplicationContext(
        (BeanDefinitionRegistry) ((AnnotationConfigServletWebServerApplicationContext) beanRegistry)
          .getBeanFactory());
    };
  }

  public void genericApplicationContext(BeanDefinitionRegistry beanRegistry) {
    ClassPathBeanDefinitionScanner beanDefinitionScanner = new ClassPathBeanDefinitionScanner(beanRegistry);
    beanDefinitionScanner.addIncludeFilter(removeModelAndEntitiesFilter());
    beanDefinitionScanner.addExcludeFilter(removeApplicationFilter());
    beanDefinitionScanner.addExcludeFilter(removeCommonUser());
    beanDefinitionScanner.scan("fvd.archi.clean");
  }

  static TypeFilter removeModelAndEntitiesFilter() {
    return (MetadataReader mr, MetadataReaderFactory mrf) -> !mr.getClassMetadata()
        .getClassName()
        .endsWith("Model");
  }

  static TypeFilter removeApplicationFilter() {
    return (MetadataReader mr, MetadataReaderFactory mrf) -> mr.getClassMetadata()
      .getClassName()
      .endsWith("Application");
  }

  static TypeFilter removeCommonUser() {
    return (MetadataReader mr, MetadataReaderFactory mrf) -> mr.getClassMetadata()
      .getClassName()
      .endsWith("CommonUser");
  }
}