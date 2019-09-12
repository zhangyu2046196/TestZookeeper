package com.youyuan.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * 同步删除节点
 * @author zhangyu
 * @date 2018/9/6 15:02
 */
public class DeleteNodeSync implements Watcher {
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) {
        try {
            zooKeeper=new ZooKeeper("172.18.32.16:12181",5000,new DeleteNodeSync());
            Thread.sleep(Integer.MAX_VALUE);
            System.out.println("客户端与服务端创建连接,连接状态:"+zooKeeper.getState());
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
        System.out.println("接受监听事件......");
        if (watchedEvent.getState()==Event.KeeperState.SyncConnected){//客户端与服务端建立连接
            executeService();
        }
    }

    /**
     * 处理业务逻辑
     */
    private void executeService() {
        Stat stat=new Stat();//存储节点的版本，创建事件，id等信息
        try {
            //第一个参数代表需要删除的节点路径
            //第二个参数代表版本信息 -1代表不校验版本
            zooKeeper.delete("/myconfig/host",stat.getVersion());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
