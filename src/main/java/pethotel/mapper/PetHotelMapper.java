package pethotel.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import pethotel.dto.ApplyDto;
import pethotel.dto.CompanyDto;
import pethotel.dto.ConsultingDto;
import pethotel.dto.ReviewDto;
import pethotel.dto.StarDto;
@Mapper
public interface PetHotelMapper {


	List<StarDto> star();
	//	List<ConsultingDto> insertreply(int consultingIdx) throws Exception;
	void insertconsulting(ConsultingDto consultingDto);
	void insertreply(ConsultingDto consultingDto)throws Exception;

	void insertConsulting(ConsultingDto consultingDto)throws Exception;
	
	List<ConsultingDto> selectConsultingList(int offset) throws Exception;
	
	ConsultingDto detail(int consultingIdx) throws Exception;
	//사진 등록 파트
	void insertcompany(CompanyDto companyDto)throws Exception;
	List<CompanyDto> companylist(int offset)throws Exception;
	CompanyDto onecompany(int companyIdx) throws Exception;

	int selectConsultingListCount() throws Exception;
	
	//디테일
	CompanyDto companydetail(int companyIdx);
	List<ReviewDto> reviewlist2(int companyIdx);
	CompanyDto reviewlist1(int companyIdx);
	void insertreview(ReviewDto reviewDto);
	//예약확인
	void insertapply(ApplyDto applyDto) throws Exception;
	
	ApplyDto reservation(int applyIdx) throws Exception;
	List<ApplyDto> applylist(Map<String, Object> map) throws Exception;
	int selectApplyListCount() throws Exception;
	CompanyDto displayinsert(int companyIdx);
	//페이징
	int selectBoardListCount() throws Exception;
	ReviewDto averageStar(int companyIdx);
	List<ReviewDto> reviewlist2Paging(Map<String, Object> param);
	int reviewCount(int companyIdx);
}
// - 개선안
// 하나의 Mapper파일에 다 몰려있으면 충돌가능성이 높고, 테스트하기에 효율,용이하기때문에 도메인별로 Mapper 파일 나눠서 설정
// +XML파일 쿼리 파일도 Mapper파일에 맞게 나눠서 재설정
// XML파일 에서는 MyBatis가 인터페이스 메서드와 XML 쿼리를 매칭할 때 namespace + id 조합을 기준으로 찾기 때문에 namespace = 반드시 인터페이스 FQCN과 같아야 함

// namespace = <mapper> 태그 안에서 설정하는 값
// FQCN (Fully Qualified Class Name) = "패키지명 + 클래스명" 전체 경로
// 즉, mapper.xml의 <mapper namespace="..."> 값은 반드시 매핑 대상 인터페이스의 FQCN과 같아야 한다는 뜻