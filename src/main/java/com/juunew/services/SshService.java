package com.juunew.services;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.juunew.model.ConnectInfo;
import com.juunew.model.WebscoketObj;
import com.juunew.utils.Base64Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SshService {
    private static List<WebscoketObj> sessionQueue = new CopyOnWriteArrayList<>();
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Value("${trustiePodUrl}")
    String trustiePodUrl;


    private ConnectInfo connectInfo;

    /**
     * 在webssh连接时，初始化一个ssh连接
     *
     * @param session
     */
    public void add(WebSocketSession session) {
        JSch jsch = new JSch();

        WebscoketObj webscoketObj = new WebscoketObj();
        webscoketObj.setChannel(null);
        webscoketObj.setjSch(jsch);
        webscoketObj.setSession(session);

        sessionQueue.add(webscoketObj);
    }

    /**
     * 处理客户端发过来的数据
     *
     * @param buffer
     */
    public void recv(String buffer, WebSocketSession session) {
        try {
            JSONObject object = JSONObject.parseObject(buffer);
            String tp = object.getString("tp");
            if ("init".equals(tp)) {
                //初始化连接
                this.connectInfo = object.getObject("data", ConnectInfo.class);
                WebscoketObj webscoketObj = findBySession(session);
                if (webscoketObj!=null) {

                    //TODO 还可以优化下
                    executorService.execute(() -> {
                        try {
                            connectTossh(webscoketObj, connectInfo,session);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("连接ssh出错: ", e);
                        }
                    });

                }
            } else if("client".equals(tp)){
                String data = object.getString("data");
                WebscoketObj webscoketObj = findBySession(session);
                if (webscoketObj!=null){
                    transTossh(webscoketObj.getChannel(), data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            close(session);
        }

    }

    private void transTossh(Channel channel, String data) throws IOException {
        if (channel!=null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
        }
    }

    private void connectTossh(WebscoketObj webscoketObj, ConnectInfo connectInfo, WebSocketSession webSocketSession) throws Exception {
        //启动线程
        Session session = null;
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session = webscoketObj.getjSch().getSession(connectInfo.getUsername(), connectInfo.getHost(), connectInfo.getPort());
            session.setConfig(config);
            session.setPassword(connectInfo.getSecret());
            session.connect(30000);
            Channel channel = session.openChannel("shell");

            channel.connect(3 * 1000);

            webscoketObj.setChannel(channel);

            transTossh(channel, "\r");


            InputStream inputStream = channel.getInputStream();
            //循环读取
            byte[] buf = new byte[1024];//一次读1024个byte
            StringBuffer sb = new StringBuffer();
            while (true) {
                int length = inputStream.read(buf);
                if (length < 0) throw new Exception("读取出错");
                sendMsg(webSocketSession, Arrays.copyOfRange(buf, 0, length));
            }
        }finally {
            if (session!=null){
                session.disconnect();
            }
        }
    }


    /**
     * 发送数据回websocket
     * @param session
     * @param buffer
     * @throws IOException
     */
    public void sendMsg(WebSocketSession session, byte[] buffer) throws IOException {
        session.sendMessage(new TextMessage(Base64Helper.encodeBytes(buffer)));
    }

    private void _close(WebSocketSession session){
        WebscoketObj webscoketObj = findBySession(session);
        if (webscoketObj!=null){
            if(webscoketObj.getChannel()!=null)webscoketObj.getChannel().disconnect();
            sessionQueue.remove(webscoketObj);
        }
    }

    /**
     * 清除
     *
     * @param session
     */
    public void close(WebSocketSession session) {
        this._close(session);
    }


    public WebscoketObj findBySession(WebSocketSession session) {
        Optional<WebscoketObj> optional = sessionQueue.stream()
                .filter(webscoketObj -> webscoketObj.getSession() == session).findFirst();
        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }
}
