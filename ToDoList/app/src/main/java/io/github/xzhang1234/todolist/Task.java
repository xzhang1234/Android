package io.github.xzhang1234.todolist;

import java.io.Serializable;

/**
 * Created by xiaoyun on 7/10/17.
 */

class Task implements Serializable {
    public static final long serialVersionUID = 20161120L;

    private long m_Id;
    private final String mName;
    private final String mDescription;
    private final int mSortOrder;
    private int mStatus;

    public Task(long id, String name, String description, int sortOrder, int status) {
        this.m_Id = id;
        mName = name;
        mDescription = description;
        mSortOrder = sortOrder;
        mStatus = status;
    }

    public long getId() {
        return m_Id;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getSortOrder() {
        return mSortOrder;
    }

    public void setId(long id) {
        this.m_Id = id;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public int getStatus() {
        return this.mStatus;
    }

    @Override
    public String toString() {
        return "Task{" +
                "m_Id=" + m_Id +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mSortOrder=" + mSortOrder + '\'' +
                ", mStatus=" + mStatus +
                '}';
    }
}

