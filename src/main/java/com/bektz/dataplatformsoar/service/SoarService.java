package com.bektz.dataplatformsoar.service;

import com.bektz.dataplatformsoar.configs.SoarConfiguration;
import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.SqlVerifyResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SoarService {

    private static final String CMD = "%s/soar '-query=%s'";

    @Value("${soar-configs.execuable_path}")
    private String soarPath;

    @Autowired
    private SoarConfiguration soarConfiguration;

    public SqlVerifyResp verify(SqlVerifyReq sqlVerifyReq) {
        String[] cmds = {"/bin/sh", "-c", String.format(CMD, soarPath, sqlVerifyReq.getSql())};
        Map<String, SoarConfiguration.Skill> skillsMap = soarConfiguration.getSkillsMap();
        List<String> processList = new ArrayList<>();
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmds);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                processList.add(line);
            }
            input.close();
        } catch (IOException e) {
        }
        String sqlInfo = String.join("<br>", processList);
        return SqlVerifyResp.builder().verifyInfo(sqlInfo).build();
    }
}
