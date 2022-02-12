package bell.avishkar.recog.project.storagemanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CollectionStats {

    private String collectionName;
    private Long numberOfImages;

}
