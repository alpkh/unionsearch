package com.unionsearch.ver2.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "talents")
public class Talent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date; // 일시

    @Column(columnDefinition = "TEXT")
    private String position; // 포지션

    @Column
    private String name; // 성명

    @Column(name = "birth_year")
    private String birthYear; // 생년

    @Column
    private String phone; // 전화번호

    @Column
    private String email; // 이메일

    @Column(columnDefinition = "TEXT")
    private String school; // 학교

    @Column(columnDefinition = "TEXT")
    private String experience; // 경력

    @Column(columnDefinition = "TEXT")
    private String expertise; // 전문분야

    @Column(name = "status", columnDefinition = "TEXT")
    private String status; // 진행사항

    @Column(columnDefinition = "TEXT")
    private String source; // 입수경로

    @Column(name = "resume_number", columnDefinition = "TEXT")
    private String resumeNumber; // 이력서번호

    @Column(name = "contact_person")
    private String contactPerson; // 컨택담당자

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription; // 업무내용

    @Column(columnDefinition = "TEXT")
    private String qualifications; // 자격요건

    @Column(name = "major_and_career", columnDefinition = "TEXT")
    private String majorAndCareer; // 관련전공 및 경력

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getResumeNumber() {
        return resumeNumber;
    }

    public void setResumeNumber(String resumeNumber) {
        this.resumeNumber = resumeNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getMajorAndCareer() {
        return majorAndCareer;
    }

    public void setMajorAndCareer(String majorAndCareer) {
        this.majorAndCareer = majorAndCareer;
    }
}
