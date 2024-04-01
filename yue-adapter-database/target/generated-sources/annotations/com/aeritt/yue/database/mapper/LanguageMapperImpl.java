package com.aeritt.yue.database.mapper;

import com.aeritt.yue.api.model.Language;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-01T15:19:55+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (BellSoft)"
)
@Component
public class LanguageMapperImpl implements LanguageMapper {

    @Override
    public Language mapEntity(com.aeritt.yue.database.entity.Language language) {
        if ( language == null ) {
            return null;
        }

        String name = null;
        String code = null;

        name = language.getName();
        code = language.getCode();

        Language language1 = new Language( name, code );

        return language1;
    }
}
