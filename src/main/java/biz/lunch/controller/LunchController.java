package biz.lunch.controller;

import biz.lunch.service.LunchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 점심비 정산 Controller
 * REST API 엔드포인트 제공
 * 
 * @author 공통서비스 개발팀
 * @since 2025.01.09
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/lunch")
public class LunchController {
    
    @Resource(name = "lunchService")
    private LunchService lunchService;
    
    /**
     * 점심비 등록
     * @param params 등록 정보 (lunchDate, storeName, payerUserId, totalAmount, participants)
     * @return 등록 결과 (성공: 양수, 실패: 0 이하)
     */
    @PostMapping("/registerLunch.do")
    public Map<String, Object> registerLunch(@RequestBody Map<String, Object> params) {
        log.debug("registerLunch - params: {}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            int count = lunchService.registerLunch(params);
            result.put("success", count > 0);
            result.put("count", count);
            result.put("message", count > 0 ? "등록 성공" : "등록 실패");
        } catch (Exception e) {
            log.error("registerLunch error", e);
            result.put("success", false);
            result.put("message", "시스템 오류: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 점심비 수정
     * @param params 수정 정보 (lunchId, lunchDate, storeName, totalAmount, participants)
     * @return 수정 결과
     */
    @PostMapping("/updateLunch.do")
    public Map<String, Object> updateLunch(@RequestBody Map<String, Object> params) {
        log.debug("updateLunch - params: {}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            int count = lunchService.updateLunch(params);
            result.put("success", count > 0);
            result.put("count", count);
            result.put("message", count > 0 ? "수정 성공" : "수정 실패");
        } catch (Exception e) {
            log.error("updateLunch error", e);
            result.put("success", false);
            result.put("message", "시스템 오류: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 점심비 삭제
     * @param id 삭제할 점심비 ID
     * @return 삭제 결과
     */
    @PostMapping("/deleteLunch.do")
    public Map<String, Object> deleteLunch(@RequestBody Map<String, Object> params) {
        log.debug("deleteLunch - params: {}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            int id = Integer.parseInt(String.valueOf(params.get("lunchId")));
            int count = lunchService.deleteLunch(id);
            result.put("success", count > 0);
            result.put("count", count);
            result.put("message", count > 0 ? "삭제 성공" : "삭제 실패");
        } catch (Exception e) {
            log.error("deleteLunch error", e);
            result.put("success", false);
            result.put("message", "시스템 오류: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 점심비 목록 조회
     * @param params 조회 조건 (startDate, endDate, settlementStatus 등)
     * @return 점심비 목록
     */
    @PostMapping("/getLunchList.do")
    public Map<String, Object> getLunchList(@RequestBody Map<String, Object> params) {
        log.debug("getLunchList - params: {}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> list = lunchService.getLunchList(params);
            result.put("success", true);
            result.put("list", list);
            result.put("count", list.size());
        } catch (Exception e) {
            log.error("getLunchList error", e);
            result.put("success", false);
            result.put("message", "시스템 오류: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 통계 조회
     * @param params 조회 조건 (year, month, userId 등)
     * @return 통계 데이터
     */
    @PostMapping("/getStatistics.do")
    public Map<String, Object> getStatistics(@RequestBody Map<String, Object> params) {
        log.debug("getStatistics - params: {}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> stats = lunchService.getStatistics(params);
            result.put("success", true);
            result.putAll(stats);
        } catch (Exception e) {
            log.error("getStatistics error", e);
            result.put("success", false);
            result.put("message", "시스템 오류: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 정산 완료 처리
     * @param params 정산 정보 (startDate, endDate)
     * @return 정산 결과
     */
    @PostMapping("/completeSettlement.do")
    public Map<String, Object> completeSettlement(@RequestBody Map<String, Object> params) {
        log.debug("completeSettlement - params: {}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            int count = lunchService.completeSettlement(params);
            result.put("success", count > 0);
            result.put("count", count);
            result.put("message", count > 0 ? "정산 완료" : "정산 실패");
        } catch (Exception e) {
            log.error("completeSettlement error", e);
            result.put("success", false);
            result.put("message", "시스템 오류: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 화면 이동 - 등록 폼
     */
    @GetMapping("/lunchForm.do")
    public String lunchForm() {
        return "lunch/lunchForm";
    }
    
    /**
     * 화면 이동 - 목록
     */
    @GetMapping("/lunchList.do")
    public String lunchList() {
        return "lunch/lunchList";
    }
    
    /**
     * 화면 이동 - 통계
     */
    @GetMapping("/lunchStats.do")
    public String lunchStats() {
        return "lunch/lunchStats";
    }
}
