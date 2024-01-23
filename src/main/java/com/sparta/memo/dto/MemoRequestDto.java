package com.sparta.memo.dto;

import lombok.Getter;

@Getter
public class MemoRequestDto {
    private String username;
    private String contents;
    private String title;
    private String password;
}