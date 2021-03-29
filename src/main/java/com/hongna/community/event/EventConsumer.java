package com.hongna.community.event;

import com.alibaba.fastjson.JSONObject;
import com.hongna.community.entity.DiscussPost;
import com.hongna.community.entity.Event;
import com.hongna.community.entity.Message;
import com.hongna.community.service.DiscussPostService;
import com.hongna.community.service.ElasticsearchService;
import com.hongna.community.service.MessageService;
import com.hongna.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    //一个方法可以消费多个主题，一个整体可以被多个消费者消费
    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleCommentMessage(ConsumerRecord record){
        if(record == null || record.value() == null){
            logger.error("消息的内容为空");
            return;
        }
        //将json解析为字符串
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if(event == null){
            logger.error("消息格式有误!");
            return;
        }
        //发送站内通知
        //构造一个Message对象插入数据库，由系统发送，所以fromId为1
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        //事件的触发者
        message.setToId(event.getEntityUserId());
        //会话直接设置为topic
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        //用内容去拼接实际显示的通知 比如页面显示的 用户nowcoder(事件的触发者)评论了你的帖子(实体的数据，因为之后在前台需要链接过去)
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId",event.getEntityId());
        //表示可能还存在一些额外的值
        if(!event.getData().isEmpty()){
            for(Map.Entry<String, Object> entry : event.getData().entrySet()){
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        //存储至数据表中
        messageService.addMessage(message);
    }

    // 消费发帖事件
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.saveDiscussPost(post);
    }
}
