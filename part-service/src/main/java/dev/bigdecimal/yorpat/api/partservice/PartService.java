package dev.bigdecimal.yorpat.api.partservice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartService {
    private final PartRepo repo;

    public PartService(PartRepo repo) {
        this.repo = repo;
    }

    @Transactional
    public void createPart(PartModel model) throws Exception {
        PartEntity entity = new PartEntity();
        entity.setPartName(model.getPartName());
        entity.setPartRole(model.getPartRole());
        entity.setProgramId(model.getProgramId());
        try {
            repo.save(entity);
        } catch (Exception e) {
            throw e;
        }
    }
}
