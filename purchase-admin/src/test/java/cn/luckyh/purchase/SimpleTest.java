package cn.luckyh.purchase;

import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import cn.luckyh.purchase.workflow.vo.runtime.TaskCompleteRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/19 14:18
 */
public class SimpleTest {

    @Test
    void testBeans() {
        TaskCompleteRepresentation completeRepresentation = new TaskCompleteRepresentation();
        completeRepresentation.setAssignee("admin");
        completeRepresentation.setOpinion(0);
        completeRepresentation.setComment("测bean转map");

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap map = objectMapper.convertValue(completeRepresentation, HashMap.class);
        System.out.println(map);
    }

    @Test
    void testListMapToMap() {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("key" + i, "value" + i);
            mapList.add(map);
        }


        //foreach
        Map<String, String> result = new HashMap<>();
        mapList.forEach(map -> {
            String key = String.valueOf(map.get("STATE"));
            String value = String.valueOf(map.get("COUNT"));
            result.put(key, value);
        });

        //stream.
        Map<String, String> collect = mapList.stream().map(Map::entrySet).flatMap(Set::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println(collect);
    }

    @Test
    void representation() {
        CreateProcessInstanceRepresentation representation = new CreateProcessInstanceRepresentation();
        Assert.notNull(representation.getVariables());
    }

    @Test
    void containsAny() {
    }
}
