package pethotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pethotel.dto.LoginDto;
import pethotel.dto.UserDto;
import pethotel.mapper.LoginMapper;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginMapper loginMapper;
	
	/*UserDTo타입의 login이라는 메서드(LoginDto 타입의 loginDto라는 이름의 객체를 입력받은 = 매개변수) */ // --08/26
	@Override //인터페이스 혹은 부모클래스에 정의된 메서드 재정의(특정 인터페이스/추상클래드 메서드 따라 구현되었다는 의미)
	public UserDto login(LoginDto loginDto) throws Exception {
		return loginMapper.login(loginDto); //2.실제 로직 담긴 부분 - loginMapper객체에 있는 login이라는 메서트 호출하는데 매개변수인 loginDto객체를 함께 전달
	} //loginMapper의 login 메서드가 반환하는 값을 그대로 이 메서드의 최종 반환값으로 사용

	@Override
	public void memberRegistration(UserDto userDto)throws Exception { // --08/26
		loginMapper.memberRegistration(userDto); //void타입이라 반환하진 않음 - loginMapper객체에 있는 memberRegistration메서드를 호출하는데 매개변수인 uesrDto객체를 함께 전달 
		// 회원가입 폼에 입력된 내용을 UserDto타입으로 userDto에 저장했을거..?
		
	}

	@Override // --08/26
	public boolean isUserIdExists(String userId) throws Exception {//논리연산자 .String타입의 userId라는 매개변수를 갖는 isUserIdExists 메서드
		return loginMapper.countBySameUserId(userId) > 0; // loginMapper 객체에 있는 countBySameUserId메서드를 호출하는데 userId객체를 함께전달함 0보다 큰?
		// loginMapper.countBySameUserId(userId)가 반환하는 값이 0보다 큰지(> 0)를 확인헤 사용자 ID의 존재 여부를 true 또는 false로 반환
		/* 이 메서드는 입력받은 userId가 데이터베이스에 이미 존재하는지 확인하는 역할
		아이디가 이미 있다면: countBySameUserId가 1 이상을 반환하므로, > 0은 true가 되고, isUserIdExists 메서드는 최종적으로 true를 반환
		아이디가 없다면: countBySameUserId가 0을 반환하므로, > 0은 false가 되고, isUserIdExists 메서드는 최종적으로 false를 반환 */
	}
}
