package com.youyuan.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * 异步删除节点
 * @author zhangyu
 * @date 2018/9/6 15:16
 */
public class DeleteNodeASync implements Watcher {
    private static ZooKeeper zooKeeper=null;

    public static void main(String[] args) {
        try {
            zooKeeper=new ZooKeeper("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",5000,new DeleteNodeASync());
            Thread.sleep(Integer.MAX_VALUE);
            System.out.println("客户端与服务端连接状态:"+zooKeeper.getState());
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
        if(watchedEvent.getState()==Event.KeeperState.SyncConnected){//客户端与服务端连接成功
            executeService();
        }
    }

    /**
     * 执行业务处理
     */
    private void executeService() {
        Stat stat=new Stat();//存储节点的版本、创建事件、id等信息
        //第一个参数要删除的节点路径
        //第二个参数版本信息  -1代表不验证版本
        //第三个参数异步回调
        //第四个参数操作传递上下文内容
        zooKeeper.delete("/myconfig/host",stat.getVersion(),new IDeleteCallBack(),"异步删除节点");
    }

    /**
     * 回调
     */
    class IDeleteCallBack implements AsyncCallback.VoidCallback{
        // rc是result code 服务端响应结果码。客户端可以从这个结果码中识别出API的调用结果，常见的结果码有：
        //      0（OK）,接口调用成功
        //      -4（ConnectionLoss），客户端和服务器连接断开
        //      -110（NodeExists） 节点已存在
        //      -112（SessionExpired）会话已过期
        //   path: 接口调用传入的数据节点的节点路径
        //   ctx: 异步调用的上下文
        public void processResult(int rc, String path, Object ctx) {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("rc=").append(rc).append("|");
            stringBuilder.append("path=").append(path).append("|");
            stringBuilder.append("ctx=").append(ctx);
            System.out.println("zk异步删除节点回调数据:"+stringBuilder.toString());
        }
    }
}
