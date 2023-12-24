package pl.kurs.persondiary.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindPersonQuery {

    private String type;
    private String firstName;
    private String lastName;
    private String pesel;
    private Double heightFrom;
    private Double heightTo;
    private Double weightFrom;
    private Double weightTo;
    private String email;
    private Integer versionFrom;
    private Integer versionTo;
    private String gender;
    private Integer ageFrom;
    private Integer ageTo;
    private Double salaryFrom;
    private Double salaryTo;
    private String position;
    private String universityName;
    private Integer studyYearFrom;
    private Integer studyYeaTo;
    private String studyField;
    private Double scholarshipFrom;
    private Double scholarshipTo;
    private Double pensionFrom;
    private Double pensionTo;
    private Integer workedYearsFrom;
    private Integer workedYearsTo;
}