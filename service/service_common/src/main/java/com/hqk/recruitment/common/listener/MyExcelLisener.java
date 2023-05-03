package com.hqk.recruitment.common.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.hqk.recruitment.common.entity.Dict;
import com.hqk.recruitment.common.mapper.DictMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class MyExcelLisener extends AnalysisEventListener<Dict> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<Dict> cachedDataList = new ArrayList<>(BATCH_COUNT);


    private  DictMapper dictMapper;

    public MyExcelLisener(DictMapper mapper) {
        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
        this.dictMapper=mapper;
    }


    /**
     * 这个每一条数据解析都会来调用
     *
     * @param dict    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(Dict dict, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(dict));
        cachedDataList.add(dict);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList =new ArrayList<>(BATCH_COUNT);
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
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        this.dictMapper.insertBatch(cachedDataList);
        log.info("存储数据库成功！");
    }

}
