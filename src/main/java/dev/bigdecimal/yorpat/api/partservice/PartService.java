package dev.bigdecimal.yorpat.api.partservice;

import java.sql.Date;
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
    private final String PROGAM_SERVICE_API_DOMAIN = System.getenv("PROGAM_SERVICE_API_DOMAIN");

    public PartService(PartRepo repo) {
        this.repo = repo;
    }

    @Transactional
    public void createPart(PartModel partModel) throws Exception {
        ProgramModel programModel = new ProgramModel();
        boolean programExists = false;
        try {
            Mono<List<ProgramModel>> responseAsList = webClient
                    .get()
                    .uri(PROGAM_SERVICE_API_DOMAIN + "/programs/getAllPrograms")
                    .exchangeToMono(
                            response -> response.bodyToMono(new ParameterizedTypeReference<List<ProgramModel>>() {
                            }));
            Iterator<ProgramModel> it = responseAsList.block().iterator();
            while (it.hasNext()) {
                programModel = it.next();
                if (partModel.getProgramId().equals(programModel.getProgramId())) {
                    programExists = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!programExists) {
            throw new NoSuchElementException("There is no such program with id of " + partModel.getProgramId());
        }
        PartEntity entity = new PartEntity();
        entity.setPartName(partModel.getPartName());
        entity.setPartRole(partModel.getPartRole());
        entity.setProgramId(programModel.getProgramId());
        entity.setProgramName(programModel.getProgramName());
        entity.setProgramDate(Date.valueOf(programModel.getProgramDate()));
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
    public int updatePart(PartModel partModel) throws Exception {
        ProgramModel programModel = new ProgramModel();
        try {
            Mono<ProgramModel> responseAsProgramModel = webClient
                    .get()
                    .uri(PROGAM_SERVICE_API_DOMAIN
                            + "/programs/getProgram/" + partModel.getProgramId())
                    .exchangeToMono(
                            response -> response.bodyToMono(ProgramModel.class));
            programModel = responseAsProgramModel.block();
        } catch (Exception e) {
            throw e;
        }
        try {
            if (!programModel.getProgramId().equals(null)) {
                return repo.updatePart(partModel.getPartName(), partModel.getPartRole(), partModel.getProgramId(),
                        programModel.getProgramName(), Date.valueOf(
                                programModel.getProgramDate()),
                        partModel.getPartId());
            }
            throw new IllegalAccessException("Valid program id required");
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
