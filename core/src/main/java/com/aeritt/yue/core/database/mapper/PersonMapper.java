package com.aeritt.yue.core.database.mapper;

import com.aeritt.yue.api.model.PersonBE;
import com.aeritt.yue.core.database.entity.person.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PersonMapper {
	@Mapping(source = "mainLanguage.name", target = "mainLanguage")
	PersonBE personToPersonBE(Person person);

	@Mapping(source = "mainLanguage", target = "mainLanguage.name")
	Person personBEToPerson(PersonBE personBE);
}
