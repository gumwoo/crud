package biz.lunch.service;

import java.util.List;
import java.util.Map;

/**
 * 점심비 정산 관리 서비스 인터페이스
 * 
 * @author 공통서비스 개발팀
 * @since 2025.01.09
 * @version 1.0
 */
public interface LunchService {
    
    /**
     * 점심/커피 내역 등록
     * @param params 등록 정보 (마스터 + 참여자 리스트)
     * @return 등록된 lunch_id
     * @throws Exception
     */
    int registerLunch(Map<String, Object> params) throws Exception;
    
    /**
     * 점심/커피 내역 수정
     * @param params 수정 정보 (마스터 + 참여자 리스트)
     * @return 수정된 건수
     * @throws Exception
     */
    int updateLunch(Map<String, Object> params) throws Exception;
    
    /**
     * 점심/커피 내역 삭제
     * @param lunchId 삭제할 내역 ID
     * @return 삭제된 건수
     * @throws Exception
     */
    int deleteLunch(int lunchId) throws Exception;
    
    /**
     * 점심/커피 내역 목록 조회
     * @param params 조회 조건 (startDate, endDate, userId, settlementStatus 등)
     * @return 내역 목록
     * @throws Exception
     */
    List<Map<String, Object>> getLunchList(Map<String, Object> params) throws Exception;
    
    /**
     * 사용자별/월별 통계 조회
     * @param params 조회 조건 (year, month, userId 등)
     * @return 통계 데이터
     * @throws Exception
     */
    Map<String, Object> getStatistics(Map<String, Object> params) throws Exception;
    
    /**
     * 정산 완료 처리
     * @param params lunchId, updateUserId
     * @return 처리된 건수
     * @throws Exception
     */
    int completeSettlement(Map<String, Object> params) throws Exception;
}
