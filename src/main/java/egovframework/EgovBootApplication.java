package egovframework;

import org.mybatis.spring.annotation.MapperScan; // 👈 import 확인
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"egovframework", "biz"}) // 👈 이것은 @Service, @Controller 등을 스캔 (유지)
@MapperScan(basePackages = { // 👈 [수정] Mapper 인터페이스가 있는 패키지만 정확히 지정
        "egovframework.com.cmm.dao",
        "biz.file.dao",
        "biz.login.dao",
        "biz.lunch.dao",
        "biz.menu.dao"
})
public class EgovBootApplication extends SpringBootServletInitializer {
	
	/**
	 * 외장 톰캣에서 실행될 때 사용되는 설정 메서드
	 * WAR 파일로 배포 시 외장 톰캣이 이 메서드를 호출하여 애플리케이션을 초기화
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EgovBootApplication.class);
	}
	
	/**
	 * 내장 톰캣으로 실행될 때 사용되는 메인 메서드
	 * 개발 환경에서 직접 실행할 때 사용
	 */
	public static void main(String[] args) {
		log.debug("##### EgovBootApplication Start #####");

		SpringApplication springApplication = new SpringApplication(EgovBootApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		//springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		log.debug("##### EgovBootApplication End #####");
	}

}
