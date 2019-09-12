package com.youyuan.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * 异步获取节点数据
 * @author zhangyu
 * @date 2018/9/6 15:50
 */
public class GetDataASync implements Watcher {
    private static ZooKeeper zooKeeper=null;

    public static void main(String[] args) {
        try {
            zooKeeper=new ZooKeeper("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",5000,new GetDataASync());
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
     * 执行业务逻辑
     */
    private void executeService() {
        zooKeeper.getData("/myconfig/username",true,new IDataCallBack(),"异步获取节点数据");
    }

    /**
     * 回调
     */
    class IDataCallBack implements AsyncCallback.DataCallback{
        // rc是result code 服务端响应结果码。客户端可以从这个结果码中识别出API的调用结果，常见的结果码有：
        //      0（OK）,接口调用成功
        //      -4（ConnectionLoss），客户端和服务器连接断开
        //      -110（NodeExists） 节点已存在
        //      -112（SessionExpired）会话已过期
        //   path: 接口调用传入的数据节点的节点路径
        //   ctx: 异步调用的上下文
        //   bytes: 查询出的节点内容
        //   stat: 节点信息 包含版本，id，创建事件等
        public void processResult(int rc, String path, Object ctx, byte[] bytes, Stat stat) {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("rc=").append(rc).append("|");
            stringBuilder.append("path=").append(path).append("|");
            stringBuilder.append("ctx=").append(ctx).append("|");
            stringBuilder.append("节点内容=").append(new String(bytes)).append("|");
            stringBuilder.append("stat=").append(stat);
            System.out.println("异步获取节点数据回调内容:"+stringBuilder.toString());
        }
    }
}
