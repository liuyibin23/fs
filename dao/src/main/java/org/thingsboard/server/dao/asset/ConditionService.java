package org.thingsboard.server.dao.asset;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@ConditionalOnExpression("'${condition.isload:null}'=='null' || '${condition.isload}'=='true'")
@Slf4j
public class ConditionService {

    @Value("${condition.msg}")
    private String msg;

    @PostConstruct
    public void init(){
        log.info("ConditionService init,{}",msg);
    }

    @PreDestroy
    public void destroy(){
        log.info("ConditionService destroy");
    }

}
