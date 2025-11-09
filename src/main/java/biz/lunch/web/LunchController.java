package biz.lunch.web;

import biz.login.vo.LoginVO;
import biz.lunch.service.LunchService;
import biz.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 점심비 정산 관리 컨트롤러
 * 
 * @author 공통서비스 개발팀
 * @since 2025.01.09
 * @version 1.0
 */
@Slf4j
@Controller
@RequestMapping("/lunch")
public class LunchController {
    
    @Resource(name = "lunchService")
    private LunchService lunchService;
    
    /**
     * 점심비 정산 목록 화면
     * @param model Model
     * @return 목록 화면
     */
    @GetMapping("/lunchList.do")
    public String lunchList(Model model) {
        log.debug("===== 점심 목록 화면 진입 =====");
        
        // 로그인 체크
        LoginVO loginVO = SessionUtil.getLoginUser();
        if (loginVO == null) {
            return "redirect:/login/loginForm.do";
        }
        
        model.addAttribute("loginVO", loginVO);
        return "lunch/lunchList";
    }
    
    /**
     * 점심비 통계 화면
     * @param model Model
     * @return 통계 화면
     */
    @GetMapping("/lunchStats.do")
    public String lunchStats(Model model) {
        log.debug("===== 점심 통계 화면 진입 =====");
        
        // 로그인 체크
        LoginVO loginVO = SessionUtil.getLoginUser();
        if (loginVO == null) {
            return "redirect:/login/loginForm.do";
        }
        
        model.addAttribute("loginVO", loginVO);
        return "lunch/lunchStats";
    }
    
    /**
     * 점심비 정산 등록 화면
     * @param model Model
     * @return 등록 화면
     */
    @GetMapping("/lunchForm.do")
    public String lunchForm(Model model) {
        log.debug("===== 점심 등록 화면 진입 =====");
        
        // 로그인 체크
        LoginVO loginVO = SessionUtil.getLoginUser();
        if (loginVO == null) {
            return "redirect:/login/loginForm.do";
        }
        
        model.addAttribute("loginVO", loginVO);
        model.addAttribute("mode", "register");
        return "lunch/lunchForm";
    }
    
    /**
     * 점심비 정산 수정 화면
     * @param lunchId 수정할 내역 ID
     * @param model Model
     * @return 수정 화면
     */
    @GetMapping("/lunchForm.do/{lunchId}")
    public String lunchFormEdit(@PathVariable int lunchId, Model model) {
        log.debug("===== 점심 수정 화면 진입: {} =====", lunchId);
        
        // 로그인 체크
        LoginVO loginVO = SessionUtil.getLoginUser();
        if (loginVO == null) {
            return "redirect:/login/loginForm.do";
        }
        
        // TODO: 상세 조회 후 model에 담기
        
        model.addAttribute("loginVO", loginVO);
        model.addAttribute("mode", "update");
        model.addAttribute("lunchId", lunchId);
        return "lunch/lunchForm";
    }

    // ===== AJAX API =====
    
    /**
     * 점심/커피 내역 목록 조회 (AJAX)
     * @param params 조회 조건
     * @return JSON 응답
     */
    @PostMapping("/getLunchList.do")
    @ResponseBody
    public Map<String, Object> getLunchList(@RequestBody Map<String, Object> params) {
        log.debug("===== 점심 목록 조회 API =====");
        log.debug("조회 조건: {}", params);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 로그인 체크
            LoginVO loginVO = SessionUtil.getLoginUser();
            if (loginVO == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return response;
            }
            
            // 서비스 호출
            Map<String, Object> result = lunchService.getLunchList(params);
            
            response.put("success", true);
            response.put("data", result);
            
        } catch (Exception e) {
            log.error("점심 목록 조회 중 오류", e);
            response.put("success", false);
            response.put("message", "조회 중 오류가 발생했습니다.");
        }
        
        return response;
    }
    
    /**
     * 점심/커피 내역 등록 (AJAX)
     * @param params 등록 정보
     * @return JSON 응답
     */
    @PostMapping("/registerLunch.do")
    @ResponseBody
    public Map<String, Object> registerLunch(@RequestBody Map<String, Object> params) {
        log.debug("===== 점심 내역 등록 API =====");
        log.debug("등록 데이터: {}", params);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 로그인 체크
            LoginVO loginVO = SessionUtil.getLoginUser();
            if (loginVO == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return response;
            }
            
            // 등록자 정보 설정
            params.put("registerUserId", loginVO.getUserId());
            
            // 서비스 호출
            int lunchId = lunchService.registerLunch(params);
            
            if (lunchId > 0) {
                response.put("success", true);
                response.put("message", "등록되었습니다.");
                response.put("lunchId", lunchId);
            } else {
                response.put("success", false);
                response.put("message", "등록에 실패했습니다.");
            }
            
        } catch (Exception e) {
            log.error("점심 내역 등록 중 오류", e);
            response.put("success", false);
            response.put("message", "등록 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 점심/커피 내역 수정 (AJAX)
     * @param params 수정 정보
     * @return JSON 응답
     */
    @PostMapping("/updateLunch.do")
    @ResponseBody
    public Map<String, Object> updateLunch(@RequestBody Map<String, Object> params) {
        log.debug("===== 점심 내역 수정 API =====");
        log.debug("수정 데이터: {}", params);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 로그인 체크
            LoginVO loginVO = SessionUtil.getLoginUser();
            if (loginVO == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return response;
            }
            
            // 수정자 정보 설정
            params.put("updateUserId", loginVO.getUserId());
            
            // 서비스 호출
            int result = lunchService.updateLunch(params);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "수정되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "수정에 실패했습니다.");
            }
            
        } catch (Exception e) {
            log.error("점심 내역 수정 중 오류", e);
            response.put("success", false);
            response.put("message", "수정 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 점심/커피 내역 삭제 (AJAX)
     * @param lunchId 삭제할 내역 ID
     * @return JSON 응답
     */
    @PostMapping("/deleteLunch.do")
    @ResponseBody
    public Map<String, Object> deleteLunch(@RequestParam int lunchId) {
        log.debug("===== 점심 내역 삭제 API: {} =====", lunchId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 로그인 체크
            LoginVO loginVO = SessionUtil.getLoginUser();
            if (loginVO == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return response;
            }
            
            // 서비스 호출
            int result = lunchService.deleteLunch(lunchId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "삭제되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "삭제에 실패했습니다.");
            }
            
        } catch (Exception e) {
            log.error("점심 내역 삭제 중 오류", e);
            response.put("success", false);
            response.put("message", "삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 정산 완료 처리 (AJAX)
     * @param params lunchId
     * @return JSON 응답
     */
    @PostMapping("/completeSettlement.do")
    @ResponseBody
    public Map<String, Object> completeSettlement(@RequestBody Map<String, Object> params) {
        log.debug("===== 정산 완료 처리 API =====");
        log.debug("처리 데이터: {}", params);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 로그인 체크
            LoginVO loginVO = SessionUtil.getLoginUser();
            if (loginVO == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return response;
            }
            
            // 수정자 정보 설정
            params.put("updateUserId", loginVO.getUserId());
            
            // 서비스 호출
            int result = lunchService.completeSettlement(params);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "정산 완료 처리되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "정산 완료 처리에 실패했습니다.");
            }
            
        } catch (Exception e) {
            log.error("정산 완료 처리 중 오류", e);
            response.put("success", false);
            response.put("message", "처리 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 통계 조회 (AJAX)
     * @param params 조회 조건
     * @return JSON 응답
     */
    @PostMapping("/getStatistics.do")
    @ResponseBody
    public Map<String, Object> getStatistics(@RequestBody Map<String, Object> params) {
        log.debug("===== 통계 조회 API =====");
        log.debug("조회 조건: {}", params);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 로그인 체크
            LoginVO loginVO = SessionUtil.getLoginUser();
            if (loginVO == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return response;
            }
            
            // 서비스 호출
            Map<String, Object> result = lunchService.getStatistics(params);
            
            response.put("success", true);
            response.put("data", result);
            
        } catch (Exception e) {
            log.error("통계 조회 중 오류", e);
            response.put("success", false);
            response.put("message", "조회 중 오류가 발생했습니다.");
        }
        
        return response;
    }

}