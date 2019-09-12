package com.youyuan.zk;

import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * 同步获取节点数据
 * @author zhangyu
 * @date 2018/9/6 15:40
 */
public class GetDataSync implements Watcher {
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) {
        try {
            zooKeeper=new ZooKeeper("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",5000,new GetDataSync());
            Thread.sleep(Integer.MAX_VALUE);
            System.out.println("客户端与服务端连接状态:"+zooKeeper.getState());
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
     * 执行业务方法
     */
    private void executeService() {
        try {
            //第一个参数读取节点路径
            //第二个参数监听类型  因为监听一次失效  true代表继续监听  false放弃监听
            //第三个参数 Stat 包含版本信息，id，创建时间等信息
            byte[] data=zooKeeper.getData("/myconfig/username",true,new Stat());
            System.out.println("同步获取节点数据:"+new String(data));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
