package pethotel.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pethotel.dto.LoginDto;
import pethotel.dto.UserDto;
import pethotel.service.LoginService;

@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;

	// 로그인 화면 요청
	@GetMapping("/login.do")
	public String login(HttpSession session) throws Exception {
		if (session.getAttribute("users") == null) {
			return "login.html";
		} else {
			return "redirect:/mainpage";
		}
	}

	// 일치하는 사용자가 존재하지 않는 경우 메시지와 함께 로그인 페이지로,
	// 일치하는 사용자가 존재하는 경우 게시판 페이지로 이동합니다.
	// 로그인 처리 요청
	@PostMapping("/login.do")
	public String login(LoginDto loginDto, HttpSession session) throws Exception {
		UserDto userDto = loginService.login(loginDto);
		if (userDto == null) {
			session.setAttribute("message", "일치하는 정보가 존재하지 않습니다.");
			return "redirect:login.do";
		} else {
			session.setAttribute("users", userDto);
			return "redirect:/mainpage";
		}
	}

	// 로그아웃 처리
	// 세션에 user를 삭제하고, 세션을 무효화한 후 로그인 페이지로 이동합니다.
	@GetMapping("/logout.do")
	public String logout(HttpSession session) throws Exception {
		session.removeAttribute("users");
		session.invalidate();
		return "redirect:/login.do";
	}

	// 회원가입 기능 추가
	// 회원가입 화면 요청
	@GetMapping("/signup.do")
	public String signup() throws Exception {
		return "signup.html";
	}

	@ResponseBody
	@GetMapping("/userIdCheck")
	public boolean userIdCheck(@RequestParam("userId") String userId)throws Exception {
		return !loginService.isUserIdExists(userId); //true면 사용가능하도록 
	}
	
	// 회원 가입 처리 요청
	@PostMapping("/memberRegistration.do")
	public String memberRegistration(UserDto userDto) throws Exception{
		loginService.memberRegistration(userDto);
		return "redirect:/login.do";
	}
}
