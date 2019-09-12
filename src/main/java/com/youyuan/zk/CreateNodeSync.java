package com.youyuan.zk;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * 同步创建数据节点
 * @author zhangyu
 * @date 2018/9/6 10:42
 */
public class CreateNodeSync implements Watcher {

    private static ZooKeeper zooKeeper=null;

    public static void main(String[] args) throws IOException, InterruptedException {
        //创建zookeeper连接
        zooKeeper=new ZooKeeper("172.18.32.21:12181",5000,new CreateNodeSync());
        System.out.println(zooKeeper.getState());
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * watcher监听方法
     * @param watchedEvent
     */
    public void process(WatchedEvent watchedEvent) {
        System.out.println("收到监听信息......");
        try {
            if (watchedEvent.getState()==Event.KeeperState.SyncConnected){//客户端与服务器连接成功
                executeService(zooKeeper);//处理业务逻辑
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理业务逻辑  同步创建数据节点
     * 节点创建类型(CreateMode)
     *      1、PERSISTENT:持久化节点
     *      2、PERSISTENT_SEQUENTIAL:顺序自动编号持久化节点，这种节点会根据当前已存在的节点数自动加 1
     *      3、EPHEMERAL:临时节点客户端,session超时这类节点就会被自动删除
     *      4、EPHEMERAL_SEQUENTIAL:临时自动编号节点
     * @param zooKeeper
     */
    private void executeService(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        //创建节点 /myconfig/url   内容为http://youyuan.com  CreateMode.PERSISTENT代表持久化节点
        String createResult= zooKeeper.create("/myconfig/url","http://youyuan.com".getBytes(),ZooDefs.Ids.READ_ACL_UNSAFE,CreateMode.PERSISTENT);
        System.out.println("创建节点结果:"+createResult);
    }
}
