package cn.sinobest.ypgj.util;

import cn.sinobest.jzpt.framework.security.SecurityUtil;
import cn.sinobest.jzpt.framework.utils.JdbcService;
import com.google.common.io.Files;

import java.sql.Types;
import java.util.UUID;

/**
 * Created by chenjianhua on 2015/7/22 0022.
 */
public class FtpHelper {
    public static String upload(byte[] datas, String dir, String fileName){
        String sYSTEMID = UUID.randomUUID().toString().replace("-","");
        upload(datas, dir, fileName, sYSTEMID);
        return sYSTEMID;
    }

    public static String upload(byte[] datas, String dir, String fileName, String fileId){
        String username = SecurityUtil.getCurrentUser().getUsername();
        return upload(datas, dir, fileName, fileId, username);
    }
    public static String upload(byte[] datas, String dir, String fileName, String fileId, String username){
        //1.连接ftp服务器
        FtpUtil ftpUtil = FtpUtil.getInstance();
        ftpUtil.connect();

        //2.上传文件
        String suffix = Files.getFileExtension(fileName);
        try {
            //3.把文件相关信息入库
            JdbcService.getInstance().update("INSERT INTO T_FJGL(SYSTEMID,WBID,FJMC,FJLX,CREATOR,CREATEDTIME,SFYBC) VALUES (?,?,?,?,?,sysdate,'0')"
                    , new Object[]{fileId, fileId, fileName, suffix, username}
                    , new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
            //4.上传文件到ftp
            ftpUtil.upload(datas,dir.toString(),new StringBuilder(fileId).append(".").append(suffix).toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        //5.断开与ftp的连接
        ftpUtil.disconnect();
        return fileId;
    }
}
