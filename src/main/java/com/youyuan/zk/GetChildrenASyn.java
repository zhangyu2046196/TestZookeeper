package com.youyuan.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

/**
 * 异步获取子节点信息
 * @author zhangyu
 * @date 2018/9/6 16:41
 */
public class GetChildrenASyn implements Watcher {

    private static ZooKeeper zooKeeper=null;

    public static void main(String[] args) {
        try {
            zooKeeper=new ZooKeeper("172.18.32.16:12181,172.18.32.21:12181,172.18.32.25:12181",5000,new GetChildrenASyn());
            Thread.sleep(Integer.MAX_VALUE);
            System.out.println("客户端与服务端建立连接");
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
        if (watchedEvent.getState()==Event.KeeperState.SyncConnected){//客户端连接服务端状态
            executeService();
        }
    }

    /**
     *  处理业务逻辑
     */
    private void executeService() {
        zooKeeper.getChildren("/myconfig",true,new IChildrenCallBack(),"异步获取子节点信息");
    }

    /**
     * 回调
     */
    class IChildrenCallBack implements AsyncCallback.ChildrenCallback{

        // rc是result code 服务端响应结果码。客户端可以从这个结果码中识别出API的调用结果，常见的结果码有：
        //      0（OK）,接口调用成功
        //      -4（ConnectionLoss），客户端和服务器连接断开
        //      -110（NodeExists） 节点已存在
        //      -112（SessionExpired）会话已过期
        //   path: 接口调用传入的数据节点的节点路径
        //   ctx: 异步调用的上下文
        //   bytes: 查询出的节点内容
        //   stat: 节点信息 包含版本，id，创建事件等
        public void processResult(int rc, String path, Object ctx, List<String> list) {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("rc=").append(rc).append("|");
            stringBuilder.append("path=").append(path).append("|").append("\n");
            if (list!=null && list.size()>0){
                for (String children:list){
                    stringBuilder.append("子节点=").append(children).append("\n");
                }
            }
            System.out.println("获取子节点信息:"+stringBuilder.toString());
        }
    }
}
