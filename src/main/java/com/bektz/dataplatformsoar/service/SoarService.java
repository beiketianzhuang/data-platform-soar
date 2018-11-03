package com.bektz.dataplatformsoar.service;

import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.SqlVerifyResp;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class SoarService {

    public SqlVerifyResp verify(SqlVerifyReq sqlVerifyReq) {
        String soar = System.getenv("soar");
        String[] cmds = {"/bin/sh","-c","cd /Users/chenlang/work/src/github.com/XiaoMi/soar && echo 'select * from alert' | ./soar"};
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

        processList.remove("");
        return SqlVerifyResp.builder().verifyInfo(processList.toString()).build();
    }
}
