package pethotel.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import pethotel.dto.ApplyDto;
import pethotel.dto.CompanyDto;
import pethotel.dto.ConsultingDto;
import pethotel.dto.ReviewDto;
import pethotel.dto.StarDto;
import pethotel.dto.UserDto;
import pethotel.service.PetHotelService;

@Slf4j
@Controller
public class PetHotelController {
	@Autowired
	private PetHotelService petHotelService;

	// --------------------------qna등록페이지-----------------------
	@GetMapping("/detail.register")
	public ModelAndView consulting() throws Exception {
		ModelAndView mv = new ModelAndView("Business_QnA_regist.html");
		return mv;
	}

//	@PostMapping("/register/12")
//	public String insertConsulting(ConsultingDto consultingDto) throws Exception {
//
//		petHotelService.insertconsulting(consultingDto);
//		return ("redirect:/list");
//	}

	@PostMapping("/registerConsulting.do")
	public String insertConsulting(HttpSession session, ConsultingDto consultingDto) throws Exception {
		UserDto loginUser = (UserDto) session.getAttribute("users");

		consultingDto.setCUserId(loginUser.getUserId());
		consultingDto.setConsultingCreatedt(LocalDateTime.now());
		petHotelService.insertConsulting(consultingDto);
		return "redirect:/list";

	}

	/* ----------------------qna상세 페이지--------------------------------- */ 
	@GetMapping("/detail.openconsultDetail.do") // 여기서도 URL에 인덱스 뜨는거 고쳐야함
	public ModelAndView detail(@RequestParam int consultingIdx) throws Exception { // URL 파라미터로 consultingIdx를 받아 >
																					// service 메서드-
																					// petHotelService.detail(consultingIdx)
																					// 호출 > 결과값 consultingDtoㄹ,ㄹ View에
																					// 전달
		// data flow check breakpoint set(메서드 첫 줄에)-08/24
		ConsultingDto consultingDto = petHotelService.detail(consultingIdx); // 1. Q&A 게시글 상세페이지 메서드 요청 - consultingIdx
																				// 받아서 service 호출
		ModelAndView mv = new ModelAndView("Business_answer_content.html"); // 2. "Business_answer_content.html"이라는 뷰
																			// 이름을 지정해 ModelAndView 객체 mv를 생성

		mv.addObject("detail", consultingDto); // 3.view 파일에 detail이라는 속성의 이름으로(=key로) consultingDto =값(value)을 담은 객체를
												// view로 전달
//      mv.addObject("키(key)", 값(value); - 사용 방식
		return mv;
	}
	/* **개선점** */
	/* Spring Security의 @PreAuthorize("hasRole('ADMIN')") 등으로 역할 검증을 하는 걸 권장. 인터셉터는 인증/로깅 위주, 권한은 시큐리티가 더 표준적이라함 */
	// + 관리자용 @PostMapping 컨트롤러 메서드 하나 새로 만드는 게 더 보편적, 직관적 인듯
	// 하단 댓글 작성할 수 있는 사람 - 관리자 권한 보유한 사람 으로 권한제어 필요함.
	@PostMapping("/reply/1234") // --> 여기 url도 좀 더 직관적이게 ex)QNAReply_admin라던지 그런 거로 바꿔야 함. 다른 댓글 url과 중복되어 보일 수도
	// interceptor에 설정을 추가해야하나
	public String insertreply(HttpSession session, ConsultingDto consultingDto) throws Exception {
		petHotelService.insertreply(consultingDto);
//		return ("redirect:/detail.openconsultDetail.do");
		return ("redirect:/detail.openconsultDetail.do?consultingIdx=" + consultingDto.getConsultingIdx()); // 다시 돌아와서 serviceImpl에서 저장된 값을
		// consultingDto 의 @Data로 타져서 그곳의 consultingIdx를 가져와 "redirect:/detail.openconsultDetail.do?consultingIdx=" 뒤에 consultingIdx라는 파라미터를 붙여서 전달 
		// > 새로 등록된 댓글을 확인 가능도록 등록 직후(컨트롤러 메서드 종료 후) 해당 댓글이 있는 상세페이지로 리다이렉트 처리
	}

	// ---------------------qnalist--------------------------------------------
	@GetMapping("/list")
	public ModelAndView consultinglist(
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage)
			throws Exception {
		ModelAndView mv = new ModelAndView("Business_QA.html");// 1 --08/30 > 4
		/* ↓ selectConsultingList 메서드가 반환하는 컨설팅 데이터(ConsultingDto) 객체들의 목록(List)을 **list**라는 변수에 저장 */
		List<ConsultingDto> list = petHotelService.selectConsultingList((currentPage - 1) * 10); // 2 -- 10개씩 보여지도록 페이징 처리하는 > 5
		mv.addObject("list", list); // 6 > 8
		mv.addObject("pageCount", Math.ceil(petHotelService.selectConsultingListCount() / 10.0)); // 9 > 11 (return petHotelMapper.selectConsultingListCount();) <-여기 타고 넘어옴
		mv.addObject("currentPage", currentPage); // 12
		return mv; // 13
	}

	// --------------------------------업체등록-----------------------------------------
	@GetMapping("/company")
	public ModelAndView companyregist() throws Exception {
		ModelAndView mv = new ModelAndView("Business_registration.html");
		return mv;
	}

	@PostMapping("/company/regist")
	public String insertcompany(@RequestParam("file") MultipartFile file, CompanyDto companydto) throws Exception {
		petHotelService.insertcompany(file, companydto);
		/* 8/31 
		 * 업체 등록 페이지에서 파일 선택 및 내용 입력 후 등록을 누르면  */
		return ("redirect:/company"); //impl파일에서 관련 메서드 모두 실행 및 저장 후 해당 내용 반영된 /company로 리다이렉트 > 신규 업체등록 완료. 
	}
	// 08/25
	// 뭔가 url이며 컨트롤러 순서도 뒤죽박죽이고 조잡한 부분이 너무 많음. - 효율적으로 개선해야함.
	// 그리고 다들 등록하기 버튼만 있을 뿐 수정/삭제 관련된 기능이 없음
	// -수정/삭제 하려면 Q&A 댓글 작성 및 수정,삭제는 오로지 admin만 가능하도록 권한 set해야함. +리뷰나 뭐 게시글 등 일개 유저가
	// 남긴 것들은 본인들이 수정/삭제 가능하도록 설정해야.. (유저/관리자 구분해서 권한 별도로 설정)

	// ---------------------------메인페이지--------------------------
	@GetMapping("/mainpage")
	public ModelAndView mainpage(
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage)
			throws Exception {
		ModelAndView mv = new ModelAndView("/mainpage");

		List<CompanyDto> list = petHotelService.companylist((currentPage - 1) * 4);
		mv.addObject("list", list);
		mv.addObject("pageCount", Math.ceil(petHotelService.selectBoardListCount() / 4.0)); // 0825 serviceImpl 파일의
																							// selectBoardListCount()
																							// 메서드에서 return
																							// petHotelMapper.selectBoardListCount();를
																							// 수행함
		mv.addObject("currentPage", currentPage);

		return mv;
	}

	/* 주목 */

	// -------------------------회사 상세----------------------------------
	@GetMapping("/detail.company") // 여기 왜 회사 상세가 2개인지 확인해보고 필요없는건 다 빼버려야하
	public ModelAndView detailcompany() throws Exception {
		ModelAndView mv = new ModelAndView("Company_info_detail.html");
		return mv;
	}
	// ----------------------회사 리스트---------------------------------

	@GetMapping("/download.do") // ↓ 쿼리스트링에서 전달된 업체번호를(companyIdx) / ↓ 받음 응답 객체로 직접 브라우저에 파일을 보낼 수 있음
	public void downloadFile(@RequestParam int companyIdx, HttpServletResponse response) throws Exception { // 반환하지 않고, 직접 파일 스트림을 응답에 사용
		CompanyDto companyDto = petHotelService.onecompany(companyIdx); // 1 > 3 --companyIdx로 업체 정보 1건 조회
		String companyPhoto = companyDto.getCompanyPhoto(); // 4 > 6 --getCompanyPhoto()로 파일 경로(사진 경로) 가져옴

		FileInputStream fis = null; // 7
		BufferedInputStream bis = null; // 8
		BufferedOutputStream bos = null; // 9
		/* ↑ 나중에 finally에서 close() 하기 위해 try 바깥에서 미리 선언(선언만) */
		try {
			response.setHeader("Content-Disposition", "inline;"); // 10
			/* ↑ 파일을 브라우저에서 바로 보여주겠다는 의미 // "attachment;"로 바꾸면 브라우저가 강제로 다운로드창 띄움*/
			byte[] buf = new byte[1024]; // 11
			fis = new FileInputStream(companyPhoto); // 12
			bis = new BufferedInputStream(fis); // 13 --효율적인 읽기를 위해 BufferedInputStream 래핑
			bos = new BufferedOutputStream(response.getOutputStream()); // 14  --응답으로 내보낼 출력 스트림 준비
			
			int read;
			while ((read = bis.read(buf, 0, 1024)) != -1) { // 15 > 17
				bos.write(buf, 0, read); // 16 >  18  -- 반복
			/* ↑ 반복문을 통해 파일 전체 내용을 1KB씩 계속 전송 */
			}
		} finally {
			bos.close(); // > 다 돌고나면 finally구문 
			bis.close();
			fis.close();
		} // 모든 작업이 끝나고 나면 열어둔 스트림들을 반드시 닫아줘야 함 close() 안 하면 데이터 유실가능성 있음
	}
	/* *개선점 -- 업로드 파일이 이미지일 경우 response.setContentType("image/jpeg") 같은 것도 추가하면 정확도를 높일 수 있음 
	 * 파일명이 한글일 경우 Content-Disposition 설정은 URL 인코딩 필요 - 그럼 파일명이 한글인지 아닌지 판별하는 코드도 필요하겠지..?

	 */

	// ---------------------------------업체 상세페이지----------------------------
	@GetMapping("/companyDetail.do") // <--여기 개선
	// **개선점**
	// 추후 URL 인덱스넘버 알고 업체명으로 뜨도록 DB에 slug 추가해서 설정, 컨트롤러 및 쿼리, 서비스레이어 로직 변경예정 - 보안성 및
	// 최적화 다른 관련 코드들도 수정
	public ModelAndView companydetail(@RequestParam("companyIdx") int companyIdx,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage)
			throws Exception {
		// 리뷰 페이징처리
		int pageSize = 5; // 1 --08/30
		int offset = (currentPage - 1) * pageSize; // 2

		// 업체 상세정보
		CompanyDto companydetail = petHotelService.companydetail(companyIdx); // 3 > 5
		ModelAndView mv = new ModelAndView("company_info_detail.html"); // 6

		mv.addObject("companydetail", companydetail); // 7

 		CompanyDto reviewlist1 = petHotelService.reviewlist1(companyIdx);// 8 > 10
		mv.addObject("reviewlist1", reviewlist1); // 11

		List<ReviewDto> reviewlist2 = petHotelService.reviewlist2Paging(offset, companyIdx); // 12 > 17
		int totalReviews = petHotelService.reviewCount(companyIdx); // 18 > 21 --리턴값으로 받은 리뷰 카운드 숫자를 토탈로 설정
		mv.addObject("reviewlist2", reviewlist2);// 22
		mv.addObject("pageCount", Math.ceil(totalReviews / 5.0));// 23 -- 5개씩만 보이도록 페이징 처리
		mv.addObject("currentPage", currentPage); //24
		mv.addObject("companyIdx", companyIdx); // 25    --뷰 화면단에 해당 내용의 객체들 추가?

		// 별점 불러오는거
		List<StarDto> starDto = petHotelService.star(); //26 > 28 impl에서 리턴한 값 가져옴
		mv.addObject("star", starDto); // 29 뷰 ~ star에 starDto타입으로 객체 추가?

		// 평점 (이거 안되는데 확인해봐야함)
		ReviewDto reviewDto = petHotelService.averageStar(companyIdx); //30 > 33 -- 3.6 반환한거 가져오겠지
		mv.addObject("averageStar", reviewDto); // 34 -- 가져온 거 view 파일에 averageStar이라는 속성의 이름으로(=key로) reviewDto =값(value)을 담은 객체를 view로 전달
		return mv; //위 내용 까지 담긴 뷰 반환
	}

/////여기수정함 고쳐야함
	@PostMapping("/insertCompanyReview")
	public String insertreview(ReviewDto reviewDto, HttpSession session) throws Exception {
		UserDto loginUser = (UserDto) session.getAttribute("users");
		if (loginUser == null) {
			// 로그인 안 되어 있으면 로그인 페이지로 리다이렉트
			return "redirect:/login.do";
		}
		// reviewDto에 로그인된 사용자 ID 넣기
		reviewDto.setReviewWriter(loginUser.getUserId());

		petHotelService.insertreview(reviewDto);
		return ("redirect:/companyDetail.do?companyIdx=" + reviewDto.getCompanyIdx());
	}

	
	// --------------------예약 등록----------------------------
	
	@PostMapping("/apply")
	public String insertapply(ApplyDto applyDto) throws Exception {
		petHotelService.insertapply(applyDto);
		return ("redirect:/applylist?companyIdx=" + applyDto.getCompanyIdx());
	}
	
	/* 9/1 -- 아마 ↓이건 mv (입력 폼)불러오는 메서드같은 > 그 후 PostMapping 타지고 */
	@GetMapping("/apply123")
	public ModelAndView displayinsert(@RequestParam int companyIdx) throws Exception {
		ModelAndView mv = new ModelAndView("apply.html"); // 1 -- 9/1 업체상세에서 예약하기를 누르면 여기로 타짐
		CompanyDto companyDto = petHotelService.displayinsert(companyIdx);// 2 > 4
		mv.addObject("displayinsert", companyDto);// 5 -- displayinsert라는 이름의 companyDto타입 객체를 view에 추가
		return mv; // 6 ㄴ 그걸 리턴
	}

	@GetMapping("/applylist")
	public ModelAndView applylist(
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam int companyIdx) throws Exception {
		ModelAndView mv = new ModelAndView("applylist.html");
		List<ApplyDto> list = petHotelService.applylist((currentPage - 1) * 10, companyIdx);
		mv.addObject("companyIdx", companyIdx);
		mv.addObject("applylist", list);
		mv.addObject("pageCount", Math.ceil(petHotelService.selectApplyListCount() / 10.0));
		mv.addObject("currentPage", currentPage);
		return mv;
	}

	// --------------------예약 확인------------------------
	@GetMapping("/reservation")
	public ModelAndView reservation(@RequestParam int applyIdx) throws Exception {
		ModelAndView mv = new ModelAndView("reservation.html");
		ApplyDto applyDto = petHotelService.reservation(applyIdx);
		mv.addObject("reservation", applyDto);
		return mv;
	}
}

// 08/24 
// **개선점** - 전반적으로 네이밍 부분이 헷갈림 - 직관적이면서 통일감을 주는 네이밍 재설정 필요