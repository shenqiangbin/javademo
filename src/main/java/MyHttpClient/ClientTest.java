package MyHttpClient;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import common.P;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import static org.apache.xmlbeans.impl.store.Public2.test;

public class ClientTest {
    public static void main(String[] args) throws Exception {

        testFileExist();
        test();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sql", "insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1700','','    (年末数)人','总计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','B9','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1485','','    (年末数)人','卫生技术人员合计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','C9','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1140','','    (年末数)人','卫生技术人员医生','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','D9','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('954','','    (年末数)人','卫生技术人员#中医','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','E9','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('103','','    (年末数)人','卫生技术人员#西医师','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','F9','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('83','','    (年末数)人','卫生技术人员#西医士','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','G9','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('120','','    (年末数)人','卫生技术人员-护师护士','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','H9','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('225','','    (年末数)人','卫生技术人员-其他卫生技术人员','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','I9','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('0.46','','    (年末数)人','每千人拥有医生数','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','J9','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('2143','','    (年末数)人','总计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','B10','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1863','','    (年末数)人','卫生技术人员合计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','C10','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1261','','    (年末数)人','卫生技术人员医生','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','D10','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('953','','    (年末数)人','卫生技术人员#中医','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','E10','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('134','','    (年末数)人','卫生技术人员#西医师','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','F10','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('174','','    (年末数)人','卫生技术人员#西医士','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','G10','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('151','','    (年末数)人','卫生技术人员-护师护士','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','H10','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('451','','    (年末数)人','卫生技术人员-其他卫生技术人员','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','I10','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('0.49','','    (年末数)人','每千人拥有医生数','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','J10','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('2344','','    (年末数)人','总计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','B11','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1999','','    (年末数)人','卫生技术人员合计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','C11','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1268','','    (年末数)人','卫生技术人员医生','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','D11','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('950','','    (年末数)人','卫生技术人员#中医','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','E11','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('131','','    (年末数)人','卫生技术人员#西医师','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','F11','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('187','','    (年末数)人','卫生技术人员#西医士','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','G11','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('162','','    (年末数)人','卫生技术人员-护师护士','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','H11','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('569','','    (年末数)人','卫生技术人员-其他卫生技术人员','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','I11','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('0.48','','    (年末数)人','每千人拥有医生数','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','J11','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('2532','','    (年末数)人','总计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','B12','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('2157','','    (年末数)人','卫生技术人员合计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','C12','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1377','','    (年末数)人','卫生技术人员医生','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','D12','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1021','','    (年末数)人','卫生技术人员#中医','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','E12','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('148','','    (年末数)人','卫生技术人员#西医师','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','F12','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('208','','    (年末数)人','卫生技术人员#西医士','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','G12','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('174','','    (年末数)人','卫生技术人员-护师护士','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','H12','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('606','','    (年末数)人','卫生技术人员-其他卫生技术人员','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','I12','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('0.51','','    (年末数)人','每千人拥有医生数','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','J12','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('2780','','    (年末数)人','总计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','B13','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('2376','','    (年末数)人','卫生技术人员合计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','C13','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1507','','    (年末数)人','卫生技术人员医生','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','D13','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1012','','    (年末数)人','卫生技术人员#中医','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','E13','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('171','','    (年末数)人','卫生技术人员#西医师','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','F13','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('324','','    (年末数)人','卫生技术人员#西医士','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','G13','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('207','','    (年末数)人','卫生技术人员-护师护士','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','H13','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('662','','    (年末数)人','卫生技术人员-其他卫生技术人员','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','I13','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('0.55','','    (年末数)人','每千人拥有医生数','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','J13','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('2921','','    (年末数)人','总计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','B14','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('2541','','    (年末数)人','卫生技术人员合计','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','C14','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('1518','','    (年末数)人','卫生技术人员医生','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','D14','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('982','','    (年末数)人','卫生技术人员#中医','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','E14','148387','T_33_历年卫生机构人员数') |;|insert into nv_excel_pickup_origi(Value,ValueType,UnitName,IndicatorName,ValueDate,ProductName,CountryName,Title,CreateUser,CreateTime,UpdateUser,UpdateTime,TableSheetName,ValueAddress,MetadataSysID,MetadataID) values ('189','','    (年末数)人','卫生技术人员#西医师','','','','T-33历年卫生机构人员数','sa',SYSDATE(),'sa',SYSDATE(),'1','F14','148387','T_33_历年卫生机构人员数') ;");
        Map<String, String> headMap = new HashMap<>();
        headMap.put("cookie", "");
        HttpHelper.httpPost("http://192.168.105.89:8006/api/executeBatch", headMap, paramsMap);

//        Map<String, String> paramsMap2 = new HashMap<>();
//        paramsMap2.put("sql","update user set status = 0 where userid = -1");
//        httpHelper.httpPost("http://192.168.105.89:8006/api/execute",null,paramsMap2);

        //saveFileToLocal();

    }

    public static void testUpload() throws Exception {
//        getImgUrl: http://192.168.103.90:8088/getfile/index?filename=
//        uploadImgUrl: http://192.168.103.90:8088/api/Resource/UploadFile
//        fileExistUrl: http://192.168.103.90:8088/api/Resource/FileExist?filename=

        String serverUrl = "http://192.168.103.90:8088/api/Resource/UploadFile";
        String fileName = "F:/girl.jpg";
        Map<String, String> map = new HashMap<>();
        map.put("filename", "girl.jpg");
        String result = HttpHelper.uploadFile(serverUrl, fileName, "girl.jpg", map);
        System.out.println(result);

        Reulst theResult = JSON.parseObject(result, Reulst.class);
        if (theResult.getStatus() != 0) {
            throw new Exception("上传失败了");
        }
    }

    public static void testFileExist() throws Exception {
//        getImgUrl: http://192.168.103.90:8088/getfile/index?filename=
//        uploadImgUrl: http://192.168.103.90:8088/api/Resource/UploadFile
//        fileExistUrl: http://192.168.103.90:8088/api/Resource/FileExist?filename=

        String fileName = "girl.jpg";
        String serverUrl = "http://192.168.103.90:8088/api/Resource/FileExist?filename=" + fileName;
        String result = HttpHelper.httpGet(serverUrl, null);
        System.out.println(result);

        Reulst theResult = JSON.parseObject(result, Reulst.class);
        if (theResult.getStatus() != 0) {
            throw new Exception("上传失败了");
        } else {
            if ((Boolean) theResult.getData()) {
                System.out.println("此图片已存在");
            } else {
                System.out.println("此图片不存在");
            }
        }
    }

    //输出文件到浏览器

    //保存文件到本地
    public static void saveFileToLocal() {
        File newFile = new File("d:/abc.png");
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream stream = null;
        try {

            stream = new FileOutputStream(newFile);
            String url = "http://oimagec6.ydstatic.com/image?id=7347104849285270631&product=dict-homepage&w=&h=&fill=0&cw=&ch=&sbc=0&cgra=CENTER&of=jpeg";
            httpDownloadFile(url, stream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void httpDownloadFile(String url, OutputStream stream) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);

            P.print("status:" + response.getStatusLine());
            HttpEntity httpEntity = response.getEntity();
            //long contentLength = httpEntity.getContentLength();
            InputStream is = httpEntity.getContent();

            byte[] buffer = new byte[4096];
            int r;
            while ((r = is.read(buffer)) > 0) {
                stream.write(buffer, 0, r);
            }

            stream.flush();

            httpclient.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void httpDownloadFile(String url, HttpServletResponse response) {
//
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        try {
//            HttpGet httpGet = new HttpGet(url);
//            CloseableHttpResponse response1 = httpclient.execute(httpGet);
//            try {
//                System.out.println(response1.getStatusLine());
//                HttpEntity httpEntity = response1.getEntity();
//                long contentLength = httpEntity.getContentLength();
//                InputStream is = httpEntity.getContent();
//
//                response.setContentLengthLong(contentLength);
//                //String fileName = image.getName();
//                //String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
//                response.setHeader("Content-Type","image/jpeg");
//                OutputStream toClient = response.getOutputStream();
//
//                //将请求返回的页面的流先放到buffer中，放一个buffer，输出response就写入一个buffer。
//                byte[] buffer = new byte[4096];
//                int r = 0;
//                while((r = is.read(buffer)) > 0) {
//                    toClient.write(buffer, 0, r);
//                }
//
//                toClient.flush();
//
//            } finally {
//                response1.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}

class Reulst {
    private Integer status;
    private String message;
    private Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
