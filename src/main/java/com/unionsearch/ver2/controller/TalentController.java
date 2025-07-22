package com.unionsearch.ver2.controller;

import com.unionsearch.ver2.entity.Talent;
import com.unionsearch.ver2.repository.TalentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/talents")
public class TalentController {

  private final TalentRepository talentRepository;

  public TalentController(TalentRepository talentRepository) {
    this.talentRepository = talentRepository;
  }

  @GetMapping
  public List<Talent> getAllTalents() {
    return talentRepository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Talent> getTalentById(@PathVariable Long id) {
    return talentRepository.findById(id)
        .map(talent -> ResponseEntity.ok().body(talent))
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Talent createTalent(@RequestBody Talent talent) {
    return talentRepository.save(talent);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Talent> updateTalent(@PathVariable Long id, @RequestBody Talent talentDetails) {
    return talentRepository.findById(id)
        .map(talent -> {
          talent.setDate(talentDetails.getDate());
          talent.setPosition(talentDetails.getPosition());
          talent.setName(talentDetails.getName());
          talent.setBirthYear(talentDetails.getBirthYear());
          talent.setPhone(talentDetails.getPhone());
          talent.setEmail(talentDetails.getEmail());
          talent.setSchool(talentDetails.getSchool());
          talent.setExperience(talentDetails.getExperience());
          talent.setExpertise(talentDetails.getExpertise());
          talent.setStatus(talentDetails.getStatus());
          talent.setSource(talentDetails.getSource());
          talent.setResumeNumber(talentDetails.getResumeNumber());
          talent.setContactPerson(talentDetails.getContactPerson());
          talent.setJobDescription(talentDetails.getJobDescription());
          talent.setQualifications(talentDetails.getQualifications());
          talent.setMajorAndCareer(talentDetails.getMajorAndCareer());

          Talent updatedTalent = talentRepository.save(talent);
          return ResponseEntity.ok().body(updatedTalent);
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTalent(@PathVariable Long id) {
    return talentRepository.findById(id)
        .map(talent -> {
          talentRepository.delete(talent);
          return ResponseEntity.ok().<Void>build();
        })
        .orElse(ResponseEntity.notFound().build());
  }
}
