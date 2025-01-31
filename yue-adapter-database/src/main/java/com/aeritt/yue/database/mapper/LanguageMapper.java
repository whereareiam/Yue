package com.aeritt.yue.database.mapper;

import com.aeritt.yue.api.model.Language;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface LanguageMapper {
	@Mapping(source = "name", target = "name")
	@Mapping(source = "code", target = "code")
	Language modelToEntity(com.aeritt.yue.database.entity.Language language);

	@Mapping(source = "name", target = "name")
	@Mapping(source = "code", target = "code")
	com.aeritt.yue.database.entity.Language entityToModel(Language language);
}
