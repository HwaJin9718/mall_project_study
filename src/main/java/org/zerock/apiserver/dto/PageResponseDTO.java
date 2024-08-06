package org.zerock.apiserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {

    // DTO 목록 데이터
    private List<E> dtoList;

    private List<Integer> pageNumList;

    private PageRequestDTO pageRequestDTO;

    // prev : 이전 화살표 , next : 다음 화살표
    private boolean prev, next;

    // totalCount : 총 데이터 , prevPage : 이전 페이지(시작번호 - 1) , nextPage : 다음 페이지(시작번호 + 1) , totalPage : 전체 페이지 , current : 현재 페이지
    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long total) {

        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int) total;

        // 끝(end) 페이지 계산
        int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;

        // 시작(start) 페이지 계산
        int start = end - 9;

        // 진짜 마지막 페이지 (총 페이지)
        int last = (int) (Math.ceil(totalCount/(double)pageRequestDTO.getSize()));

        end = end > last ? last : end;

        this.prev = start > 1;

        this.next = totalCount > end * pageRequestDTO.getSize();

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        this.prevPage = prev ? start - 1 : 0;

        this.nextPage = next ? end + 1 : 0;

        this.totalPage = this.pageNumList.size();

        this.current = pageRequestDTO.getPage();

    }

}
