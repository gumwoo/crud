package biz.lunch.dao;

import biz.lunch.vo.LunchMasterVO;
import biz.lunch.vo.LunchParticipantVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 점심비 정산 Mapper (DAO)
 * MyBatis를 통한 데이터베이스 접근 인터페이스
 * 
 * @author 공통서비스 개발팀
 * @since 2025.01.09
 * @version 1.0
 */
@Mapper
public interface LunchMapper {
    
    // ===== 마스터 CRUD =====
    
    /**
     * 점심/커피 내역 등록 (마스터)
     * @param vo 점심 내역 정보
     * @return 등록된 건수
     */
    int insertLunchMaster(LunchMasterVO vo);
    
    /**
     * 점심/커피 내역 수정 (마스터)
     * @param vo 수정할 점심 내역 정보
     * @return 수정된 건수
     */
    int updateLunchMaster(LunchMasterVO vo);
    
    /**
     * 점심/커피 내역 삭제 (마스터)
     * @param lunchId 삭제할 점심 내역 ID
     * @return 삭제된 건수
     */
    int deleteLunchMaster(int lunchId);
    
    /**
     * 점심/커피 내역 상세 조회
     * @param lunchId 점심 내역 ID
     * @return 점심 내역 상세
     */
    LunchMasterVO selectLunchMaster(int lunchId);
    
    // ===== 참여자 CRUD =====
    
    /**
     * 참여자 정보 등록
     * @param vo 참여자 정보
     * @return 등록된 건수
     */
    int insertParticipant(LunchParticipantVO vo);
    
    /**
     * 참여자 정보 수정
     * @param vo 수정할 참여자 정보
     * @return 수정된 건수
     */
    int updateParticipant(LunchParticipantVO vo);
    
    /**
     * 특정 내역의 참여자 전체 삭제
     * @param lunchId 점심 내역 ID
     * @return 삭제된 건수
     */
    int deleteParticipantsByLunchId(int lunchId);
    
    /**
     * 특정 내역의 참여자 목록 조회
     * @param lunchId 점심 내역 ID
     * @return 참여자 목록
     */
    List<LunchParticipantVO> selectParticipantsByLunchId(int lunchId);
    
    // ===== 조회 및 집계 =====
    
    /**
     * 점심/커피 내역 목록 조회 (조건별)
     * @param params 조회 조건 (startDate, endDate, userId, storeName, settlementStatus 등)
     * @return 내역 목록
     */
    List<LunchMasterVO> selectLunchList(Map<String, Object> params);
    
    /**
     * 점심/커피 내역 목록 조회 (참여자 정보 포함)
     * @param params 조회 조건
     * @return 내역 목록 (참여자 포함)
     */
    List<LunchMasterVO> selectLunchListWithParticipants(Map<String, Object> params);
    
    /**
     * 기간별 참여자 목록 조회 (집계용)
     * @param params 조회 조건 (startDate, endDate, settlementStatus)
     * @return 참여자 목록
     */
    List<LunchParticipantVO> selectParticipantsForSummary(Map<String, Object> params);
    
    /**
     * 사용자별 참여 횟수 조회
     * @param params 조회 조건
     * @return 사용자별 참여 횟수 Map
     */
    List<Map<String, Object>> selectUserParticipationCount(Map<String, Object> params);
    
    /**
     * 결제자별 결제 횟수 조회 (대표 정산자 계산용)
     * @param params 조회 조건
     * @return 결제자별 결제 횟수 Map
     */
    List<Map<String, Object>> selectPayerCount(Map<String, Object> params);
    
    // ===== 정산 처리 =====
    
    /**
     * 정산 완료 처리
     * @param params lunchId, settlementDate, updateUserId
     * @return 업데이트된 건수
     */
    int updateSettlementStatus(Map<String, Object> params);
    
    /**
     * 정산 완료 취소 (상태를 '대기'로 변경)
     * @param params lunchId, updateUserId
     * @return 업데이트된 건수
     */
    int cancelSettlement(Map<String, Object> params);
    
    // ===== 통계 =====
    
    /**
     * 월별 통계 조회
     * @param params year, month
     * @return 월별 통계 데이터
     */
    List<Map<String, Object>> selectMonthlyStatistics(Map<String, Object> params);
    
    /**
     * 사용자별 통계 조회
     * @param params startDate, endDate, userId
     * @return 사용자별 통계 데이터
     */
    List<Map<String, Object>> selectUserStatistics(Map<String, Object> params);
}
