# 第一开发

## 开发社区首页

封装帖子实体类，还有用户实体类，还有Page页面类，我们根据帖子查询，

# 第二开发

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



# 内容相关开发

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

当我们点击私信详情时，我们会在Mapper中检查传入的最新几条数据，是否为未读消息，如果是的话，我们

## 统一处理异常
