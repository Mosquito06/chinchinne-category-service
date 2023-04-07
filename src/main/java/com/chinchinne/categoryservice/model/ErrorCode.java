package com.chinchinne.categoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode
{
     INVALID_REQUEST(1020, "잘못된 요청입니다.")
    ,NOT_FOUND_RECORD(1021, "존재하지 않는 데이터 입니다.")
    ,USING_RECORD(2022, "사용 중인 데이터 입니다.");

    private final int code;
    private final String detail;
}
