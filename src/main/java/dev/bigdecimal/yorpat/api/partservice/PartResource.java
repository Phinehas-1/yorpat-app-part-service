package dev.bigdecimal.yorpat.api.partservice;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("parts")
public class PartResource {
    private final PartService service;
    private final ResponseEntity<? extends Object> requestErrorResponse = new ResponseEntity<>(
            "Request body/parameter(s) has unexpected value(s)", HttpStatus.BAD_REQUEST);

    public PartResource(PartService service) {
        this.service = service;
    }

    // create a program part
    @PostMapping("/createPart")
    public ResponseEntity<? extends Object> createPart(@RequestBody PartModel part) {
        if (!part.equals(null) && !part.getProgramId().equals(null)) {
            try {
                service.createPart(part);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return requestErrorResponse;
    }

    // fetch a program part by its part id
    @GetMapping("/getPart/{partId}")
    public ResponseEntity<? extends Object> getPart(@PathVariable Long partId) {
        if (!partId.equals(null)) {
            try {
                PartEntity part = service.getPartByPartId(partId);
                return new ResponseEntity<PartEntity>(part, HttpStatus.FOUND);
            } catch (Exception e) {
                return new ResponseEntity<String>("No part found.", HttpStatus.NOT_FOUND);
            }
        }
        return requestErrorResponse;
    }

    // fetch all the available parts by program id
    @GetMapping("/getParts/{programId}")
    public ResponseEntity<? extends Object> getParts(@PathVariable Long programId) {
        if (!programId.equals(null)) {
            try {
                List<PartEntity> parts = service.getParts(programId);
                if (!parts.equals(null)) {
                    return new ResponseEntity<List<PartEntity>>(parts, HttpStatus.FOUND);
                }
                return new ResponseEntity<String>("No part found.", HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return requestErrorResponse;
    }

    // update a program part
    @PutMapping("/updatePart")
    public ResponseEntity<? extends Object> updatePart(@RequestBody PartModel part) {
        if (!part.equals(null) && !part.getPartId().equals(null)) {
            try {
                if (service.updatePart(part) > 0) {
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);
                }
            } catch (Exception e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return requestErrorResponse;
    }

    // delete a program part
    @DeleteMapping("/removePart/{partId}")
    public ResponseEntity<? extends Object> deletePart(@PathVariable Long partId) {
        if (!partId.equals(null)) {
            try {
                if (service.deletePart(partId)) {
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);
                }
            } catch (Exception e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return requestErrorResponse;
    }
}