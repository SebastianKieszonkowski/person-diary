package pl.kurs.persondiary.services.creators;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.kurs.persondiary.command.singleCommand.CreatePensionerCommand;
import pl.kurs.persondiary.models.Pensioner;

@Service
@Validated
public class PensionerServiceCreator {

    public CreatePensionerCommand preparePensionerCommand(JsonNode personJsonNode){
        return CreatePensionerCommand.builder()
                .type(personJsonNode.get("type").asText())
                .firstName(personJsonNode.get("firstName").asText())
                .lastName(personJsonNode.get("lastName").asText())
                .pesel(personJsonNode.get("pesel").asText())
                .height(personJsonNode.get("height").asDouble())
                .weight(personJsonNode.get("weight").asDouble())
                .email(personJsonNode.get("email").asText())
                .pension(personJsonNode.get("pension").asDouble())
                .workedYears(personJsonNode.get("workedYears").asInt())
                .build();
    }
    public Pensioner createPensioner(@Valid CreatePensionerCommand createPensionerCommand) {
        return Pensioner.builder()
                .firstName(createPensionerCommand.getFirstName())
                .lastName(createPensionerCommand.getLastName())
                .pesel(createPensionerCommand.getPesel())
                .height(createPensionerCommand.getHeight())
                .weight(createPensionerCommand.getWeight())
                .email(createPensionerCommand.getEmail())
                .pension(createPensionerCommand.getPension())
                .workedYears(createPensionerCommand.getWorkedYears())
                .build();
    }
}