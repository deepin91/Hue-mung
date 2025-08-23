package pethotel.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import pethotel.interceptor.LoggerInterceptor;
import pethotel.interceptor.LoginCheckInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new LoggerInterceptor());
//		registry.addInterceptor(new LoginCheckInterceptor());
//	}
    
    @Override  //08/23
    public void addInterceptors(InterceptorRegistry registry) {
      // 1) 요청 로거: 전역, 정적/에러는 제외
      registry.addInterceptor(new LoggerInterceptor())
              .order(1)
              .addPathPatterns("/**")
              .excludePathPatterns(
                  "/css/**", "/js/**", "/images/**", "/webjars/**",
                  "/favicon.ico", "/error");

      // 2) 로그인 체크: 보호가 필요한 경로만, 공개 경로는 제외
      registry.addInterceptor(loginCheckInterceptor())  // 주입 권장
              .order(2)
              .addPathPatterns("/**")
              .excludePathPatterns(
                  "/", "/login", "/logout", "/signup",
                  "/companies/**", "/reviews/**", // 공개 읽기 API면 제외
                  "/css/**", "/js/**", "/images/**", "/webjars/**",
                  "/favicon.ico", "/error");
    }
	
	

	// 의존성 주입이 필요하면 @Bean으로 등록해서 주입 받기
	  @Bean
	  public LoginCheckInterceptor loginCheckInterceptor() { 
	    return new LoginCheckInterceptor(); 
	  }
	
}

// Interceptor 구현클래스에서 재정의한 메서드가 실제 요청 시 실행될 수 있도록 스프링 mvc에 등록하는 설정 클래스 
// WebMvcConfigurer(인터페이스)를 implements 해서 addInterceptors 메서드를 오버라이드하고,
// InterceptorRegistry에 addInterceptor(...)로 붙이면 됨

// 이곳에 등록된 순서에 따라 실제 동작 시 영향을 미치므로 순서 주의
// 보통 요청이 들어오면 > 로그를 찍고 > 로그인/권한 검증 > 컨트롤러 실행 순

// 대게 Interceptor 구현체 파일만들어 작동시킬 내용 메서드 정의하고 현 파일에서 해당 메서드를 등록 하는 개념
// ㄴ그리고 Configuration은 보통 크게 WebMvcConfiguration/SecurityConfig/DataSourceConfig 파일로 나눠 설정함
// 프로젝트가 작거나 혼자 하는 경우 WebMvcConfiguration 파일 하나에 CorsConfig,SecurityConfig,TransactionConfig 관련 설정을 전부 다 넣어도 가능하지만
// 협업 시 설정 성격별로 분리해주는게 베스트임.