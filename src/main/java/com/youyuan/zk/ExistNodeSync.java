package com.youyuan.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * 同步判断节点是否存在
 * @author zhangyu
 * @date 2018/9/6 21:27
 */
public class ExistNodeSync implements Watcher {

    private static ZooKeeper zooKeeper=null;

    public static void main(String[] args) {
        try {
            zooKeeper=new ZooKeeper("172.18.32.16,12181,172.18.32.21:12181,172.18.32.25:12181",5000,new ExistNodeSync());
            Thread.sleep(Integer.MAX_VALUE);
            System.out.println("客户端连接服务端状态:"+zooKeeper.getState());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 事件监听数据
     * @param watchedEvent
     */
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState()==Event.KeeperState.SyncConnected){//客户端与服务端建立连接
            executeService();
        }
    }

    /**
     * 处理业务逻辑
     */
    private void executeService() {
        try {
            Stat stat=zooKeeper.exists("/myconfig",true);
            System.out.println("判断节点是否存在:"+stat.toString());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
