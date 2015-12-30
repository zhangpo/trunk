package cn.com.choicesoft.util.wait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * M¡£c
 * 2015-04-24
 * Jnwsczh@163.com
 */
public class SocketClient {
    static Socket client;

    public SocketClient(String site, int port){
        try{
            client = new Socket(site,port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String sendMsg(String msg){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream());
            out.println(msg);
            out.flush();
            return in.readLine();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public void closeSocket(){
        try{
            if(client!=null) {
                client.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}