package dev.bigdecimal.yorpat.api.partservice;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PartRepo extends CrudRepository<PartEntity, Long> {
    @Modifying
    @Query(value = "UPDATE PartEntity p SET p.partName = :partName, p.partRole = :partRole, p.programId = :programId, p.programName = :programName, p.programDate = :programDate WHERE p.partId = :partId")
    public int updatePart(@Param("partName") String partName, @Param("partRole") String partRole,
            @Param("programId") Long programId, @Param("programName") String programName,
            @Param("programDate") Date programDate, @Param("partId") Long partId);

    @Query(value = "SELECT p FROM PartEntity p WHERE p.programId = :programId")
    public List<PartEntity> getPartsByProgramId(@Param("programId") Long programId);
}
