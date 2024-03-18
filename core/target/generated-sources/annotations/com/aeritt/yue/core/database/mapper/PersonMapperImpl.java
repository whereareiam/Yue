package com.aeritt.yue.core.database.mapper;

import com.aeritt.yue.api.model.PersonBE;
import com.aeritt.yue.core.database.entity.Language;
import com.aeritt.yue.core.database.entity.person.Person;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-17T15:02:47+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (BellSoft)"
)
@Component
public class PersonMapperImpl implements PersonMapper {

    @Override
    public PersonBE personToPersonBE(Person person) {
        if ( person == null ) {
            return null;
        }

        String mainLanguage = null;
        String name = null;
        String id = null;
        String globalName = null;

        mainLanguage = personMainLanguageName( person );
        name = person.getName();
        id = person.getId();
        globalName = person.getGlobalName();

        PersonBE personBE = new PersonBE( id, globalName, name, mainLanguage );

        return personBE;
    }

    @Override
    public Person personBEToPerson(PersonBE personBE) {
        if ( personBE == null ) {
            return null;
        }

        Person person = new Person();

        person.setMainLanguage( personBEToLanguage( personBE ) );
        person.setId( personBE.getId() );
        person.setGlobalName( personBE.getGlobalName() );
        person.setName( personBE.getName() );

        return person;
    }

    private String personMainLanguageName(Person person) {
        if ( person == null ) {
            return null;
        }
        Language mainLanguage = person.getMainLanguage();
        if ( mainLanguage == null ) {
            return null;
        }
        String name = mainLanguage.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    protected Language personBEToLanguage(PersonBE personBE) {
        if ( personBE == null ) {
            return null;
        }

        Language language = new Language();

        language.setName( personBE.getMainLanguage() );

        return language;
    }
}
