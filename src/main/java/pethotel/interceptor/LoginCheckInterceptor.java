package pethotel.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 요청 주소에 /list, /company, /companyDetail, /login.do가 포함된 경우, 세션에 user 정보가 존재하는지 체크
		// 포함되지 않은 경우 메시지와 함께 login 페이지로 이동
		if ((request.getRequestURI().indexOf("/companyDetail")>= 0 ||
				request.getRequestURI().contains("/list") ||
				request.getRequestURI().contains("/company")) 
			&& request.getSession().getAttribute("users") == null) {
			request.getSession().setAttribute("message", "로그인 후 사용하실 수 있습니다.");
			response.sendRedirect("/login.do");
			return false;
		} else {
			return true;
		}
	}
}
//request.getRequestURI().indexOf("/list") >= 0 ||
//request.getRequestURI().indexOf("/company") >= 0 ||

// 나중에 커스텀 어노테이션 패키지 하나 만들어서 실제로 만들어보고 사용(연해보면 좋을 듯