package com.youyuan.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * 同步更新节点
 * @author zhangyu
 * @date 2018/9/6 17:34
 */
public class UpdaeNodeSync implements Watcher {

    private static ZooKeeper zooKeeper=null;

    public static void main(String[] args) {
        try {
            zooKeeper=new ZooKeeper("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",5000,new UpdaeNodeSync());
            Thread.sleep(Integer.MAX_VALUE);
            System.out.println("客户端与服务端建立连接状态:"+zooKeeper.getState());
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
            Stat stat=zooKeeper.setData("/myconfig/username","root123".getBytes(),-1);
            System.out.println("同步修改节点信息:"+stat.toString());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
