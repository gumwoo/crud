package biz.lunch.vo;

import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

/**
 * 점심비 정산 마스터 VO
 * tb_lunch_master 테이블과 매핑되는 데이터 객체
 * 
 * @author 공통서비스 개발팀
 * @since 2025.01.09
 * @version 1.0
 */
@Data
public class LunchMasterVO {
    
    // ===== 기본 정보 =====
    private int lunchId;                // 점심 내역 ID (PK)
    private String lunchDate;           // 날짜 (YYYY-MM-DD)
    private String storeName;           // 가게명
    private String lunchType;           // 구분 (점심/커피)
    
    // ===== 결제 정보 =====
    private String payerUserId;         // 결제자 사용자 ID
    private String payerUserName;       // 결제자 이름
    private int totalAmount;            // 총 금액
    
    // ===== 정산 정보 =====
    private String settlementStatus;    // 정산 상태 (대기/완료)
    private Timestamp settlementDate;   // 정산 완료 일시
    
    // ===== 기타 =====
    private String description;         // 비고
    
    // ===== 등록/수정 정보 =====
    private String registerUserId;      // 등록자 ID
    private Timestamp registerDate;     // 등록일시
    private String updateUserId;        // 수정자 ID
    private Timestamp updateDate;       // 수정일시
    
    // ===== 조인 데이터 (참여자 리스트) =====
    private List<LunchParticipantVO> participants;  // 참여자 목록
    
    // ===== 화면 표시용 추가 필드 =====
    private int participantCount;       // 참여 인원 수
    private String statusDisplay;       // 상태 표시용 (한글)
    
    /**
     * 정산 상태 확인
     * @return 정산 완료 여부
     */
    public boolean isSettled() {
        return "완료".equals(this.settlementStatus);
    }
    
    /**
     * 점심/커피 구분 확인
     * @return 점심 여부
     */
    public boolean isLunch() {
        return "점심".equals(this.lunchType);
    }
}
