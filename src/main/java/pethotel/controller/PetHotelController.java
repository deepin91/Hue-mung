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
	public String insertConsulting(HttpSession session, ConsultingDto consultingDto) throws Exception{
		UserDto loginUser = (UserDto)session.getAttribute("users");
		
		consultingDto.setCUserId(loginUser.getUserId());
		consultingDto.setConsultingCreatedt(LocalDateTime.now());
		petHotelService.insertConsulting(consultingDto);
		return "redirect:/list";
		
	}

	// ----------------------qna상세 페이지---------------------------------
	@GetMapping("/detail.openconsultDetail.do")
	public ModelAndView detail(@RequestParam int consultingIdx) throws Exception {

		ConsultingDto consultingDto = petHotelService.detail(consultingIdx);
		ModelAndView mv = new ModelAndView("Business_answer_content.html");

		mv.addObject("detail", consultingDto);
		return mv;

	}

	@PostMapping("/reply/1234")
	public String insertreply(HttpSession session, ConsultingDto consultingDto) throws Exception {
		petHotelService.insertreply(consultingDto);
//		return ("redirect:/detail.openconsultDetail.do");
		return ("redirect:/detail.openconsultDetail.do?consultingIdx=" + consultingDto.getConsultingIdx());
	}

	// ---------------------qnalist--------------------------------------------
	@GetMapping("/list")
	public ModelAndView consultinglist(
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage)
			throws Exception {
		ModelAndView mv = new ModelAndView("Business_QA.html");
		List<ConsultingDto> list = petHotelService.selectConsultingList((currentPage - 1) * 10);
		mv.addObject("list", list);
		mv.addObject("pageCount", Math.ceil(petHotelService.selectConsultingListCount() / 10.0));
		mv.addObject("currentPage", currentPage);
		return mv;
	}

	// --------------------------------업체
	// 등록-----------------------------------------
	@GetMapping("/company")
	public ModelAndView companyregist() throws Exception {
		ModelAndView mv = new ModelAndView("Business_registration.html");
		return mv;
	}

	@PostMapping("/company/regist")
	public String insertcompany(@RequestParam("file") MultipartFile file, CompanyDto companydto) throws Exception {
		petHotelService.insertcompany(file, companydto);
		return ("redirect:/company");
	}

	// ---------------------------메인페이지--------------------------
	@GetMapping("/mainpage")
	public ModelAndView mainpage(
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage)
			throws Exception {
		ModelAndView mv = new ModelAndView("/mainpage");

		List<CompanyDto> list = petHotelService.companylist((currentPage - 1) * 4);
		mv.addObject("list", list);
		mv.addObject("pageCount", Math.ceil(petHotelService.selectBoardListCount() / 4.0));
		mv.addObject("currentPage", currentPage);

		return mv;
	}

	// -------------------------회사 상세----------------------------------
	@GetMapping("/detail.company")
	public ModelAndView detailcompany() throws Exception {
		ModelAndView mv = new ModelAndView("Company_info_detail.html");
		return mv;
	}
	// ----------------------회사 리스트---------------------------------

	@GetMapping("/download.do")
	public void downloadFile(@RequestParam int companyIdx, HttpServletResponse response) throws Exception {
		CompanyDto companyDto = petHotelService.onecompany(companyIdx);
		String companyPhoto = companyDto.getCompanyPhoto();

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			response.setHeader("Content-Disposition", "inline;");

			byte[] buf = new byte[1024];
			fis = new FileInputStream(companyPhoto);
			bis = new BufferedInputStream(fis);
			bos = new BufferedOutputStream(response.getOutputStream());
			int read;
			while ((read = bis.read(buf, 0, 1024)) != -1) {
				bos.write(buf, 0, read);
			}
		} finally {
			bos.close();
			bis.close();
			fis.close();
		}
	}

	// -------------------------------------------------------------업체 상세페이지
	@GetMapping("/companyDetail.do")
	public ModelAndView companydetail(@RequestParam("companyIdx")int companyIdx,
									  @RequestParam	(value = "currentPage", required = false, defaultValue = "1") int currentPage)throws Exception {
		//리뷰 페이징처리							  
				int pageSize = 5;
				int offset = (currentPage - 1) * pageSize;
		
		// 업체 상세정보
		CompanyDto companydetail = petHotelService.companydetail(companyIdx);
		ModelAndView mv = new ModelAndView("company_info_detail.html");

		mv.addObject("companydetail", companydetail);
		
		CompanyDto reviewlist1 = petHotelService.reviewlist1(companyIdx);
		mv.addObject("reviewlist1", reviewlist1);

		List<ReviewDto> reviewlist2 = petHotelService.reviewlist2Paging(offset, companyIdx);
		int totalReviews = petHotelService.reviewCount(companyIdx);
		mv.addObject("reviewlist2", reviewlist2);
		mv.addObject("pageCount", Math.ceil(totalReviews / 5.0));
		mv.addObject("currentPage", currentPage);
		mv.addObject("companyIdx", companyIdx);
				
		// 별점 불러오는거
		List<StarDto> starDto = petHotelService.star();
		mv.addObject("star", starDto);
		
		// 평점 (이거 안되는데 확인해봐야함)
		ReviewDto reviewDto = petHotelService.averageStar(companyIdx);
		mv.addObject("averageStar", reviewDto);
		return mv;
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

	@GetMapping("/apply123")
	public ModelAndView displayinsert(@RequestParam int companyIdx) throws Exception {
		ModelAndView mv = new ModelAndView("apply.html");
		CompanyDto companyDto = petHotelService.displayinsert(companyIdx);
		mv.addObject("displayinsert", companyDto);
		return mv;
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