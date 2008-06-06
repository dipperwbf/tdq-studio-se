// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.algorithms;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author scorreia
 * 
 * Utility class.
 */
public final class AlgoUtils {

    private AlgoUtils() {
    }

    /**
     * Method "incrementValueCounts". Stores in map the object as key, and counts how many times it appears.
     * 
     * @param <T> the type of the key
     * @param value the value to store
     * @param valueToCount the map [object -> number of objects]
     * @return true if ok
     */
    public static <T> boolean incrementValueCounts(T value, final Map<T, Long> valueToCount) {
        Long freq = valueToCount.get(value);
        if (freq == null) {
            freq = 0L;
        }
        freq++;
        valueToCount.put(value, freq);
        return freq > 0;
    }

    /**
     * DOC scorreia Comment method "getMedian".
     * 
     * @param totalCount
     * @param valueToCount
     * @return
     */
    public static double getMedian(long totalCount, final TreeMap<Object, Long> valueToCount) {
        Set<Object> keys = valueToCount.keySet();
        Collection<Long> counts = valueToCount.values();

        double kthValue = ((double) totalCount) / 2;

        double localMedian = 0;

        long sumCount = 0;
        Iterator<Object> keyIterator = keys.iterator();
        Number searchedKey = null;

        for (Long curValue : counts) {
            if (sumCount >= kthValue) {
                // compute median
                if (totalCount % 2 != 0) { // odd number of value, take the middle
                    localMedian = searchedKey.doubleValue();
                } else { // even number of values
                    localMedian = searchedKey.doubleValue();
                    if (sumCount - kthValue < 1) { // in case there are not many identical values
                        Number nextKey = (Number) keyIterator.next(); // CAST here
                        localMedian = (localMedian + nextKey.doubleValue()) / 2;
                    }
                }
                break; // we got it.
            }
            // else loop
            searchedKey = (Number) keyIterator.next(); // CAST here
            sumCount += curValue;
        }
        return localMedian;
    }

}
