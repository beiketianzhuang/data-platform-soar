package com.bektz.dataplatformsoar.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchemaResp {
    private String schema;
    private Map<String, List<String>> tables;
}
