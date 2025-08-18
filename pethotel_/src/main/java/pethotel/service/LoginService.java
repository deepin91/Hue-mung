package pethotel.service;

import org.springframework.stereotype.Service;

import pethotel.dto.LoginDto;
import pethotel.dto.UserDto;

@Service
public interface LoginService {
	public UserDto login(LoginDto loginDto) throws Exception;
	// 회원가입
	public void memberRegistration(UserDto userDto) throws Exception;
	public boolean isUserIdExists(String userId)throws Exception;
}
