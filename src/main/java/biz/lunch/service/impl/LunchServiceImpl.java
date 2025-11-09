package biz.lunch.service.impl;

import biz.lunch.dao.LunchMapper;
import biz.lunch.service.LunchService;
import biz.lunch.vo.LunchMasterVO;
import biz.lunch.vo.LunchParticipantVO;
import biz.lunch.vo.LunchSummaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 점심비 정산 관리 서비스 구현 클래스
 * 
 * @author 공통서비스 개발팀
 * @since 2025.01.09
 * @version 1.0
 */
@Slf4j
@Service("lunchService")
public class LunchServiceImpl implements LunchService {
    
    @Resource(name = "lunchMapper")
    private LunchMapper lunchMapper;
    
    /**
     * 점심/커피 내역 등록
     * 마스터와 참여자를 한 트랜잭션으로 처리
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int registerLunch(Map<String, Object> params) throws Exception {
        log.debug("===== 점심 내역 등록 시작 =====");
        log.debug("등록 데이터: {}", params);
        
        try {
            // 1. 마스터 VO 생성
            LunchMasterVO masterVO = createMasterVO(params);
            
            // 2. 마스터 등록
            int result = lunchMapper.insertLunchMaster(masterVO);
            
            if (result > 0) {
                int lunchId = masterVO.getLunchId(); // auto-generated key
                log.debug("생성된 lunch_id: {}", lunchId);
                
                // 3. 참여자 목록 등록
                List<Map<String, Object>> participants = 
                    (List<Map<String, Object>>) params.get("participants");
                
                if (participants != null && !participants.isEmpty()) {
                    for (Map<String, Object> pMap : participants) {
                        LunchParticipantVO pVO = new LunchParticipantVO();
                        pVO.setLunchId(lunchId);
                        pVO.setUserId((String) pMap.get("userId"));
                        pVO.setUserName((String) pMap.get("userName"));
                        
                        // 안전한 숫자 변환
                        Object amountObj = pMap.get("amount");
                        int amount = 0;
                        if (amountObj instanceof Integer) {
                            amount = (Integer) amountObj;
                        } else if (amountObj instanceof String) {
                            amount = Integer.parseInt((String) amountObj);
                        } else if (amountObj != null) {
                            amount = Integer.parseInt(amountObj.toString());
                        }
                        pVO.setAmount(amount);
                        
                        lunchMapper.insertParticipant(pVO);
                        log.debug("참여자 등록: {}", pVO.getUserName());
                    }
                }
                
                log.debug("===== 점심 내역 등록 완료 =====");
                return lunchId;
            }
            
            return 0;
            
        } catch (Exception e) {
            log.error("점심 내역 등록 중 오류 발생", e);
            throw e;
        }
    }
    
    /**
     * 점심/커피 내역 수정
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateLunch(Map<String, Object> params) throws Exception {
        log.debug("===== 점심 내역 수정 시작 =====");
        log.debug("수정 데이터: {}", params);
        
        try {
            // 안전한 lunchId 변환
            Object lunchIdObj = params.get("lunchId");
            int lunchId = 0;
            if (lunchIdObj instanceof Integer) {
                lunchId = (Integer) lunchIdObj;
            } else if (lunchIdObj instanceof String) {
                lunchId = Integer.parseInt((String) lunchIdObj);
            } else if (lunchIdObj != null) {
                lunchId = Integer.parseInt(lunchIdObj.toString());
            }
            
            // 1. 마스터 수정
            LunchMasterVO masterVO = createMasterVO(params);
            masterVO.setLunchId(lunchId);
            int result = lunchMapper.updateLunchMaster(masterVO);
            
            // 2. 기존 참여자 삭제
            lunchMapper.deleteParticipantsByLunchId(lunchId);
            
            // 3. 새로운 참여자 등록
            List<Map<String, Object>> participants = 
                (List<Map<String, Object>>) params.get("participants");
            
            if (participants != null && !participants.isEmpty()) {
                for (Map<String, Object> pMap : participants) {
                    LunchParticipantVO pVO = new LunchParticipantVO();
                    pVO.setLunchId(lunchId);
                    pVO.setUserId((String) pMap.get("userId"));
                    pVO.setUserName((String) pMap.get("userName"));
                    
                    // 안전한 숫자 변환
                    Object amountObj = pMap.get("amount");
                    int amount = 0;
                    if (amountObj instanceof Integer) {
                        amount = (Integer) amountObj;
                    } else if (amountObj instanceof String) {
                        amount = Integer.parseInt((String) amountObj);
                    } else if (amountObj != null) {
                        amount = Integer.parseInt(amountObj.toString());
                    }
                    pVO.setAmount(amount);
                    
                    lunchMapper.insertParticipant(pVO);
                }
            }
            
            log.debug("===== 점심 내역 수정 완료 =====");
            return result;
            
        } catch (Exception e) {
            log.error("점심 내역 수정 중 오류 발생", e);
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
            // CASCADE 설정으로 참여자도 자동 삭제됨
            int result = lunchMapper.deleteLunchMaster(lunchId);
            
            log.debug("===== 점심 내역 삭제 완료 =====");
            return result;
            
        } catch (Exception e) {
            log.error("점심 내역 삭제 중 오류 발생", e);
            throw e;
        }
    }
    
    /**
     * Map 데이터를 MasterVO로 변환하는 헬퍼 메서드
     */
    private LunchMasterVO createMasterVO(Map<String, Object> params) {
        LunchMasterVO vo = new LunchMasterVO();
        vo.setLunchDate((String) params.get("lunchDate"));
        
        String storeName = (String) params.get("storeName");
        vo.setStoreName(storeName);
        
        // 가게명에 "커피"가 포함되면 커피, 아니면 점심으로 자동 분류
        String lunchType = (storeName != null && storeName.contains("커피")) ? "커피" : "점심";
        vo.setLunchType(lunchType);
        
        vo.setPayerUserId((String) params.get("payerUserId"));
        vo.setPayerUserName((String) params.get("payerUserName"));
        
        // 안전한 숫자 변환
        Object totalAmountObj = params.get("totalAmount");
        int totalAmount = 0;
        if (totalAmountObj instanceof Integer) {
            totalAmount = (Integer) totalAmountObj;
        } else if (totalAmountObj instanceof String) {
            totalAmount = Integer.parseInt((String) totalAmountObj);
        } else if (totalAmountObj != null) {
            totalAmount = Integer.parseInt(totalAmountObj.toString());
        }
        vo.setTotalAmount(totalAmount);
        
        vo.setDescription((String) params.get("description"));
        vo.setRegisterUserId((String) params.get("registerUserId"));
        vo.setUpdateUserId((String) params.get("updateUserId"));
        
        if (params.containsKey("settlementStatus")) {
            vo.setSettlementStatus((String) params.get("settlementStatus"));
        }
        
        return vo;
    }

    /**
     * 점심/커피 내역 목록 조회 및 자동 집계
     * ★ 가장 복잡하고 중요한 비즈니스 로직 ★
     */
    @Override
    public Map<String, Object> getLunchList(Map<String, Object> params) throws Exception {
        log.debug("===== 점심 내역 조회 및 자동 집계 시작 =====");
        log.debug("조회 조건: {}", params);
        
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 1. 내역 목록 조회 (참여자 포함)
            List<LunchMasterVO> lunchList = lunchMapper.selectLunchListWithParticipants(params);
            log.debug("조회된 내역 수: {}", lunchList.size());
            
            // 2. 자동 집계 객체 생성
            LunchSummaryVO summary = new LunchSummaryVO();
            summary.setStartDate((String) params.get("startDate"));
            summary.setEndDate((String) params.get("endDate"));
            
            // 3. 결제자별 카운트 (대표 정산자 계산용)
            Map<String, Integer> payerCountMap = new HashMap<>();
            
            // 4. 각 내역별로 송금 금액 계산
            for (LunchMasterVO master : lunchList) {
                List<LunchParticipantVO> participants = master.getParticipants();
                
                if (participants == null || participants.isEmpty()) {
                    continue;
                }
                
                String payerUserId = master.getPayerUserId();
                int totalAmount = master.getTotalAmount();
                
                // 결제자 카운트 증가
                payerCountMap.put(payerUserId, 
                    payerCountMap.getOrDefault(payerUserId, 0) + 1);
                
                // 각 참여자의 송금 금액 계산
                for (LunchParticipantVO participant : participants) {
                    String userId = participant.getUserId();
                    String userName = participant.getUserName();
                    int amount = participant.getAmount();
                    
                    // 결제자 여부 확인
                    boolean isPayer = userId.equals(payerUserId);
                    
                    // 송금 금액 계산
                    participant.calculateTransferAmount(totalAmount, isPayer);
                    
                    // 사용자별 합계에 추가
                    summary.addUserAmount(userId, userName, amount);
                    summary.setUserTransfer(userId, participant.getTransferAmount());
                }
                
                // 정산 상태별 카운트
                if ("완료".equals(master.getSettlementStatus())) {
                    summary.setSettledCount(summary.getSettledCount() + 1);
                } else {
                    summary.setPendingCount(summary.getPendingCount() + 1);
                }
                
                // 전체 금액 합산
                summary.setTotalAmount(summary.getTotalAmount() + totalAmount);
                summary.setTotalCount(summary.getTotalCount() + 1);
            }
            
            // 5. 대표 정산자 계산 (내부 로직용)
            summary.calculateRepresentative(payerCountMap);
            
            // 6. 월 라벨 생성 (예: 2024년 12월)
            if (params.get("startDate") != null) {
                String startDate = (String) params.get("startDate");
                String[] parts = startDate.split("-");
                if (parts.length >= 2) {
                    summary.setMonthLabel(parts[0] + "년 " + parts[1] + "월");
                }
            }
            
            // 7. 결과 반환
            result.put("lunchList", lunchList);
            result.put("summary", summary);
            
            log.debug("===== 점심 내역 조회 및 자동 집계 완료 =====");
            log.debug("총 건수: {}, 총 금액: {}", summary.getTotalCount(), summary.getTotalAmount());
            
            return result;
            
        } catch (Exception e) {
            log.error("점심 내역 조회 중 오류 발생", e);
            throw e;
        }
    }

    /**
     * 사용자별/월별 통계 조회
     */
    @Override
    public Map<String, Object> getStatistics(Map<String, Object> params) throws Exception {
        log.debug("===== 통계 조회 시작 =====");
        log.debug("조회 조건: {}", params);
        
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 1. 월별 통계
            List<Map<String, Object>> monthlyStats = lunchMapper.selectMonthlyStatistics(params);
            result.put("monthlyStatistics", monthlyStats);
            
            // 2. 사용자별 통계
            List<Map<String, Object>> userStats = lunchMapper.selectUserStatistics(params);
            result.put("userStatistics", userStats);
            
            // 3. 사용자별 참여 횟수
            List<Map<String, Object>> participationCount = 
                lunchMapper.selectUserParticipationCount(params);
            result.put("participationCount", participationCount);
            
            log.debug("===== 통계 조회 완료 =====");
            return result;
            
        } catch (Exception e) {
            log.error("통계 조회 중 오류 발생", e);
            throw e;
        }
    }
    
    /**
     * 정산 완료 처리
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int completeSettlement(Map<String, Object> params) throws Exception {
        log.debug("===== 정산 완료 처리 시작 =====");
        log.debug("처리 데이터: {}", params);
        
        try {
            int result = lunchMapper.updateSettlementStatus(params);
            
            log.debug("===== 정산 완료 처리 완료 =====");
            return result;
            
        } catch (Exception e) {
            log.error("정산 완료 처리 중 오류 발생", e);
            throw e;
        }
    }

}