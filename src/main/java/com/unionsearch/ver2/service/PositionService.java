package com.unionsearch.ver2.service;

import com.unionsearch.ver2.entity.Position;
import com.unionsearch.ver2.repository.PositionRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

  private final PositionRepository positionRepository;

  public PositionService(PositionRepository positionRepository) {
    this.positionRepository = positionRepository;
  }

  // 기존의 개별 포지션 저장 메서드 (웹에서 직접 작성)
  public Position save(Position position) {
    return positionRepository.save(position);
  }

  // 모든 포지션 조회
  public List<Position> findAllPositions() {
    return positionRepository.findAll();
  }

  // 특정 포지션 조회
  public Optional<Position> findPositionById(Long id) {
    return positionRepository.findById(id);
  }

  // 포지션 업데이트
  public Position update(Long id, Position positionDetails) {
    Position existingPosition = positionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Position not found with id: " + id));

    existingPosition.setTitle(positionDetails.getTitle());
    existingPosition.setCompany(positionDetails.getCompany());
    existingPosition.setLevel(positionDetails.getLevel());
    existingPosition.setStatus(positionDetails.getStatus());
    existingPosition.setDate(positionDetails.getDate());
    existingPosition.setDescription(positionDetails.getDescription());

    return positionRepository.save(existingPosition);
  }

  // 포지션 삭제 메서드
  public void deleteById(Long id) {
    positionRepository.deleteById(id);
  }

  // 파일 업로드 시 포지션 데이터 덮어쓰기 또는 추가하기
  public void saveOrUpdatePositions(MultipartFile file, boolean overwrite) throws IOException {
    List<Position> positions = parseExcelFile(file);

    if (overwrite) {
      positionRepository.deleteAll(); // 기존 데이터 삭제
    }

    positionRepository.saveAll(positions); // 새 데이터 추가
  }

  // Excel 파일을 읽어 Position 리스트로 변환하는 메서드
  private List<Position> parseExcelFile(MultipartFile file) throws IOException {
    List<Position> positions = new ArrayList<>();
    Workbook workbook = new XSSFWorkbook(file.getInputStream());
    Sheet sheet = workbook.getSheetAt(0);

    for (Row row : sheet) {
      if (row.getRowNum() == 0)
        continue; // 첫 번째 행은 헤더로 간주하고 건너뜀

      Position position = new Position();
      position.setTitle(row.getCell(0).getStringCellValue());
      position.setCompany(row.getCell(1).getStringCellValue());
      position.setLevel(row.getCell(2).getStringCellValue());
      position.setStatus(row.getCell(3).getStringCellValue());

      // 날짜 형식 확인 및 설정
      Cell dateCell = row.getCell(4);
      if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
        position.setDate(dateCell.getLocalDateTimeCellValue().toLocalDate());
      } else {
        position.setDate(null); // 날짜가 없는 경우 null 설정
      }

      position.setDescription(row.getCell(5).getStringCellValue());
      positions.add(position);
    }

    workbook.close();
    return positions;
  }

  // 포지션 데이터 다운로드용 Excel 생성 메서드
  public byte[] exportPositionsToExcel() throws IOException {
    List<Position> positions = positionRepository.findAll();
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Positions");

    // Header Row
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("Title");
    headerRow.createCell(1).setCellValue("Company");
    headerRow.createCell(2).setCellValue("Level");
    headerRow.createCell(3).setCellValue("Status");
    headerRow.createCell(4).setCellValue("Date");
    headerRow.createCell(5).setCellValue("Description");

    // Data Rows
    int rowNum = 1;
    for (Position position : positions) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(0).setCellValue(position.getTitle());
      row.createCell(1).setCellValue(position.getCompany());
      row.createCell(2).setCellValue(position.getLevel());
      row.createCell(3).setCellValue(position.getStatus());
      row.createCell(4).setCellValue(position.getDate() != null ? position.getDate().toString() : "");
      row.createCell(5).setCellValue(position.getDescription());
    }

    // Convert Workbook to Byte Array
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    workbook.write(out);
    workbook.close();

    return out.toByteArray();
  }
}
