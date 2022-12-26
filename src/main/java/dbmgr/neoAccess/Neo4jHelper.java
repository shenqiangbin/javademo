package dbmgr.neoAccess;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.neo4j.driver.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Neo4jHelper {
    private Driver neo4jDriver;

    public static final String STR_NODE = "NODE";
    public static final String STR_RELATIONSHIP = "RELATIONSHIP";

    public Neo4jHelper(Driver driver) {
        neo4jDriver = driver;
    }

    /**
     * neo4j驱动执行cypher
     *
     * @param cypherSql
     * @return
     */
    public Result excuteCypherSql(String cypherSql) {
        Session session = neo4jDriver.session();

        try (Transaction tx = session.beginTransaction()) {
            Result result = tx.run(cypherSql);
            tx.commit();
            return result;
        } finally {
            session.close();
        }

//        注意注意：不要使用这个，sql 有问题，也不会报错
//        StatementResult result = session.run(cypherSql);
//        return result;

    }

    /**
     * 返回查询结果
     *
     * @param cypherSql
     * @return
     */
    public List<Map<String, Object>> GetValue(String cypherSql) {
        List<Map<String, Object>> ents = new ArrayList<>();
        Session session = neo4jDriver.session();

        try (Transaction tx = session.beginTransaction()) {
            Result result = tx.run(cypherSql);
            //tx.();
            if (result.hasNext()) {
                List<Record> records = result.list();
                for (Record recordItem : records) {
                    List<Pair<String, Value>> f = recordItem.fields();
                    for (Pair<String, Value> pair : f) {
                        Map<String, Object> map = pairToMap(pair);
                        ents.add(map);
                    }
                }
            }
        } finally {
            session.close();
        }
        return ents;
    }

    public Map<String, Object> pairToMap(Pair<String, Value> pair) {
        Value value = pair.value();
        String nodeType = value.type().name();

        if (nodeType.equalsIgnoreCase(STR_NODE)) {
            Node node = value.asNode();

            Map<String, Object> cloneMap = new LinkedHashMap<>();
            cloneMap.put("uuid", node.id());
            cloneMap.put("sysLabels",node.labels());
            cloneMap.put("neoType", STR_NODE);
            Map<String, Object> asMap = node.asMap();
            cloneMap.putAll(asMap);

            return cloneMap;
        } else if (nodeType.equalsIgnoreCase(STR_RELATIONSHIP)) {
            Relationship relationship = value.asRelationship();

            Map<String, Object> cloneMap = new LinkedHashMap<>();
            cloneMap.put("uuid", relationship.id());
            cloneMap.put("neoType", STR_RELATIONSHIP);
            cloneMap.put("sys_sourceid", relationship.startNodeId());
            cloneMap.put("sys_targetid", relationship.endNodeId());

            Map<String, Object> asMap = relationship.asMap();
            cloneMap.putAll(asMap);

            return cloneMap;
        } else {
            //System.out.println("not handle this type:" + nodeType);
            return null;
        }
    }

    private String getFilterClause(List<String> filter) {
        if (filter == null || filter.size() == 0) {
            return "''";
        } else {
            String filterClause = "[";
            for (int i = 0; i < filter.size(); i++) {
                if (i > 0) {
                    filterClause += ",";
                }
                filterClause += "'";
                filterClause += filter.get(i);
                filterClause += "'";
            }
            filterClause += "]";
            return filterClause;
        }
    }

}
