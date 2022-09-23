package storedprocedureTesting;


import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;

public class SPTesing {

    Connection con=null;
    Statement stmt=null;
    Statement stmt2=null;
    ResultSet rs;
    CallableStatement cStmt;
    ResultSet rs1;
    ResultSet rs2;

    @BeforeClass
    void setup() throws SQLException {
        con = DriverManager.getConnection("jdbc:postgresql://10.230.47.4:5432/azdev02db", "jpsazdev02@azdev-db01", "jpsazdev02");
    }

    @AfterClass
    void tearDown() throws SQLException {
        con.close();
    }

    @Test
    void test_storedProceduresExist() throws SQLException {
        stmt2=con.createStatement();
//        stmt2.executeUpdate("INSERT INTO `anatoliy`.`wp_provausers` (`ID`, `user_login`, `user_pass`, `user_nicename`, `user_email`, `user_url`, `user_registered`, `user_activation_key`, `user_status`, `display_name`) VALUES ('6', 'trypass', MD5('demo'), 'Anatoliy', 'anatoliy@siinfo.eu', 'http://www.example.com/', '2022-09-01 00:00:00', '', '0', 'DispANatoliy'); INSERT INTO `anatoliy`.`wp_provausers` (`ID`, `user_login`, `user_pass`, `user_nicename`, `user_email`, `user_url`, `user_registered`, `user_activation_key`, `user_status`, `display_name`) VALUES ('5', 'myaccount', MD5('demo'), 'Anatoliy', 'anatoliy@siinfo.eu', 'http://www.example.com/', '2022-09-01 00:00:00', '', '0', 'DispANatoliy')");
//        stmt2.executeUpdate("DELETE FROM `wp_provausers` WHERE ID=4");
//        stmt2.executeQuery("SELECT * FROM `azdev02db.nss.`");
//        stmt=con.createStatement();
        rs=stmt2.executeQuery("select * from jpsazdev02.tbjps_group_instance_family where jackpot_group_family_code = 'M01' and grpinst_status_code IN ('ACTV')");
        while(rs.next()) {
            System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
        }

        //CRUD SQL QUERRY
        //DELETE FROM `wp_provausers` WHERE ID=4
        //SELECT * FROM `wp_provausers`
        //INSERT INTO `anatoliy`.`wp_provausers` (`ID`, `user_login`, `user_pass`, `user_nicename`, `user_email`, `user_url`, `user_registered`, `user_activation_key`, `user_status`, `display_name`) VALUES ('4', 'demo', MD5('demo'), 'Anatoliy', 'anatoliy@siinfo.eu', 'http://www.example.com/', '2022-09-01 00:00:00', '', '0', 'DispANatoliy')

//        Assert.assertEquals(rs.getString("user_login"), "anatoliy");
    }

    @Test
    void test_RGSEINST_3057() throws SQLException {
        con = DriverManager.getConnection("jdbc:postgresql://10.230.47.4:5432/azdev02db", "jpsazdev02@azdev-db01", "jpsazdev02");
    }


    @Test(enabled = false)
    void test_AllSites() throws SQLException {
        /*cStmt = con.prepareCall("SELECT domain FROM `wps_blogs`");//Here should be Stored Procedure otherwise its NOT WORKING
        rs1=cStmt.executeQuery();*/

//        stmt=con.createStatement();
//        rs2=stmt.executeQuery("SELECT * FROM `wps_blogs`");

//        Assert.assertEquals(compareResultSet(rs1,rs2), true);
    }

    public boolean compareResultSet(ResultSet ResultSet1, ResultSet ResultSet2) throws SQLException {
        while (ResultSet1.next()){
            ResultSet2.next();
            int count = ResultSet1.getMetaData().getColumnCount();
            for (int i = 1; 1 <= count; i++){
                if (!StringUtils.equals(ResultSet1.getString(i),ResultSet2.getString(i))){
                    return false;
                }
            }
        }
        return true;
    }
}
