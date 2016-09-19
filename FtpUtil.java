package cn.sinobest.ypgj.util;

import cn.sinobest.jzpt.framework.utils.JdbcService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  chenjianhua
 * @date    2014/03/27
 * ftp工具类，依赖org.apache.commons.net.ftp.FTPClient实现
 */
public class FtpUtil {
    private FTPClient ftpClient = new FTPClient();
	private static Map<String,String> para = new HashMap<String, String>();
	private String rootPath;
    private static FtpUtil ftpUtil = null;
    static{

    }

    public static FtpUtil getInstance() {
        if (ftpUtil == null) {
            synchronized (FtpUtil.class) {

                ftpUtil = new FtpUtil();
            }
        }
        return ftpUtil;
    }

	private FtpUtil() {
        /*   初始化ftp配置信息   */
        List<Map<String,String>> list = JdbcService.getInstance().queryForList("SELECT code,value FROM s_parameter WHERE CATEGORY=?", new Object[]{"FTP"}, new int[]{Types.VARCHAR});
        for(Map<String,String> entry : list){
            para.put(entry.get("CODE"),entry.get("VALUE"));
        }
        rootPath = para.get("ftpPath");
	}

    /**
     * 连接ftp服务器
     */
	public boolean connect() {
		String ipAddr = para.get("ftpIpAddr");
		String username = para.get("ftpUser");
		String password = para.get("ftpPassword");
        int port = 0;
        if(para.get("ftpPort")!=null && !StringUtils.isEmpty(para.get("ftpPort"))){
            port = Integer.parseInt(para.get("ftpPort"));
        }
        try {
            if(port > 0){
                ftpClient.connect(ipAddr,port);
            }else{
                ftpClient.connect(ipAddr);
            }
            if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
                if(ftpClient.login(username, password)){
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient.makeDirectory(this.rootPath);
                    ftpClient.changeWorkingDirectory(this.rootPath);
                    System.out.println("连接ftp服务器成功...");
                    return true;
                }else{
                    throw new RuntimeException("登录ftp文件服务器失败！请检查配置！");
                }
            }else{
                throw new RuntimeException("连接ftp文件服务器失败！请检查配置！");
            }

		} catch (IOException e) {
			e.printStackTrace();
		}
        return false;
	}

    /**
     * 与ftp服务器断开
     */
    public void disconnect(){
        if(ftpClient != null && ftpClient.isConnected()){
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 递归创建远程服务器目录
     * @param dirPath 远程服务器文件绝对路径
     * @return 目录创建是否成功
     * @throws IOException
     */
    public boolean CreateDirecroty(String dirPath) throws IOException{
        boolean status = true;
        String directory = dirPath.substring(0, dirPath.lastIndexOf("/")+1);
        if(!directory.equalsIgnoreCase("/")&&!ftpClient.changeWorkingDirectory(new String(directory.getBytes("GBK"),"iso-8859-1"))){
            //如果远程目录不存在，则递归创建远程服务器目录
            int start=0;
            int end = 0;
            if(directory.startsWith("/")){
                start = 1;
            }else{
                start = 0;
            }
            end = directory.indexOf("/",start);
            while(true){
                String subDirectory = new String(dirPath.substring(start,end).getBytes("GBK"),"iso-8859-1");
                if(!ftpClient.changeWorkingDirectory(subDirectory)){
                    if(ftpClient.makeDirectory(subDirectory)){
                        ftpClient.changeWorkingDirectory(subDirectory);
                    }else {
                        System.out.println("创建目录失败");
                        return false;
                    }
                }

                start = end + 1;
                end = directory.indexOf("/",start);

                //检查所有目录是否创建完毕
                if(end <= start){
                    break;
                }
            }
        }
        return status;
    }

    /**
     * 上传文件到ftp指定目录
     * @param content
     * @param dirPath
     * @param fileName
     * @return
     */
    public boolean upload(byte[] content,String dirPath,String fileName){

        if(content!=null && content.length > 0 && !StringUtils.isEmpty(fileName)){
            ByteArrayInputStream bais = null;
            try {
                ftpClient.changeWorkingDirectory("/");
                if(!StringUtils.isEmpty(dirPath)){
                    dirPath = this.rootPath + dirPath;
                }else{
                    dirPath = this.rootPath;
                }
                if(!ftpClient.changeWorkingDirectory(dirPath)){
                    if(CreateDirecroty(dirPath)){
                        System.out.println("ftp创建目录成功");
                        ftpClient.changeWorkingDirectory(dirPath);
                    }else {
                        System.out.println("ftp创建目录失败");
                    }
                }
                ftpClient.setBufferSize(1024*4);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
                bais = new ByteArrayInputStream(content);
                ftpClient.storeFile(fileName, bais);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else{
            return false;
        }


    }

    /**
     * 从ftp服务器下载文件
     * @param dirPath
     * @param fileName
     * @return
     * @throws IOException
     */
	public byte[] download(String dirPath,String fileName) throws IOException {

        //校验参数
        if(StringUtils.isEmpty(fileName)){
            return null;
        }
        InputStream inputStream = null;
        String ipAddr = para.get("ftpIpAddr");
        String username = para.get("ftpUser");
        String password = para.get("ftpPassword");
        int port = 0;
        if(para.get("ftpPort")!=null && !StringUtils.isEmpty(para.get("ftpPort"))){
            port = Integer.parseInt(para.get("ftpPort"));
        }
        //1.连接登录ftp文件服务器
        FTPClient ftpClient1 = new FTPClient();
        try {
            if(port > 0){
                ftpClient1.connect(ipAddr,port);
            }else{
                ftpClient1.connect(ipAddr);
            }
            if(FTPReply.isPositiveCompletion(ftpClient1.getReplyCode())){
                if(ftpClient1.login(username, password)){
                    ftpClient1.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient1.makeDirectory(this.rootPath);
                    ftpClient1.changeWorkingDirectory(this.rootPath);
                }else{
                    throw new RuntimeException("登录ftp文件服务器失败！请检查配置！");
                }
            }else{
                throw new RuntimeException("连接ftp文件服务器失败！请检查配置！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ftpClient1.changeWorkingDirectory("/");
        if(!StringUtils.isEmpty(dirPath)){
            dirPath = this.rootPath + dirPath;
        }else{
            dirPath = this.rootPath;
        }
        ftpClient1.changeWorkingDirectory(dirPath);
        ftpClient1.setBufferSize(1024 * 4);
        //设置以二进制方式传输
        ftpClient1.setFileType(ftpClient1.BINARY_FILE_TYPE);
        //2.获取文件流
        inputStream = ftpClient1.retrieveFileStream(fileName);
        if(inputStream == null){
            System.err.println("ftp服务器没有该文件！");
            return null;
        }

        //3.把InputStream里面的数据转储到byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] b = new byte[1024];
        while ((len = inputStream.read(b, 0, b.length)) != -1) {
            baos.write(b, 0, len);
        }
        byte[] buffer =  baos.toByteArray();

        //4.关闭
        inputStream.close();
        if(ftpClient1 != null && ftpClient1.isConnected()){
            try {
                ftpClient1.logout();
                ftpClient1.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;

	}

    /**
     * 下载根目录下的文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public byte[] download(String fileName) throws IOException {
        return download(null, fileName);
    }
}