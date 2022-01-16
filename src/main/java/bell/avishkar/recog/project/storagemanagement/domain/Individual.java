package bell.avishkar.recog.project.storagemanagement.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor // No arg constructor
@AllArgsConstructor // All arg constructor
@Data // getter setter string equals
@Builder // Builder design pattern
@Entity // Database entity
public class Individual {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String identifier;
    private String identifierType;
    private String fileName;
    private String externalImageId;
}
