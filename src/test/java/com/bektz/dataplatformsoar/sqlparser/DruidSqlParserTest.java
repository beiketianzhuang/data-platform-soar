package com.bektz.dataplatformsoar.sqlparser;

import com.bektz.dataplatformsoar.sqlparser.druid.DruidSqlParser;
import org.junit.jupiter.api.Test;

import java.util.Map;

class DruidSqlParserTest {


    String[] sqls = new String[]{
//            "SELECT * ,username FROM MY_TABLE1, MY_TABLE2, (SELECT username FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 \" +\n" +
//                    "                \" WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)",
            "select a.name c from (select alert.name from alert inner join user on alert.id = user.id) a",
            "select a.m cc , a.nnn from (select c.mobile as m ,c.nnn nnn from card c where c.id = 1) a",
            "select u.name,u.no  from (select u.name name ,c.card_no no from user u inner join card c on u.id = c.id) u",
            "select * from (select a,avg(*) b from alert) c",
            "select a.na a  from (select u.name as na , u.age from user u inner join alert al on alert.id = user.id)  a",
            "select a.m as cc from (select c.mobile as m from (select mobile from card) c) a",
            "select * from firekylin.alert",
            "select st.sname,st.sno from student st join (select sc.sno sn,count(sc.cno) cou from sc group by sc.sno) scs on st.sno=scs.sn where scs.cou <(select count(cno) from course);",
            "select distinct st.sno, sname\n" +
                    "  from student st\n" +
                    "  join sc sc on (st.sno = sc.sno)\n" +
                    " where sc.cno in (select cno from sc where sno = 's001') and sc.sno<>'s001';"
    };

    @Test
    void parserRealMetaData() {
        DruidSqlParser druidSqlParser = new DruidSqlParser();
        for (String sql : sqls) {
            Map<String, ColumnItem> columnItems = druidSqlParser.parserSql(sql);
            System.out.println(columnItems);
        }
    }


    @Test
    void parserTest() {
        String sql = "select distinct st.sno, sname\n" +
                "  from student st\n" +
                "  join sc sc on (st.sno = sc.sno)\n" +
                " where sc.cno in (select cno from sc where sno = 's001') and sc.sno<>'s001';";
        DruidSqlParser druidSqlParser = new DruidSqlParser();
        Map<String, ColumnItem> columnItems = druidSqlParser.parserSql(sql);
        System.out.println(columnItems);

    }
}