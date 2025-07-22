package com.unionsearch.ver2.controller;

import com.unionsearch.ver2.service.PositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/positions/file")
public class PositionFileController {

  private final PositionService positionService;

  public PositionFileController(PositionService positionService) {
    this.positionService = positionService;
  }

  // 파일 업로드로 포지션 데이터 덮어쓰기 및 추가하기 기능
  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(
      @RequestParam("file") MultipartFile file,
      @RequestParam("overwrite") boolean overwrite) {
    try {
      positionService.saveOrUpdatePositions(file, overwrite);
      String message = overwrite ? "기존 데이터를 덮어쓰고 업로드가 완료되었습니다." : "기존 데이터에 추가되었습니다.";
      return ResponseEntity.ok(message);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 처리 실패");
    }
  }
}
