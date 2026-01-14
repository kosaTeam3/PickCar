package com.erp.domain.employee.entity;

import com.erp.global.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BranchCode {
    HQ("000", "본사"),
    GARAK("001", "가락"),
    WANGSIMNI("002","왕십리"),
    SADANG("003", "사당"),
    SINDORIM("004", "신도림"),
    NOWON("005", "노원"),
    GONGDEOK("006", "공덕");

    private final String code;
    private final String kName;

    public static String getCodeByName(String name){

        for ( BranchCode branch : BranchCode.values()) {

            if (branch.getKName().equals(name)){
                return branch.getCode();
            }
        }
        throw new CustomException(404, "해당 지점이 없습니다." + name);

//        401 : (Unauthorized) - 인증 문제
//        400 : (Bad Request) - 잘못된 요청
//        404 : (Not Found) - 찾는 데이터가 없음
    }
}
