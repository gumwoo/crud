package biz.lunch.vo;

import lombok.Data;
import java.sql.Timestamp;

/**
 * 점심비 정산 참여자 VO
 * tb_lunch_participant 테이블과 매핑되는 데이터 객체
 * 
 * @author 공통서비스 개발팀
 * @since 2025.01.09
 * @version 1.0
 */
@Data
public class LunchParticipantVO {
    
    // ===== 기본 정보 =====
    private int participantId;          // 참여자 ID (PK)
    private int lunchId;                // 점심 내역 ID (FK)
    
    // ===== 사용자 정보 =====
    private String userId;              // 참여자 사용자 ID
    private String userName;            // 참여자 이름
    
    // ===== 금액 정보 =====
    private int amount;                 // 개인 부담금
    
    // ===== 등록 정보 =====
    private Timestamp registerDate;     // 등록일시
    
    // ===== 계산용 추가 필드 (DB에 없음, 로직에서 계산) =====
    private int transferAmount;         // 송금할 금액 (계산 결과)
    private boolean isPayer;            // 결제자 여부
    
    /**
     * 송금 금액 계산 (결제자 여부에 따라)
     * @param totalAmount 전체 금액
     * @param isPayer 결제자 여부
     */
    public void calculateTransferAmount(int totalAmount, boolean isPayer) {
        if (isPayer) {
            // 결제자: 개인 부담금 - 전체 금액 = 음수 (받을 돈)
            this.transferAmount = this.amount - totalAmount;
        } else {
            // 비결제자: 개인 부담금 그대로 (보낼 돈)
            this.transferAmount = this.amount;
        }
        this.isPayer = isPayer;
    }
    
    /**
     * 받을 돈인지 확인
     * @return 받을 돈 여부 (음수)
     */
    public boolean isReceiver() {
        return this.transferAmount < 0;
    }
    
    /**
     * 송금 금액 절대값 반환 (화면 표시용)
     * @return 송금 금액 절대값
     */
    public int getAbsTransferAmount() {
        return Math.abs(this.transferAmount);
    }
}
