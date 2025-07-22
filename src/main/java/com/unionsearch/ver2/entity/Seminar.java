package com.unionsearch.ver2.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "seminars")
public class Seminar {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private LocalDate date;
  private LocalDate presentationDate;
  private String location;
  private String presenter;
  private String registrar;

  // Getters and Setters
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

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public LocalDate getPresentationDate() {
    return presentationDate;
  }

  public void setPresentationDate(LocalDate presentationDate) {
    this.presentationDate = presentationDate;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getPresenter() {
    return presenter;
  }

  public void setPresenter(String presenter) {
    this.presenter = presenter;
  }

  public String getRegistrar() {
    return registrar;
  }

  public void setRegistrar(String registrar) {
    this.registrar = registrar;
  }
}
