package com.futurearts.hiltonnewproj.interfaces;

import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;

public interface CompletedListener {
    public void onCompleted(int position, MaterialIssueDetails materialIssue,String key);
}
