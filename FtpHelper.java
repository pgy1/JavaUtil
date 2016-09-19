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
        //1.����ftp������
        FtpUtil ftpUtil = FtpUtil.getInstance();
        ftpUtil.connect();

        //2.�ϴ��ļ�
        String suffix = Files.getFileExtension(fileName);
        try {
            //3.���ļ������Ϣ���
            JdbcService.getInstance().update("INSERT INTO T_FJGL(SYSTEMID,WBID,FJMC,FJLX,CREATOR,CREATEDTIME,SFYBC) VALUES (?,?,?,?,?,sysdate,'0')"
                    , new Object[]{fileId, fileId, fileName, suffix, username}
                    , new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
            //4.�ϴ��ļ���ftp
            ftpUtil.upload(datas,dir.toString(),new StringBuilder(fileId).append(".").append(suffix).toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        //5.�Ͽ���ftp������
        ftpUtil.disconnect();
        return fileId;
    }
}
