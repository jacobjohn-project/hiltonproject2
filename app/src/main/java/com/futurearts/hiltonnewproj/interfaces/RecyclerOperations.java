package com.futurearts.hiltonnewproj.interfaces;

import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;

public interface RecyclerOperations {
    public void onCompleted(int position, MaterialIssueDetails materialIssue, String key);
    public void onBarCodeScanClicked(int position);
}
