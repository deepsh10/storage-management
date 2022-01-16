package bell.avishkar.recog.project.storagemanagement.repository;

import bell.avishkar.recog.project.storagemanagement.domain.Individual;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualRepository extends CrudRepository <Individual, Long> {

    Individual findByIdentifierAndIdentifierType(String identifier, String identifierType);

    Individual findByExternalImageId(String externalImageId);

}
