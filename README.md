# 首要开发

## 开发社区首页

封装帖子实体类，还有用户实体类，还有Page页面类，我们根据帖子查询，

# 开发2

## 开发注册功能

需要先开发验证码功能还有完成邮件工具类功能、

前期我们通过session存储验证码(后期通过redis改进系统性能)

验证了session跟前台的值是否匹配后，先查询数据库中是否已经存在当前用户名或者已经存在相同的邮箱等，如果符合条件，随机生成一个激活码注入到user中，然后则通过Context和templateEngine访问邮件页，并将其转换为String格式发送给邮箱。(此处需要使用到激活需求)

当我们使用邮件传来的网址访问时，我们请求会访问数据库，将我们当前的账户激活

## 开发登录功能（并给予登录凭证）

验证前台的验证码与session中的验证码是否相符，如若不符，返回错误消息。

验证用户的账号与密码，如若没有此账号则返回没有此账号，如若是密码出错，我们则显示，密码错误返回给前台。如若都正确，我们就跳转至首页，并且存储一个登录凭证給数据库。我们如果点击退出登录，我们在数据库中的登录凭证就更改状态为1，那我们输入都正确了，怎么知道我们是已经登录了呢（先把问题放在这里）

## 显示登录信息

因为我们在前端页面并不知道我们是否已经登录，所以我们需要导入拦截器显示已经登录的消息，在拦截器中查看当前浏览器是否有cookie，如果没有则不用在本次页面请求中加入user,如果有，则判断本次登录的凭证是否还存活，如果还存活，则将此cookie的user存储到model中，并在前端中展示。

## 账号设置

我们访问账号设置页，我们在controller中设置get方法，然后返回的是账号设置页。

设置上传头像功能，这个功能需要实现SpringMvc 的表格上传功能，multipartFile类型可以实现获得从前台传入的文件，我们将这个文件通过IO保存到服务器本地，当完成这个功能后，我们返回首页

我们还需要实现一个获取headUrl的get方法，我们需要实现获取这个头像的功能。



## 检查登录状态

我们在前面开发的账号设置，并不能让每个用户都登陆，如果没登录的话就不能登录，那我们该如何实现这个功能呢，通过注解与拦截器，我们实现拦截未登录用户。怎么实现的呢，如果登录了的话，我们在本次请求中hostHolder 保留有User，我们在拦截器中判断是否有User就可以判断是否已经登录。



# 帖子相关开发

## 前缀树

我们需要使用前缀树，然后我们用前缀树实现过滤敏感词，然后我们的帖子和评论等发布时都需要前缀树进行敏感词的过滤。

## 发布新帖子

前端判断用户是否已经登录，若无登录，发布帖子的功能按钮便不会展现在前端，因为我们不想一次刷新就把整个页面都刷新，所以我们导入了fastJson这个包，前端页面只用js代码(jquery代码)异步请求就可以实现帖子的发送，把前端的内容和标题发送给controller,在controller调用service，存入数据库。

## 展现帖子详情

查询帖子详情，查询帖子详情，selectDiscussPostById,在前端中帖子列表中封装帖子详情帖子id，controller中get方法中携带前台传来的id，将帖子返回给controller,返回帖子详情。

## 显示评论

service中封装查询评论或回复的方法，在帖子详情时，通过entityID（帖子的id）统一查询。

同一封装了帖子和回复，

遍历帖子，并查询帖子中是否有回复，如果有回复的话则添加一个map封装map，回复某个人的状态也很容易，只是在判断时添加了一个回复，

## 添加评论

分状态讨论、分为帖子回复还有回复评论还有回复某个人



## 私信

### 私信之展示列表

封装Message表，

- conversasion_id:表明通信的双方id拼接，规定小的id在前大的在后
  ![image](https://cdn.nlark.com/yuque/0/2021/png/10362390/1615608769078-e0e613ef-4bcb-41a6-a94f-6f9aba15e815.png)

私信展示列表就是展示最近的一条的消息而已，所以我们通过Userid查到当前用户的会话列表，通过mapper返回当前用户的对话列表，mapper可以通过UserID还有Page类的相关属性获取当前页的会话列表。

还要实现未读私信功能，这个也不难，selectLetterUnreadCount 查询status为0的私信就可以实现需求

### 增加私信

增加私信功能就是往表中添加数据，我们使用String拼装conversation_id，然后在sql中插入相关数据，默认的status为0(未读状态)



### 读私信自动减数量

当我们点击私信详情时，我们会在Mapper中检查传入的最新几条数据，是否为未读消息，如果是的话，我们就将其标记为是已读，然后返回给controller

## 统一处理异常

### SpringBoot自动处理方式

1. 把报错的错误码作为页面名放到如下目录下，当报出来相关错误时会自动显示报错的页面

![image](https://cdn.nlark.com/yuque/0/2021/png/10362390/1615608769106-d92a5670-d13d-4790-9dd5-2b020076a8a1.png)

### @ControllerAdvice和@ExceptionHandler处理异常

1.写一个跳转到处理页面的controller，这里在HomeController里面写

2.在controller包下新建advice包并创建处理异常类



### 统一记录日志

我们想要对所有service的访问记录，可以使用什么方法呢，SpringAop提供一种思路，我们可以使用切面思想。

**AOP的概念**

- Aspect Oriented Programing，即面向方面（切面）编程。
- AOP是一种编程思想，是对OOP的补充，可以进一步提高编程的效率。

通过Spring Aop我们实现了切面编程
@Component

@Aspect

# Redis使用

## 点赞功能

Redis有五种类型：String、Hash、List、Set、Sorted Set

通过Redisutil类我们可以实现点赞功能、（like:entity:entityType:entityId 由方法参数传来的参数组成的key值）

由前台传来的entity和entityType我们利用Redisutil工具类，我们实现封装Key值，再利用value值我们封装一个UserId值，完全实现redis数值的存储(实现了存储功能)

key:like:entity:entityType:entityId  value:userId

## 我收到的赞

重构点赞功能

重构点赞功能，我们因为在上个功能记录entityId还有entityType

key:like:user+entitiId value:数量

like:user+实体id

## 关注、取消关注

private static final String PREFIX_FOLLOWEE = "followee";

private static final String PREFIX_FOLLOWER = "follower";

followee:userId:entityType->zset (entityId,now)

关注，当前的用户,关注的类型->zset(关注实体的id，关注的时间)

follower:entityType:entityId -> zset(userId, now)

粉丝，当前实体类型,当前实体的id ->zset(粉丝id,被关注时间)

我们在页面点击关注按钮时，页面传来entityId,entityType,还有当前的userId,我们就可以通过redis进行关注和注册被关注

redisOperations.opsForZSet().add(followee,entityId,System.currentTimeMillis());

redisOperations.opsForZSet().add(follower,userId,System.currentTimeMillis());

/关注

getFolloweekey(int userid, int entityType){

return PREFIX_FOLLOWEE + SPLIT +userid+ SPLIT +entityType;

}

/粉丝

getFollowerKey(int entityType,int entityID){

return PREFIX_FOLLOWER + SPLIT +entityType + SpLIT + entityId

}

## 关注列表粉丝列表

根据zset存储的id值进行遍历



## 利用redis优化登录模块



![image.png](https://cdn.nlark.com/yuque/0/2021/png/10362390/1616547921092-fc42ef7b-da85-4179-9890-069ab12b7282.png)

![image.png](https://cdn.nlark.com/yuque/0/2021/png/10362390/1616549624050-99d75577-29f2-48d3-8c73-0ae980c557c7.png)

![image.png](https://cdn.nlark.com/yuque/0/2021/png/10362390/1616858052432-db28f31b-052c-41a3-b3d3-c9cd77bd884a.png)

## 第5章 阻塞队列(kafka)

在这一章节中，我们使用队列来实现点赞、关注、评论后系统自动发通知

我们首先创建消息生产者还有消息消费者，还有事件对象，生产者在生产消息的时候，给消息绑定事件对象的主题，我们发送消息的同时，消息消费者也绑定这个事件主题，当我们发送消息的时候，消息消费者同时也接受到这个消息，进行消费。![image](https://cdn.nlark.com/yuque/0/2021/png/10362390/1616496306779-9c925127-20b9-4ac1-a52a-a8c7ca79de2d.png)

### 发送系统通知

 我们通过消息队列，其实原理很简单，我们通过主题，把对应的事件对象（把message转变为json格式的String数据）封装到消息队列中，消费者对象从消息队列中绑定主题，得到传来的消息(对象)转变为event对象，通过event对象，我们通过message表，复用私信的功能，我们实现系统通知的功能。



comment：根据是回复帖子还是回复评论，我们判断其不同的entityType,我们可以实现根据其entityType来进行事件对象(event)的封装，实现对事件的发送.

like：点赞事件后面封装entityType,还有entityID,还有userId,如果我们想找点赞的帖子的话怎么办呢，我们需要额外添加postId，来实现点赞功能详情实现

follow：关注功能实现，entityId+entityType +userId(当前用户Id) 

全部封装到message表中,1为form_id entityId为to_id，这样我们就实现了基本的发送系统通知后台实现

### 显示系统通知

这个显示系统通知，我们就是通过message表来实现，我们通过entityType(conversation_id)来判断

我们封装最新的系统通知，还有最近一条的数据，根据page页查询系统通知的Mapper功能，还有未读通知功能的功能

我们因为想在首页上面展示我们的未读消息内容，所以我们需要拦截器载入messageService,载入未读消息，给首页header注入信息。





## 第6章 Elasticsearch

我们这章通过Elasticsearch实现对帖子的搜索，我们需要配置Elasticsearch，我们可以把Elasticsearch想象成是一个功能强大的数据库

术语的解释

- 索引：相当于数据库中的database  改版后作为table
- 类型：相当于数据库中的table     不再使用
- 文档：相当于数据库中的一行数据，数据结构为JSON
- 字段：相当于数据库中的一列

 discussRepository，底层封装的是elasticTemplate，由于discussRepository部分功能缺失，我们需要使用elasticTemplate来完成具体需求



我们通过NativeSearchQueryBuilder()来实现搜索条件，因为Spring帮我们封装了Elasticsearch工具类，所以使用起来并不算困难。
