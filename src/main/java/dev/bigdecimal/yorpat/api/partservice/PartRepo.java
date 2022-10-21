package dev.bigdecimal.yorpat.api.partservice;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PartRepo extends CrudRepository<PartEntity, Long> {
    @Modifying
    @Query(value = "UPDATE PartEntity p SET p.partName = :partName, p.partRole = :partRole, p.programId = :programId WHERE p.partId = :partId")
    public int updatePart(@Param("partName") String partName, @Param("partRole") String partRole,
            @Param("programId") Long programId, @Param("partId") Long partId);
}
