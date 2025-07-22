package com.unionsearch.ver2.controller;

import com.unionsearch.ver2.entity.Seminar;
import com.unionsearch.ver2.repository.SeminarRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seminars")
public class SeminarController {

  private final SeminarRepository seminarRepository;

  public SeminarController(SeminarRepository seminarRepository) {
    this.seminarRepository = seminarRepository;
  }

  @GetMapping
  public List<Seminar> getAllSeminars() {
    return seminarRepository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Seminar> getSeminarById(@PathVariable Long id) {
    return seminarRepository.findById(id)
        .map(seminar -> ResponseEntity.ok().body(seminar))
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Seminar createSeminar(@RequestBody Seminar seminar) {
    return seminarRepository.save(seminar);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Seminar> updateSeminar(@PathVariable Long id, @RequestBody Seminar seminarDetails) {
    return seminarRepository.findById(id)
        .map(seminar -> {
          seminar.setTitle(seminarDetails.getTitle());
          seminar.setDate(seminarDetails.getDate());
          seminar.setPresentationDate(seminarDetails.getPresentationDate());
          seminar.setLocation(seminarDetails.getLocation());
          seminar.setPresenter(seminarDetails.getPresenter());
          seminar.setRegistrar(seminarDetails.getRegistrar());
          Seminar updatedSeminar = seminarRepository.save(seminar);
          return ResponseEntity.ok().body(updatedSeminar);
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSeminar(@PathVariable Long id) {
    return seminarRepository.findById(id)
        .map(seminar -> {
          seminarRepository.delete(seminar);
          return ResponseEntity.ok().<Void>build(); // 명시적으로 Void를 반환
        })
        .orElseGet(() -> ResponseEntity.notFound().build()); // notFound 응답 처리
  }

}
