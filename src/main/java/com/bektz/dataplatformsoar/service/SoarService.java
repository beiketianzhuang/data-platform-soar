package com.bektz.dataplatformsoar.service;

import com.bektz.dataplatformsoar.configs.SoarConfiguration;
import com.bektz.dataplatformsoar.req.SqlVerifyReq;
import com.bektz.dataplatformsoar.resp.SqlVerifyResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SoarService {

    private static final String CMD = "%s/soar '-test-dsn=devopsa:1qaz@WSX@172.21.7.14:3306/devops' '-query=%s' '-report-type=text' '-allow-online-as-test=false' -log-output=/Users/chenlang/work/src/github.com/XiaoMi/";

    @Value("${soar-configs.execuable_path}")
    private String soarPath;

    @Autowired
    private SoarConfiguration soarConfiguration;

    public SqlVerifyResp verify(SqlVerifyReq sqlVerifyReq) {
        String[] cmds = {"/bin/sh", "-c", String.format(CMD, soarPath, sqlVerifyReq.getSql())};
        Map<String, SoarConfiguration.Skill> skillsMap = soarConfiguration.getSkillsMap();
        List<String> verifyInfos = new ArrayList<>();
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmds);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                verifyInfos.add(line);
            }
            input.close();
        } catch (IOException e) {
        }
//        List<String> infos = verifyInfos.parallelStream().filter(verifyInfo -> !StringUtils.isEmpty(verifyInfo)).collect(Collectors.toList());
        String sqlInfo = String.join("<br>", verifyInfos);
        return SqlVerifyResp.builder().verifyInfo(sqlInfo).build();
    }
}
