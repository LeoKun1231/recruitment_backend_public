package com.hqk.recruitment.user.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.hqk.recruitment.model.user.User;
import com.hqk.recruitment.user.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyExcelListener extends AnalysisEventListener<User> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<User> cachedDataList = new ArrayList<>(BATCH_COUNT);


    private UserService userService;
    private Long majorId;

    public MyExcelListener(UserService userService,Long majorId) {
        this.userService=userService;
        this.majorId=majorId;
    }


    /**
     * 这个每一条数据解析都会来调用
     *
     * @param user    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(User user, AnalysisContext context) {
        log.warn("解析到一条数据:{}", JSON.toJSONString(user));
        if("{}".equals(JSON.toJSONString(user))){
        }else{
            cachedDataList.add(user);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (cachedDataList.size() >= BATCH_COUNT) {
                saveData();
                // 存储完成清理 list
                cachedDataList =new ArrayList<>(BATCH_COUNT);
            }
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.warn("{}条数据，开始存储数据库！", cachedDataList.size());
        this.userService.MySaveBacth(cachedDataList,majorId);
        log.warn("存储数据库成功！");
    }

}
