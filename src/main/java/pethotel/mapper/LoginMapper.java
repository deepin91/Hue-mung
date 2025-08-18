package pethotel.mapper;

import org.apache.ibatis.annotations.Mapper;


import pethotel.dto.LoginDto;
import pethotel.dto.UserDto;

@Mapper
public interface LoginMapper {
	public UserDto login(LoginDto loginDto) throws Exception;
	// 회원가입 요청
	void memberRegistration(UserDto userDto) throws Exception;
	// 회원가입 시 아이디 중복체크
	int countBySameUserId(String userId)throws Exception;
}
