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

	

	@Override // --> DB에서 컨설팅목록 조회하는 것
	public List<ConsultingDto> selectConsultingList(int offset) throws Exception {
		return petHotelMapper.selectConsultingList(offset); // 3 --08/30 --값들 반환/출력
		/* ↑ 실제 데이터베이스 조회 로직을 담당하는 부분 
		 * - 매퍼 객체의 메서드를 호출하며, 전달받은 offset 값을 넘겨줌 
		 * - offset**은 데이터베이스 쿼리에서 가져올 데이터의 시작 위치를 지정하는 데 사용됨 
		 */
	} // 7
	
	@Override
	public int selectConsultingListCount() throws Exception {
		return petHotelMapper.selectConsultingListCount(); // 10 --08/30 -- 반환
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
		
		return petHotelMapper.onecompany(companyIdx); // 2.
	}
	
//업체 상세페이지
	@Override
	public CompanyDto companydetail(int companyIdx) {
		return petHotelMapper.companydetail(companyIdx); // 4
	}
	// 업체 상세페이지 볼 때 URL이 http://localhost:8080/companyDetail.do?companyIdx=3 이런식으로 떠서
	// 저렇게 보여지면 인덱스 넘버로 다른 업체정보에 접근 가능하니까 미관상 + 보완적 측면 고려해서 바꾸는 게 좋을 듯 /companies/AnyMalls 이런식으로 업체명 뜨게
	// 1. DB에 company 테이블에 slug 컬럼 추가해 URL에 사용될 업체명을 저장, 설정
	// 2. 업체 등록 로직 수정 (slug 생성 및 저장) - 새 업체가 등록될 때 companyName을 기반으로 slug생성해서 DB에 함게 저장하도록 로직 수정해야함
	// 슬러그는 URL에 적합하게 공백과 특수문자를 제거하고 소문자로 변환하는 과정이 필요
	// 3. **URL 라우팅 및 조회 로직 변경 (슬러그로 데이터 찾기)** (중요) - 기존의 companyDetail.do?companyIdx=3 라우팅을 companyDetail/{slug} 형식으로 변
	// MyBatis방식은 쿼리 수정 -  CompanyMapper.xml 파일 쿼리 수정하고, service, serviceImpl에 메서드 정의 및 비지니스로직 추가
	// html view 템플릿에서 메인페이지 >  상세페이지로 가는 링크 변경하면 끝.
	// +++추후 JPA방식으로 수정 시 다른 부분은 다 똑같고 JPA Repository 인터페이스에 slug 이용해서 DB 조회하는 메서드 추가하면 됨 - DB 조회 로직 추가
	
	@Override
	public List<ReviewDto>reviewlist2(int companyIdx) {
		// TODO Auto-generated method stub
		return petHotelMapper.reviewlist2(companyIdx);
	}

	@Override
	public CompanyDto reviewlist1(int companyIdx) {
		// TODO Auto-generated method stub
		return petHotelMapper.reviewlist1(companyIdx); // 9 --08/30 -- CompanyDto reviewlist1 = petHotelService.reviewlist1(companyIdx); 에서 여기타서옴
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
		return petHotelMapper.star(); //27 -- List<StarDto> starDto = petHotelService.star(); 타고 옴
	}

	@Override
	public ReviewDto averageStar(int companyIdx) {
		// TODO Auto-generated method stub
		if (petHotelMapper.averageStar(companyIdx) == null) { // 31 -- ReviewDto reviewDto = petHotelService.averageStar(companyIdx); 타고 옴 - // averagestar 3.6 반환 
			return new ReviewDto();
		} else {
			return petHotelMapper.averageStar(companyIdx); // 32
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
		Map<String, Object> param = new HashMap<>(); // 13 --08/30 --List<ReviewDto> reviewlist2 = petHotelService.reviewlist2Paging(offset, companyIdx); 이거타고 옴
		param.put("offset", offset); // 14
		param.put("companyIdx", companyIdx); // 15
		return petHotelMapper.reviewlist2Paging(param); // 16 --댓글 내용들 반환 
	}

	@Override
	public int reviewCount(int companyIdx) {
		return petHotelMapper.reviewCount(companyIdx); // 19 --컨트롤러--int totalReviews = petHotelService.reviewCount(companyIdx); 이거 타고 옴
	//  20 ↑ 리뷰 갯수 카운트해서 그 값 리턴 
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
