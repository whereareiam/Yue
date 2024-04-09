package com.aeritt.yue.database.mapper;

import com.aeritt.yue.api.model.UserProfile;
import com.aeritt.yue.database.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
		uses = LanguageMapper.class
)
public interface UserMapper {
	@Mapping(source = "mainLanguage", target = "language")
	UserProfile map(User user);
}

