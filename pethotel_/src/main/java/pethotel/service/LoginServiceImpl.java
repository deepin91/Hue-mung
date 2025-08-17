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
	
	@Override
	public UserDto login(LoginDto loginDto) throws Exception {
		return loginMapper.login(loginDto);
	}

	@Override
	public void memberRegistration(UserDto userDto)throws Exception {
		loginMapper.memberRegistration(userDto);
		
	}

	@Override
	public boolean isUserIdExists(String userId) throws Exception {
		return loginMapper.countBySameUserId(userId) > 0;
	}

}
