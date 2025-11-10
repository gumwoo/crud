package biz.lunch.service.impl;

import biz.lunch.mapper.LunchMapper;
import biz.lunch.service.LunchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 점심비 정산 관리 서비스 구현 클래스 (설계도 기반 - Map 전용)
 * 
 * @author 공통서비스 개발팀
 * @since 2025.01.10
 * @version 2.0
 */
@Slf4j
@Service("lunchService")
public class LunchServiceImpl implements LunchService {
    
    @Resource(name = "lunchMapper")
    private LunchMapper lunchMapper;
    
    /**
     * 점심/커피 내역 등록
     * Mapper에 Map 그대로 전달
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int registerLunch(Map<String, Object> params) throws Exception {
        log.debug("===== 점심 내역 등록 시작 =====");
        log.debug("등록 데이터: {}", params);
        
        try {
            int result = lunchMapper.registerLunch(params);
            log.debug("===== 점심 내역 등록 완료: {} =====", result);
            return result;
        } catch (Exception e) {
            log.error("점심 내역 등록 실패", e);
            throw e;
        }
    }
    
    /**
     * 점심/커피 내역 수정
     * Mapper에 Map 그대로 전달
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateLunch(Map<String, Object> params) throws Exception {
        log.debug("===== 점심 내역 수정 시작 =====");
        log.debug("수정 데이터: {}", params);
        
        try {
            int result = lunchMapper.updateLunch(params);
            log.debug("===== 점심 내역 수정 완료: {} =====", result);
            return result;
        } catch (Exception e) {
            log.error("점심 내역 수정 실패", e);
            throw e;
        }
    }
    
    /**
     * 점심/커피 내역 삭제
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteLunch(int lunchId) throws Exception {
        log.debug("===== 점심 내역 삭제 시작: {} =====", lunchId);
        
        try {
            int result = lunchMapper.deleteLunch(lunchId);
            log.debug("===== 점심 내역 삭제 완료: {} =====", result);
            return result;
        } catch (Exception e) {
            log.error("점심 내역 삭제 실패", e);
            throw e;
        }
    }
    
    /**
     * 점심/커피 내역 목록 조회
     * Mapper에서 List<Map> 그대로 반환
     */
    @Override
    public List<Map<String, Object>> getLunchList(Map<String, Object> params) throws Exception {
        log.debug("===== 점심 내역 목록 조회 시작 =====");
        log.debug("조회 조건: {}", params);
        
        try {
            List<Map<String, Object>> list = lunchMapper.getLunchList(params);
            log.debug("===== 점심 내역 목록 조회 완료: {}건 =====", list.size());
            return list;
        } catch (Exception e) {
            log.error("점심 내역 목록 조회 실패", e);
            throw e;
        }
    }
    
    /**
     * 통계 조회
     * Mapper에서 Map 그대로 반환
     */
    @Override
    public Map<String, Object> getStatistics(Map<String, Object> params) throws Exception {
        log.debug("===== 통계 조회 시작 =====");
        log.debug("조회 조건: {}", params);
        
        try {
            Map<String, Object> stats = lunchMapper.getStatistics();
            log.debug("===== 통계 조회 완료 =====");
            return stats;
        } catch (Exception e) {
            log.error("통계 조회 실패", e);
            throw e;
        }
    }
    
    /**
     * 정산 완료 처리
     * Mapper에 Map 그대로 전달
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int completeSettlement(Map<String, Object> params) throws Exception {
        log.debug("===== 정산 완료 처리 시작 =====");
        log.debug("정산 데이터: {}", params);
        
        try {
            int result = lunchMapper.completeSettlement(params);
            log.debug("===== 정산 완료 처리 완료: {} =====", result);
            return result;
        } catch (Exception e) {
            log.error("정산 완료 처리 실패", e);
            throw e;
        }
    }
}
