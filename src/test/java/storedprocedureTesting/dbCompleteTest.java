package storedprocedureTesting;

import com.BaseClass;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;

public class dbCompleteTest {
    public WebDriver driver;
    Connection con=null;
    Statement stmt2=null;
    ResultSet rs;

    @BeforeClass(description = "Go to Swagger")
    public void goToDiggiGames() throws InterruptedException {
        driver = new BaseClass().inizialize_driver();
        driver.get("http://10.230.47.15:11880/jackpot/swagger-ui.html");
        Thread.sleep(5000);
        driver.manage().window().maximize();
    }

    @AfterClass(description = "Close browser")
    public void closeBrowser() throws SQLException {
        con.close();
        // 13 | close |
        driver.quit();
    }

    @Test(description = "jackpot-group-details-service")
    public void jackpot_group_details_service() throws InterruptedException, SQLException {
        driver.findElement(By.xpath("//*[@id='operations-tag-jackpot-group-details-service']/button")).click();
        Thread.sleep(1500);
        driver.findElement(By.xpath("//div[@id='operations-jackpot-group-details-service-getJackpotGroupInstDetailsByFmlyInstUsingGET']")).click();
        Thread.sleep(1500);
        driver.findElement(By.xpath("//button[@class='btn try-out__btn']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[@id='operations-jackpot-group-details-service-getJackpotGroupInstDetailsByFmlyInstUsingGET']//input")).sendKeys("TMJP");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[@id='operations-jackpot-group-details-service-getJackpotGroupInstDetailsByFmlyInstUsingGET']//button[@class='btn execute opblock-control__btn']")).click();
        Thread.sleep(6000);
        String resultAPI = driver.findElement(By.xpath("//*[@id='operations-jackpot-group-details-service-getJackpotGroupInstDetailsByFmlyInstUsingGET']/div[2]/div/div[3]/div[2]/div/div/table/tbody/tr/td[2]/div[1]/div/pre")).getText();
        JSONObject responseAPI = new JSONObject(resultAPI);
        JSONArray jsonarray = new JSONArray(responseAPI.getJSONArray("jackpotCurrency"));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String name = jsonobject.getString("currencyCode");
            System.out.println(name);
        }

        con = DriverManager.getConnection("jdbc:postgresql://10.230.47.4:5432/azdev02db", "jpsazdev02@azdev-db01", "jpsazdev02");
        stmt2=con.createStatement();
        String a = "SELECT jgi.grpinstanceid_str grpinstanceid_str,\n" +
                "bi.bandinstanceid_str bandinstanceid_str,\n" +
                "b.band_id_str band_id_str,\n" +
                "jgi.grpinst_status_code grpinst_status_code,\n" +
                "jg.Currency_Code Base_Currency_Code,\n" +
                "fnjps_get_groupCurrencyList(jgi.grpinstanceid_str) as groupCurrencyList,\n" +
                "b.band_tmplt_id_str band_tmplt_id_str\n" +
                "FROM tbjps_jackpot_group_instance jgi,\n" +
                "tbjps_band_instance bi,\n" +
                "tbjps_band b,\n" +
                "tbjps_jackpot_group jg\n" +
                "WHERE jgi.group_instance_family_code = 'TMJP'\n" +
                "AND jgi.jackpot_group_id = jg.jackpot_group_id\n" +
                "AND jgi.grp_instance_id = bi.grp_instance_id\n" +
                "AND bi.band_id = b.band_id\n" +
                "And jgi.enabled_ind = 'Y'\n" +
                "AND jgi.grpinst_status_code = 'ACTV'\n" +
                "AND (EXISTS (SELECT 1\n" +
                "FROM tbjps_prog_jkptinstcfg_hist pch,\n" +
                "tbjps_jkptband_instance jbi,\n" +
                "tbjps_jackpot_instance ji\n" +
                "WHERE pch.jkpt_instance_id=ji.jkpt_instance_id\n" +
                "AND ji.jkpt_instance_id=jbi.jkpt_instance_id\n" +
                "AND Jbi.Band_Instance_Id=Bi.Band_Instance_Id\n" +
                "AND pch.current_row_ind='Y')\n" +
                "OR EXISTS (SELECT 1\n" +
                "FROM tbjps_jkptinstance_config_hist jch,\n" +
                "tbjps_jkptband_instance jbi,\n" +
                "tbjps_jackpot_instance ji\n" +
                "WHERE jch.jkpt_instance_id=ji.jkpt_instance_id\n" +
                "AND ji.jkpt_instance_id=jbi.jkpt_instance_id\n" +
                "AND Jbi.Band_Instance_Id=Bi.Band_Instance_Id\n" +
                "AND Jch.Current_Row_Ind='Y' Or Jch.Current_Jkpt_Cycle_Row_Ind='Y'));";
        rs=stmt2.executeQuery(a);
        while(rs.next()) {
            System.out.println(rs.getString(6));
        }
    }
}
