# online_blackboard
一个可以多人合作编辑和共享的在线黑板

# 系统架构

![sysem](https://user-images.githubusercontent.com/42209673/201069194-eedebda2-0e1a-4cc8-b95a-5ed0ba51866e.png)

# 储存架构

![DB](https://user-images.githubusercontent.com/42209673/201359220-21659e9c-04a8-45dc-b7b2-c3781f02f8c3.png)


``
    采用websocket通信实现服务器主动推送消息到客户端，当用户操做白板时会实时发送消息同步到参会人员的同一白板上,并采用ribbitmq用作消息代理，就算消息发送到不同的服务器上也可以同步到
各个客户端上
``
