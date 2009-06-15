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

package org.forzaframework.util;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author cesarreyes
 *         Date: 08-sep-2008
 *         Time: 18:37:29
 */
public class RegexUtils {

    public static List<String> matchesToList(String pattern, String input){
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(input);

        List<String> results = new ArrayList<String>();
        while(matcher.find()){
            results.add(input.substring(matcher.start(), matcher.end()));
        }
        return results;
    }
}
