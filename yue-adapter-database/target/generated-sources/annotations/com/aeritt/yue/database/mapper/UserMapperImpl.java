package com.aeritt.yue.database.mapper;

import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.model.UserProfile;
import com.aeritt.yue.database.entity.user.User;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-14T16:16:22+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22 (BellSoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private LanguageMapper languageMapper;

    @Override
    public UserProfile map(User user) {
        if ( user == null ) {
            return null;
        }

        Language language = null;
        String id = null;
        String globalName = null;
        String name = null;

        language = languageMapper.modelToEntity( user.getMainLanguage() );
        id = user.getId();
        globalName = user.getGlobalName();
        name = user.getName();

        List<Language> additionalLanguages = null;
        List<String> roles = null;

        UserProfile userProfile = new UserProfile( id, language, additionalLanguages, roles, globalName, name );

        return userProfile;
    }
}
