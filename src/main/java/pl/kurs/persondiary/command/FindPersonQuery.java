package pl.kurs.persondiary.command;


import lombok.*;

@Data
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
    private String gender;
    private Double salaryFrom;
    private Double salaryTo;
    private Integer ageFrom;
    private Integer ageTo;
    private String universityName;
    private Integer workedYearsFrom;
    private Integer workedYearsTo;
    //dodac kolejne dla poszczegulnych typów osób
}