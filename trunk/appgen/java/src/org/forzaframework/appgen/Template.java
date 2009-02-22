/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.appgen;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Cesar Reyes
 * Date: 28/12/2006
 * Time: 04:49:41 PM
 * Description:
 */
public class Template {

    private String path;
    private String fileName;
    private String destinationPath;
    private boolean willMerge;
    private String mergeReplaceText;
    private String templateStart = "<!--${class.name}-BEAN-START-->";
    private String templateEnd = "<!--${class.name}-BEAN-END-->";
    private String templateStartReplaceText = "REGULAR-START";
    private String templateEndReplaceText = "REGULAR-END";
    private String mergeDeleteText = "REGULAR-START(?s:.)*REGULAR-END";
    private TemplateType type;
    private List<LayoutStyle> layoutStyles = new ArrayList<LayoutStyle>();

    public Template(String path, String fielName, String destinationPath, boolean willMerge, TemplateType templateType) {
        this.path = path;
        this.fileName = fielName;
        this.destinationPath = destinationPath;
        this.willMerge = willMerge;
        this.type = templateType;
    }

    public Template(String path, String fileName, String destinationPath, boolean willMerge, String mergeReplaceText, TemplateType templateType) {
        this.path = path;
        this.fileName = fileName;
        this.destinationPath = destinationPath;
        this.willMerge = willMerge;
        this.mergeReplaceText = mergeReplaceText;
        this.type = templateType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }

    public boolean willMerge() {
        return willMerge;
    }

    public void setWillMerge(boolean willMerge) {
        this.willMerge = willMerge;
    }

    public String getMergeReplaceText() {
        return mergeReplaceText;
    }

    public void setMergeReplaceText(String mergeReplaceText) {
        this.mergeReplaceText = mergeReplaceText;
    }

    public String getTemplateStart() {
        return templateStart;
    }

    public void setTemplateStart(String templateStart) {
        this.templateStart = templateStart;
    }

    public String getTemplateEnd() {
        return templateEnd;
    }

    public void setTemplateEnd(String templateEnd) {
        this.templateEnd = templateEnd;
    }

    public String getTemplateStartReplaceText() {
        return templateStartReplaceText;
    }

    public void setTemplateStartReplaceText(String templateStartReplaceText) {
        this.templateStartReplaceText = templateStartReplaceText;
    }

    public String getTemplateEndReplaceText() {
        return templateEndReplaceText;
    }

    public void setTemplateEndReplaceText(String templateEndReplaceText) {
        this.templateEndReplaceText = templateEndReplaceText;
    }

    public String getMergeDeleteText() {
        return mergeDeleteText;
    }

    public void setMergeDeleteText(String mergeDeleteText) {
        this.mergeDeleteText = mergeDeleteText;
    }

    public TemplateType getType() {
        return type;
    }

    public void setType(TemplateType type) {
        this.type = type;
    }

    public List<LayoutStyle> getLayoutStyles() {
        return layoutStyles;
    }

    public void setLayoutStyles(List<LayoutStyle> layoutStyles) {
        this.layoutStyles = layoutStyles;
    }

    public void addLayoutStyle(LayoutStyle layoutStyle) {
        this.layoutStyles.add(layoutStyle);
    }

    public boolean supports(LayoutStyle layoutStyleToCompare) {
        boolean supports = false;
        for (LayoutStyle layoutStyle : getLayoutStyles()) {
            if (layoutStyle.equals(layoutStyleToCompare))
                supports = true;
        }
        return supports;
    }
}
