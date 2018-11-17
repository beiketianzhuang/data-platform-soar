package com.bektz.dataplatformsoar.resp;

import com.bektz.dataplatformsoar.constants.QueryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class JdbcResultResp {
    @Builder.Default
    private List<String> resultMeta = new ArrayList<>();
    @Builder.Default
    private List<Map<String, Object>> result = new ArrayList<>();
    private QueryStatus queryStatus;
    @Builder.Default
    private Long queryTimeInMs = 0L;
    private String queryErrorMsg;
    private String queryResultMsg;

}