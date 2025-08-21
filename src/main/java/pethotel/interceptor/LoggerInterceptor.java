package pethotel.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception{
		log.debug("-------------- START --------------");
		log.debug("Request URI : " + request.getRequestURI());
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				@Nullable ModelAndView modelAndView) throws Exception{
		log.debug("-------------- END --------------");
	}
}

// HandlerInterceptor은 스프링에서 기본적으로 제공되는 인터페이스(라이브러리에 있음 - 의존성 추가하면 가져와 사용 가능함)
// 인터페이스는 정의가 불가능하므로 implements 적어서 구현 클래스에 오버라이드(재정의) 해야함
// 클래스에 입력한다해서 바로 사용가능x, WebMvcConfigurer를 implements한 설정 클래스에서 등록해야함

// -즉, 검증, 권한 체크 등 이런 처리를 하고싶다면 Interceptor 클래스 파일을 만들어 스프링에서 제공하는 
// 라이브러리 HandlerInterceptor을 가져와 해당 클래스 파일에 재정의
// 그 후, Configurer(WebMvcConfigurer)파일 생성해 등록
// 구현 클래스에서 만든 내용을 Configurer파일에 registry.addInterceptor(new RoleCheckInterceptor())과 같은 방식으로 기입

//Servlet - 자바로 만든 웹 요청/응답을 처리하는 프로그램(웹 요청 처리 기본 단위)
//HttpServletRequest = 요청 정보
//HttpServletResponse = 응답 정보
//Object handler = 컨트롤러 실행 정보

// AOP -  매번 공통적으로 실행해야하는 동작에 대해 사용
// interceptor과 AOP차이점
// ㄴ 매번 실행해야하는 웹 요청(HTTP) 단위 작업 - interceptor (전처리)
// / 매번 실행해야하는 메서드 호출 단위 작업 - AOP(비지니스 로직 호출할 때마다 필요한 작업) 