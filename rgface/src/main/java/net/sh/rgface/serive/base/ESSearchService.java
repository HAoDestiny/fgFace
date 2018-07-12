package net.sh.rgface.serive.base;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by DESTINY on 2018/6/7.
 */

@Service
public class ESSearchService<T> {

    @Autowired
    private JestClient jestClient;

    //添加记录到ES
    public void saveEntity(T entity, String searchName, String searchType) {

        Index index = new Index.Builder(entity).index(searchName).type(searchType).build();

        try {
            jestClient.execute(index);

            System.out.println("--------------------" + searchName + ", 插入完成" );
        } catch (IOException e) {
            e.printStackTrace();

            System.out.println("--------------------" + searchName + ", 插入失败 ERROR message: "  + e.getMessage());

        }

    }

}
