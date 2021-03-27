package com.hongna.community.dao.elasticsearch;

import com.hongna.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {
}
