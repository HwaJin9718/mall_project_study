package org.zerock.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.apiserver.dto.PageRequestDTO;
import org.zerock.apiserver.dto.PageResponseDTO;
import org.zerock.apiserver.dto.ProductDTO;
import org.zerock.apiserver.service.ProductService;
import org.zerock.apiserver.util.CustomFileUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;

    private final ProductService productService;

    // 파일 업로드 테스트
//    @PostMapping("/")
//    public Map<String, String> register(ProductDTO productDTO) {
//
//        log.info("register : " + productDTO);
//
//        List<MultipartFile> files = productDTO.getFiles();
//
//        List<String> uploadedFileNames = fileUtil.saveFiles(files);
//
//        productDTO.setUploadFileNames(uploadedFileNames);
//
//        log.info(uploadedFileNames);
//
//        return Map.of("RESULT", "SUCCESS");
//
//    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {

        return productService.getList(pageRequestDTO);

    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileName = fileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileName);

        log.info(uploadFileName);

        Long pno = productService.register(productDTO);

        return  Map.of("result", pno);

    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno) {
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable Long pno, ProductDTO productDTO) {
        
        // 새로 등록되어 이미지 업로드 필요한 이미지는 저장이 필요
        // 기존에 저장되어 업로드된 파일(DB에 이미 저장된 파일)에 새로 등록된 이미지 저장 필요

        productDTO.setPno(pno);

        // 이 후 삭제된 파일을 알 기 위해 기존 DB에 등록된 파일을 조회해가지고 옴
        ProductDTO oldProductDTO = productService.get(pno);

        // 새로은 파일 업로드 진행
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        // 기존에 저장되고 이번에 수정하지 않아 지우지 않을 파일 = 계속 남아있을 파일
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        // 파일 목록 만들기 -> 현재 등록 필요한 파일 등록
        if(currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        productService.modify(productDTO);

        // 이제 필요없는 파일 삭제
        List<String> oldFIleNames = oldProductDTO.getUploadFileNames();
        if(oldFIleNames != null && oldFIleNames.size() > 0) {

            List<String> removeFiles =
                    oldFIleNames.stream().filter(fileName ->
                            uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());

            fileUtil.deleteFiles(removeFiles);

        }

        return Map.of("RESULT", "SUCCESS");
        
    }

    // 원래 삭제 기능은 없음, 다만 delFlag 값을 수정하는 것으로 보이지 않도록 처리함
    // 지금은 연습 하라고 하는 부분!!
    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable Long pno) {

        List<String> oldFileNames = productService.get(pno).getUploadFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");

    }

}
