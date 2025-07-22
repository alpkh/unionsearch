// package com.unionsearch.ver2.config;

// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class WebConfig {
//   // WebConfig에서 별도의 CORS 설정은 필요 없음 (SecurityConfig에서 관리)
// }
package com.unionsearch.ver2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:8081") // Vue 서버 주소
        .allowedMethods("GET", "POST", "PUT", "DELETE");
  }
}