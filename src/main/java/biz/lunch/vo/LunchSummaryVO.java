package biz.lunch.vo;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 점심비 정산 집계 결과 VO
 * 월별/사용자별 집계 데이터를 담는 객체
 * (DB 테이블 없음, 계산 결과만 저장)
 * 
 * @author 공통서비스 개발팀
 * @since 2025.01.09
 * @version 1.0
 */
@Data
public class LunchSummaryVO {
    
    // ===== 기간 정보 =====
    private String startDate;           // 시작일 (YYYY-MM-DD)
    private String endDate;             // 종료일 (YYYY-MM-DD)
    private String monthLabel;          // 월 표시 (예: 2024년 12월)
    
    // ===== 전체 집계 =====
    private int totalCount;             // 총 건수
    private int totalAmount;            // 총 금액
    private int settledCount;           // 정산 완료 건수
    private int pendingCount;           // 정산 대기 건수
    
    // ===== 사용자별 집계 (Map<userId, 금액>) =====
    private Map<String, Integer> userTotalMap;      // 사용자별 총 부담금
    private Map<String, Integer> userTransferMap;   // 사용자별 송금 금액
    private Map<String, Integer> userCountMap;      // 사용자별 참여 횟수
    private Map<String, String> userNameMap;        // 사용자 ID → 이름
    
    // ===== 대표 정산자 정보 =====
    private String representativeUserId;        // 대표 정산자 ID (가장 많이 결제한 사람)
    private String representativeUserName;      // 대표 정산자 이름
    private String representativeAccount;       // 대표 정산자 계좌 (고정: 이승은 과장님)
    
    /**
     * 생성자: Map 초기화
     */
    public LunchSummaryVO() {
        this.userTotalMap = new HashMap<>();
        this.userTransferMap = new HashMap<>();
        this.userCountMap = new HashMap<>();
        this.userNameMap = new HashMap<>();
        
        // 대표 정산자 기본값 (UI 고정)
        this.representativeUserName = "이승은 과장";
        this.representativeAccount = "국민은행 0000-00-000000";
    }
    
    /**
     * 사용자별 부담금 추가
     * @param userId 사용자 ID
     * @param userName 사용자 이름
     * @param amount 금액
     */
    public void addUserAmount(String userId, String userName, int amount) {
        userNameMap.put(userId, userName);
        userTotalMap.put(userId, userTotalMap.getOrDefault(userId, 0) + amount);
        userCountMap.put(userId, userCountMap.getOrDefault(userId, 0) + 1);
    }
    
    /**
     * 사용자별 송금 금액 설정
     * @param userId 사용자 ID
     * @param transferAmount 송금 금액
     */
    public void setUserTransfer(String userId, int transferAmount) {
        userTransferMap.put(userId, 
            userTransferMap.getOrDefault(userId, 0) + transferAmount);
    }
    
    /**
     * 특정 사용자의 총 부담금 조회
     * @param userId 사용자 ID
     * @return 총 부담금
     */
    public int getUserTotal(String userId) {
        return userTotalMap.getOrDefault(userId, 0);
    }
    
    /**
     * 특정 사용자의 송금 금액 조회
     * @param userId 사용자 ID
     * @return 송금 금액 (음수면 받을 돈)
     */
    public int getUserTransfer(String userId) {
        return userTransferMap.getOrDefault(userId, 0);
    }
    
    /**
     * 특정 사용자의 참여 횟수 조회
     * @param userId 사용자 ID
     * @return 참여 횟수
     */
    public int getUserCount(String userId) {
        return userCountMap.getOrDefault(userId, 0);
    }
    
    /**
     * 대표 정산자 자동 계산 (가장 많이 결제한 사람)
     * 내부 로직용이며, UI에는 고정된 이름이 표시됨
     * @param payerCountMap 결제자별 결제 횟수
     */
    public void calculateRepresentative(Map<String, Integer> payerCountMap) {
        String maxUserId = null;
        int maxCount = 0;
        
        for (Map.Entry<String, Integer> entry : payerCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxUserId = entry.getKey();
            }
        }
        
        if (maxUserId != null) {
            this.representativeUserId = maxUserId;
            // 실제 이름은 내부 로직용, UI는 고정값 사용
        }
    }
}
