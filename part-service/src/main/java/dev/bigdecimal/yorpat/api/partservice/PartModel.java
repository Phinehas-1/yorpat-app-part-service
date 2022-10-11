package dev.bigdecimal.yorpat.api.partservice;

import lombok.Data;

@Data
public class PartModel {
    private Long partId;
    private String partName;
    private String partRole;
    private Long programId;
}
