package com.bektz.dataplatformsoar.service;

import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.SqlVerifyResp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class SoarService {

    private static final String CMD = "%s/soar '-query=%s'";

    @Value("${soar_configs.execuable_path}")
    private String soarPath;

    public SqlVerifyResp verify(SqlVerifyReq sqlVerifyReq) {
        String[] cmds = {"/bin/sh", "-c", String.format(CMD, soarPath, sqlVerifyReq.getSql())};
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
        return SqlVerifyResp.builder().verifyInfo(processList.toString()).build();
    }
}
