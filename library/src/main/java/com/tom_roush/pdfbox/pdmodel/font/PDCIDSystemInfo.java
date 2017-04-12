/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tom_roush.pdfbox.pdmodel.font;

import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.common.COSObjectable;

/**
 * Represents a CIDSystemInfo.
 *
 * @author John Hewson
 */
public final class PDCIDSystemInfo implements COSObjectable
{
    private final COSDictionary dictionary;

    PDCIDSystemInfo(COSDictionary dictionary)
    {
        this.dictionary = dictionary;
    }

    public String getRegistry()
    {
        return dictionary.getNameAsString(COSName.REGISTRY);
    }

    public String getOrdering()
    {
        return dictionary.getNameAsString(COSName.ORDERING);
    }

    public int getSupplement()
    {
        return dictionary.getInt(COSName.SUPPLEMENT);
    }

    @Override
    public COSBase getCOSObject()
    {
        return dictionary;
    }
}
