package pethotel.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pethotel.dto.ApplyDto;
import pethotel.dto.CompanyDto;
import pethotel.dto.ConsultingDto;
import pethotel.dto.ReviewDto;
import pethotel.dto.StarDto;
import pethotel.mapper.PetHotelMapper;

@Service
public class PetHotelServiceImpl implements PetHotelService {
	@Autowired
	private PetHotelMapper petHotelMapper;
	
	@Value("${application.upload-path}")
	private String uploadPath;
	

	@Override
	public void insertconsulting(ConsultingDto consultingDto) throws Exception {
		petHotelMapper.insertconsulting(consultingDto);
	}

//	@Override
//	public void insertreply(int consultingIdx) throws Exception {
//		petHotelMapper.insertreply(consultingIdx);
//	}

	

	@Override
	public List<ConsultingDto> selectConsultingList(int offset) throws Exception {
		return petHotelMapper.selectConsultingList(offset);
	}
	@Override
	public int selectConsultingListCount() throws Exception {
		return petHotelMapper.selectConsultingListCount();
	}
	


	@Override
	public void insertreply(ConsultingDto consultingDto) throws Exception {
	
		petHotelMapper.insertreply(consultingDto);
		
	}
	
	@Override
	public ConsultingDto detail(int consultingIdx) throws Exception {
	
		return petHotelMapper.detail(consultingIdx);
	}

	//업체등록버튼
	@Override
	public void insertcompany(MultipartFile file,CompanyDto companydto) throws Exception {
		String savedFilePath = saveFile(file);
		companydto.setCompanyPhoto(savedFilePath);
		petHotelMapper.insertcompany(companydto);
		
	}
	@Override
	public String saveFile(MultipartFile file) throws Exception {
		//동일파일 이름 저장되는 것 방지 
		String savedFilePath = uploadPath + UUID.randomUUID().toString() + "_"+ file.getOriginalFilename();
		File uploadFile = new File(savedFilePath);
		file.transferTo(uploadFile);
		
		return savedFilePath;
	}

	@Override
	public List<CompanyDto> companylist(int offset) throws Exception {
		return petHotelMapper.companylist(offset);
	}

	@Override
	public CompanyDto onecompany(int companyIdx) throws Exception {
		
		return petHotelMapper.onecompany(companyIdx);
	}
//디테일보는거
	@Override
	public CompanyDto companydetail(int companyIdx) {
		
		return petHotelMapper.companydetail(companyIdx);
	}

	@Override
	public List<ReviewDto>reviewlist2(int companyIdx) {
		// TODO Auto-generated method stub
		return petHotelMapper.reviewlist2(companyIdx);
	}

	@Override
	public CompanyDto reviewlist1(int companyIdx) {
		// TODO Auto-generated method stub
		return petHotelMapper.reviewlist1(companyIdx);
	}

	@Override
	public void insertreview(ReviewDto reviewDto) {
		petHotelMapper.insertreview(reviewDto);
		
	}

	//예약등록---------------------------------------------------
	@Override
	public void insertapply(ApplyDto applyDto) throws Exception {
		petHotelMapper.insertapply(applyDto);
	}
	//예약상세
	@Override
	public ApplyDto reservation(int applyIdx) throws Exception {
		return petHotelMapper.reservation(applyIdx);
	}
	@Override
	public List<ApplyDto> applylist(int offset,int companyIdx) throws Exception {

		Map<String, Object> map = new HashMap<>();
		map.put("offset", offset);
		map.put("companyIdx", companyIdx);

		return petHotelMapper.applylist(map);
	}
	@Override
	public int selectApplyListCount() throws Exception {
		return petHotelMapper.selectApplyListCount();
	}

	@Override
	public CompanyDto displayinsert(int companyIdx) {
		
		return petHotelMapper.displayinsert(companyIdx);
	}
//페이징

	@Override
	public int selectBoardListCount() throws Exception {
	
		return petHotelMapper.selectBoardListCount();
	}

	@Override
	public List<StarDto> star() throws Exception {

		return petHotelMapper.star();
	}

	@Override
	public ReviewDto averageStar(int companyIdx) {
		// TODO Auto-generated method stub
		if (petHotelMapper.averageStar(companyIdx) == null) {
			return new ReviewDto();
		} else {
			return petHotelMapper.averageStar(companyIdx);
		}
	}

	@Override
	public void insertConsulting(ConsultingDto consultingDto) throws Exception {
		petHotelMapper.insertConsulting(consultingDto);
		
	}

	@Override
	public int selectReviewlistCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ReviewDto> reviewlist2Paging(int offset, int companyIdx) {
		Map<String, Object> param = new HashMap<>();
		param.put("offset", offset);
		param.put("companyIdx", companyIdx);
		return petHotelMapper.reviewlist2Paging(param);
	}

	@Override
	public int reviewCount(int companyIdx) {
		return petHotelMapper.reviewCount(companyIdx);
	}

}

// 단일 작업을 수행하는 경우에는 상관 없으나
// 동시에 여러가지 일, 한번에 두 가지 이상의 작업을 처리해야하는 경우엔 Transaction을 사용하는 편이 맞음 

// throws Exception - 예외가 발생할 수 있으면 적어야 함. 해당 메서드가 예외 발생할 수 있다 미리 고지하는 느낌? 
// - 실제로 발생할 가능성이 높은 예외타입만 명시하고 남발은 금물

// -개선해야할 부분 
// 도메인 별 로 Mapper 분리 - 개선점
//(INSERT/UPDATE/DELETE 같은..) insertconsulting, insertreply, insertcompany, insertreview, insertapply, insertConsulting
// 위와 같은 경우에는 트랜잭션 필요. / 조회하는 부분에 대해서는  필요 x - @Transaction(readOnly = true)로 읽기전용 설정해줌
// 요청/호출하는 사람이 선택해야할 경우, 도메인 의미를 가진 경우
