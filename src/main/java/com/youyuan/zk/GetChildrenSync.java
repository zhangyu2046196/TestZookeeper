package com.youyuan.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

/**
 * 同步获取子节点
 * @author zhangyu
 * @date 2018/9/6 16:31
 */
public class GetChildrenSync implements Watcher {

    private static ZooKeeper zooKeeper=null;

    public static void main(String[] args) {
        try {
            zooKeeper=new ZooKeeper("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",5000,new GetChildrenSync());
            Thread.sleep(Integer.MAX_VALUE);
            System.out.println("客户端与服务端建立连接状态:"+zooKeeper.getState());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听数据信息
     * @param watchedEvent
     */
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState()==Event.KeeperState.SyncConnected){//客户端与服务端建立连接
            executeService();
        }
    }

    /**
     * 执行业务逻辑
     */
    private void executeService() {
        try {
            //第一个参数获取子节点路径
            //第二个参数是否循环监听  true 是  flase 否  因为wathcer监听一次就失效
            List<String> childrens = zooKeeper.getChildren("/myconfig", true);
            for (String children:childrens){
                System.out.println("子节点信息:"+children);
            }

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
