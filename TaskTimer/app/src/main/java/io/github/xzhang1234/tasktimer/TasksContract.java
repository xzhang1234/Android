package io.github.xzhang1234.tasktimer;

import android.provider.BaseColumns;

/**
 * Created by xiaoyun on 7/9/17.
 */

public class TasksContract {
    static final String TABLE_NAME = "Tasks";

    // Tasks fields
    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String TASKS_NAME = "Name";
        public static final String TASKS_DESCRIPTION = "Description";
        public static final String TASKS_SORTORDER = "SortOrder";

        private Columns() {
            // private constructor to prevent instantiation
        }
    }
}
