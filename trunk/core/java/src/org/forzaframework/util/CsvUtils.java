/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.util;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;
import org.apache.commons.io.FileUtils;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;

/**
 * @author cesarreyes
 *         Date: 08-sep-2008
 *         Time: 18:20:14
 */
public class CsvUtils {

    public static List<String> getLines(String path){
        List<String> lines = null;
        try {
            lines = FileUtils.readLines(new File(path));
        } catch (IOException e) {
            //TODO: do something
        }
        if(lines.size() < 2){
            //TODO: Check
            throw new RuntimeException("Invalid file");
        }

        return lines;
    }

    public static List<String> getColumns(String header, String delimiter){
        String[] headerTokens = header.split(delimiter, -1);
        List<String> columns = new ArrayList<String>();
        for(String token : headerTokens){
            if(token.startsWith("\"") && token.endsWith("\"")) {
                columns.add(token.substring(1, token.length() - 2));
            } else {
                columns.add(token);
            }
        }

        return columns;
    }

    public static List<String> getTokens(String line, String delimiter) {
        List<String> columns = new ArrayList<String>();
        Matcher m = Pattern.compile("(?:^|" + delimiter + ")(\"(?:[^\"]|\"\")*\"|[^" + delimiter + "]*)").matcher(line);
        while (m.find()) {
            columns.add(m.group()
                    .replaceAll("^" + delimiter, "") // remove first comma if any
                    .replaceAll("^?\"(.*)\"$", "$1") // remove outer quotations if any
                    .replaceAll("\"\"", "\"")); // replace double inner quotations if any
        }
        return columns;
    }

    public static String addColumn(String line, String value, String separator){
        value = value.replaceAll("\"", "\"\"");
        if("".equals(line)){
            return "\"" + value + "\"";
        }else{
            return separator + "\"" + value + "\"";
        }
    }
}
