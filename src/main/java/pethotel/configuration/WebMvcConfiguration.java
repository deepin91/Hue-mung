package pethotel.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import pethotel.interceptor.LoggerInterceptor;
import pethotel.interceptor.LoginCheckInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor());
		registry.addInterceptor(new LoginCheckInterceptor());
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