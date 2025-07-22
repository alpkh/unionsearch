package com.unionsearch.ver2.controller;

import com.unionsearch.ver2.entity.Position;
import com.unionsearch.ver2.service.PositionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    // 모든 포지션 가져오기
    @GetMapping
    public List<Position> getAllPositions() {
        return positionService.findAllPositions();
    }

    // 특정 포지션 가져오기
    @GetMapping("/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable Long id) {
        Optional<Position> position = positionService.findPositionById(id);
        return position.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 새 포지션 생성
    @PostMapping
    public ResponseEntity<Position> createPosition(@RequestBody Position position) {
        Position newPosition = positionService.save(position);
        return ResponseEntity.ok(newPosition);
    }

    // 포지션 수정
    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(@PathVariable Long id, @RequestBody Position positionDetails) {
        Position updatedPosition = positionService.update(id, positionDetails);
        return ResponseEntity.ok(updatedPosition);
    }

    // 포지션 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        positionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
