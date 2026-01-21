package com.erp;

import com.erp.global.dto.ErrorResponse;
import com.sun.security.auth.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <H1>응답 견본입니다</H1>
 * 개발끝나고 지워주세오
 */
@RestController("/")
public class SampleController {

    //요청정보를 가져올때 사용합니다.
    @GetMapping("/getRequestUserId")
    public String test(@AuthenticationPrincipal UserPrincipal user){
        return user.getName();
    }

    //데이터가 존재할경우 견본입니다
    @GetMapping("/data")
    public List<String> withData() {
        return List.of("gd");
    }

    //데이터가 없을때 견본입니다.
    @GetMapping("/nodata")
    public ResponseEntity<Void> nodata() {
        return ResponseEntity.noContent().build();
    }

    //예외를 확인하기 위한 코드입니다
    @GetMapping("/e")
    public ResponseEntity<ErrorResponse> isError() {
        throw new RuntimeException("펑펑");
    }

}
