package com.destiner.social_reader.model.source_manager;

import com.destiner.social_reader.model.structs.source.GroupSource;
import com.destiner.social_reader.model.cache.OnOffsetArticlesLoadListener;
import com.destiner.social_reader.model.structs.source.Source;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;

import org.joda.time.DateTime;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles all work with sources: loads posts through API, updates source states.
 */
public class SourceManager {
    private static Set<Source> sources = new HashSet<>();

    static {
        sources.add(new GroupSource(43879004));
        sources.add(new GroupSource(26740020));
    }

    private SourceManager() {
    }

    /**
     * Loads new (ones which wasn't get before) posts from group sources.
     * @param callback callback listener that should be fired when posts retrieval is complete.
     */
    public static void getGroupPosts(OnOffsetArticlesLoadListener callback) {
        Set<GroupSource> groupSources = getGroupSources();
        Set<Integer> groupSourceIds = getGroupSourceIds(groupSources);
        DateTime endTime = getEarliestPostDate(groupSources);
        DateTime startTime = new DateTime(0);
        requestPosts(groupSourceIds, startTime, endTime, callback);
    }

    /**
     * Returns group sources as set
     * @return group sources
     */
    private static Set<GroupSource> getGroupSources() {
        Set<GroupSource> groupSources = new HashSet<>();
        for (Source source : sources) {
            if (source instanceof GroupSource) {
                groupSources.add((GroupSource) source);
            }
        }
        return groupSources;
    }

    /**
     * Finds the earliest post date from given source set, i. e. the date after each there were no
     * posts loaded from given sources before
     * @param sources source list
     * @return earliest post date
     */
    private static DateTime getEarliestPostDate(Set<? extends Source> sources) {
        long earliestPostMillis = Integer.MAX_VALUE;
        for (Source source : sources) {
            long firstPostMillis = source.getFirstPostTime().getMillis();
            if (firstPostMillis < earliestPostMillis) {
                earliestPostMillis = firstPostMillis;
            }
        }
        return new DateTime(earliestPostMillis);
    }

    /**
     * Extracts group source ids to the set
     * @param groupSources group sources
     * @return set of ids
     */
    private static Set<Integer> getGroupSourceIds(Set<GroupSource> groupSources) {
        Set<Integer> ids = new HashSet<>();
        for (GroupSource source : groupSources) {
            ids.add(- source.getId());
        }
        return ids;
    }

    /**
     * Requests posts from API with given parameters
     * @param sourceIds set of source ids
     * @param startTime first time when posts could be created
     * @param endTime last time when posts could be created
     * @param callback callback listener that will fire when request will complete
     */
    private static void requestPosts(Set<Integer> sourceIds, DateTime startTime, DateTime endTime,
                              OnOffsetArticlesLoadListener callback) {
        String sourceIdsString = sourceIds.toString();
        VKParameters parameters = VKParameters.from(
                "filters", "post",
                "source_ids", sourceIdsString,
                "count", 100,
                "start_time", startTime.getMillis(),
                "end_time", endTime.getMillis()
                );
        VKRequest request = new VKRequest("newsfeed.get", parameters);
        request.executeWithListener(new SourceVKRequestListener(callback));
    }
}
