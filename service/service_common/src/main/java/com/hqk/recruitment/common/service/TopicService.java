package com.hqk.recruitment.common.service;

import com.hqk.recruitment.model.common.Topic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.base.PageVo;
import com.hqk.recruitment.vo.common.TopicUpdateVo;
import com.hqk.recruitment.vo.common.TopicVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-03-03
 */
public interface TopicService extends IService<Topic> {


    R deleteTopById(Long id);

    boolean updateTopic(TopicUpdateVo topicUpdateVo);

    R listByPage(PageVo pageVo);

    R getRandomList();


    R getTopicDetailById(Long id);

    R addTopic(String content);
}
