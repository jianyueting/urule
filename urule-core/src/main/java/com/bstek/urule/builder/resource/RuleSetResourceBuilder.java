/*******************************************************************************
 * Copyright 2017 Bstek
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.urule.builder.resource;

import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.parse.deserializer.RuleSetDeserializer;
import org.dom4j.Element;

/**
 * @author Jacky.gao
 * @since 2014年12月22日
 */
public class RuleSetResourceBuilder implements ResourceBuilder<RuleSet> {
    private RuleSetDeserializer ruleSetDeserializer;

    @Override
    public RuleSet build(Element root) {
        return ruleSetDeserializer.deserialize(root);
    }

    @Override
    public boolean support(Element root) {
        return ruleSetDeserializer.support(root);
    }

    @Override
    public ResourceType getType() {
        return ResourceType.RuleSet;
    }

    public void setRuleSetDeserializer(RuleSetDeserializer ruleSetDeserializer) {
        this.ruleSetDeserializer = ruleSetDeserializer;
    }
}
