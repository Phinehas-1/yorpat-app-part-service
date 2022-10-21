package dev.bigdecimal.yorpat.api.partservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class PartService {
    private final PartRepo repo;
    private final WebClient webClient = WebClient.builder()
            .build();

    public PartService(PartRepo repo) {
        this.repo = repo;
    }

    @Transactional
    public void createPart(PartModel model) throws Exception {
        boolean programExists = false;
        try {
            Mono<List<ProgramModel>> responseAsList = webClient
                    .get()
                    .uri("http://localhost:8083/programs/getAllPrograms")
                    .exchangeToMono(
                            response -> response.bodyToMono(new ParameterizedTypeReference<List<ProgramModel>>() {
                            }));
            Iterator<ProgramModel> it = responseAsList.block().iterator();
            while (it.hasNext()) {
                if (model.getProgramId().equals(it.next().getProgramId())) {
                    programExists = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!programExists) {
            throw new NoSuchElementException("There is no such program with id of " + model.getProgramId());
        }
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

    @Transactional
    public List<PartEntity> getParts(Long programId) throws Exception {
        List<Long> programIdList = Arrays.asList(programId);
        List<PartEntity> parts;
        try {
            parts = (List<PartEntity>) repo.findAllById(programIdList);
            return parts;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public PartEntity getPartByPartId(Long partId) throws NoSuchElementException, Exception {
        try {
            return repo.findById(partId).orElseThrow();
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public int updatePart(PartModel part) throws Exception {
        try {
            return repo.updatePart(part.getPartName(), part.getPartRole(), part.getProgramId(), part.getPartId());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public boolean deletePart(Long partId) throws Exception {
        try {
            repo.deleteById(partId);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
}
