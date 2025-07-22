package com.unionsearch.ver2.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "positions") // 'positions'라는 테이블과 매핑
public class Position {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title; // 포지션명
  private String company; // 회사명
  private String level; // 직급
  private String status; // 모집 상태 (예: 모집중)
  private LocalDate date; // 등록일

  @Column(columnDefinition = "TEXT")
  private String description; // 포지션 설명

  // Getter, Setter
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
