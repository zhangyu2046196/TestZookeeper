package com.youyuan.zk;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * 异步创建数据节点
 * @author zhangyu
 * @date 2018/9/6 13:27
 */
public class CreateNodeASync implements Watcher {
    private static ZooKeeper zooKeeper=null;

    public static void main(String[] args) {
        try {
            zooKeeper=new ZooKeeper("172.18.32.21:12181",5000,new CreateNodeASync());
            System.out.println("zk客户端与服务端建立连接状态:"+zooKeeper.getState());
            Thread.sleep(Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听事件数据
     * @param watchedEvent
     */
    public void process(WatchedEvent watchedEvent) {
        System.out.println("接收监听数据......");
        if (watchedEvent.getState()==Event.KeeperState.SyncConnected){//连接成功
            executeService(zooKeeper);
        }
    }

    /**
     * 执行业务处理逻辑  异步创建数据节点
     * @param zooKeeper
     */
    private void executeService(ZooKeeper zooKeeper) {
        if (zooKeeper!=null){
            zooKeeper.create("/myconfig/host","192.168.21.26".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT,new IStringCallBck(),"创建");
        }
    }

    /**
     * 异步回调类
     */
    class IStringCallBck implements AsyncCallback.StringCallback{
        // rc是result code 服务端响应结果码。客户端可以从这个结果码中识别出API的调用结果，常见的结果码有：
        //      0（OK）,接口调用成功
        //      -4（ConnectionLoss），客户端和服务器连接断开
        //      -110（NodeExists） 节点已存在
        //      -112（SessionExpired）会话已过期
        //   path: 接口调用传入的数据节点的节点路径
        //   ctx: 异步调用的上下文
        //   name: 服务端返回的已经创建的节点的真实路径
        public void processResult(int rc,String path,Object ctx,String name) {
            StringBuilder sb=new StringBuilder();
            sb.append(rc).append("\n");
            sb.append(path).append("\n");
            sb.append(ctx).append("\n");
            sb.append(name).append("\n");
            System.out.println("回调内容:"+sb.toString());
        }
    }
}
