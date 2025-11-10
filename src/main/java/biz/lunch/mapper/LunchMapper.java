package biz.lunch.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

/**
 * 점심비 정산 Mapper (설계도 기반 - Map 전용)
 * 
 * @author 공통서비스 개발팀
 * @since 2025.01.09
 * @version 2.0
 */
@Mapper("lunchMapper")
public interface LunchMapper {
    
    /**
     * 점심/커피 내역 등록
     * @param params 등록 정보 Map
     * @return 등록된 건수
     */
    int registerLunch(Map<String, Object> params);
    
    /**
     * 점심/커피 내역 수정
     * @param params 수정 정보 Map
     * @return 수정된 건수
     */
    int updateLunch(Map<String, Object> params);
    
    /**
     * 점심/커피 내역 삭제
     * @param id 삭제할 내역 ID
     * @return 삭제된 건수
     */
    int deleteLunch(int id);
    
    /**
     * 점심/커피 내역 목록 조회
     * @param params 조회 조건 Map
     * @return 내역 목록
     */
    List<Map<String, Object>> getLunchList(Map<String, Object> params);
    
    /**
     * 통계 조회
     * @return 통계 데이터 Map
     */
    Map<String, Object> getStatistics();
    
    /**
     * 정산 완료 처리
     * @param params 정산 정보 Map
     * @return 처리된 건수
     */
    int completeSettlement(Map<String, Object> params);
}
