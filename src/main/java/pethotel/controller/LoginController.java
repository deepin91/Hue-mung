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
		UserDto userDto = loginService.login(loginDto); //1. LoginServiceImpl의 return loginMapper.login(loginDto);로 타진 후 다시 돌아와 if문을 탐  
		if (userDto == null) {
			session.setAttribute("message", "일치하는 정보가 존재하지 않습니다.");
			return "redirect:login.do";
		} else {
			session.setAttribute("users", userDto); //3 . 일치하는 정보로 로그인했으므로 if문 빠져서 else문 탐 - users라는 이름으로 userDto객체를 세션에 저장
			// ㄴ사용자가 로그인에 성공했을 때, 그 사용자의 정보가 담긴 userDto 객체를 users라는 이름으로 서버에 저장하여, 사용자가 웹사이트를 탐색하는 동안 이 정보를 계속 사용할 수 있도록 하는 코드
			return "redirect:/mainpage"; // --08/26
		}
	}

	// 로그아웃 처리
	// 세션에 user를 삭제하고, 세션을 무효화한 후 로그인 페이지로 이동합니다.
	@GetMapping("/logout.do")
	public String logout(HttpSession session) throws Exception {
		session.removeAttribute("users"); //로그아웃 시 세션에서 users라는 이름의 (userDto객체가 담긴) 것을 제거
		session.invalidate(); //세선 무효화하는 메서드 - 사용자가 로그아웃하거나, 세션 만료 시간이 지났을 때 세션을 종료하고 세션에 저장된 모든 데이터를 삭제 removeAttribute / invalidate()둘 다 라이브러리 탑재된 메서드인듯
		return "redirect:/login.do"; //메서드 수행 후 로그인 페이지로 이동시킴 --08/26
	}

	// 회원가입 기능 추가
	// 회원가입 화면 요청
	@GetMapping("/signup.do")
	public String signup() throws Exception {
		return "signup.html"; //회원가입폼 불러오고
	}

	// 회원가입 시 중복 체크
	@ResponseBody
	@GetMapping("/userIdCheck") // -- 08/26 데이터 플로우 쳌
	public boolean userIdCheck(@RequestParam("userId") String userId)throws Exception {// 회원가입 시 아이디 중복을 확인하는 기능을 구현한 메서드
		return !loginService.isUserIdExists(userId); //true면 사용가능하도록 ->loginService로 이동
	}// loginService.isUserIdExists(userId)의 반환값을 반전시킴 - 반전시킨 값 리턴함.
	/*loginService 객체의 isUserIdExists 메서드를 호출하며, 사용자가 입력한 userId를 전달 > 이 메서드는 데이터베이스를 조회해 해당 아이디가 이미 존재하는지(true), **존재하지 않는지(false)**를 확인 */
	
	
	// 회원 가입 처리 요청
	@PostMapping("/memberRegistration.do")
	public String memberRegistration(UserDto userDto) throws Exception{ // --08/26
		loginService.memberRegistration(userDto);// ->serviceImpl의 memberRegistration 메서드 탐 -loginMapper.memberRegistration(userDto);
		return "redirect:/login.do"; //serviceImpl파일에서 Mapper로 전달 후 이 줄로 돌아옴. > 회원가입 완료 후 로그인 할 수 있도록 로그인페이지 띄움
	}
}
