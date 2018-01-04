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
package com.bstek.urule.console.repository.model;

import com.bstek.urule.RuleException;

/**
 * @author Jacky.gao
 * @since 2014年12月24日
 */
public enum FileType {
    Ruleset("rs.xml"),
    DecisionTable("dt.xml"),
    ScriptDecisionTable("dts.xml"),
    ActionLibrary("al.xml"),
    VariableLibrary("vl.xml"),
    ParameterLibrary("pl.xml"),
    ConstantLibrary("cl.xml"),
    RuleFlow("rl.xml"),
    UL(".ul"),
    DecisionTree("dtree.xml"),
    Scorecard("sc"),
    DIR("DIR");

    private String extType;

    FileType(String extType) {
        this.extType = extType;
    }

    @Override
    public String toString() {
        return this.extType;
    }

    public static FileType parse(String type) {
        FileType[] fileTypes = FileType.values();

        for (FileType fileType : fileTypes) {
            if (fileType.extType.equals(type)) {
                return fileType;
            }
        }

        throw new RuleException("Unknow type:" + type);
    }
}
