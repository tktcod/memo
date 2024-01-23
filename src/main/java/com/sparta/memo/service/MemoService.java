package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
        Memo saveMemo = memoRepository.save(memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(saveMemo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        // DB 조회
        return memoRepository.findAllByOrderByModifiedAtDesc().stream().map(MemoResponseDto::new).toList();
    }
    @Transactional
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        // 비밀번호 확인
        boolean isPasswordCorrect = Objects.equals(requestDto.getPassword(), memo.getPassword());

        // memo 내용 수정
        if(isPasswordCorrect) {
            memo.update(requestDto);
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return id;

    }

    public Long deleteMemo(Long id, String password) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        // 비밀번호 확인
        boolean isPasswordCorrect = Objects.equals(password, memo.getPassword());

        // memo 삭제
        if(isPasswordCorrect) {
            memoRepository.delete(memo);
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


        return id;

    }

    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다."));
    }
}