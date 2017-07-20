package io.github.xzhang1234.todolist;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by xiaoyun on 7/12/17.
 */

public class ListFinishedCursorRecyclerViewAdapter extends RecyclerView.Adapter<ListFinishedCursorRecyclerViewAdapter.TaskViewHolder> {
    private static final String TAG = "ListFinishedCursorRecyc";

    interface OnTaskClickListener {
        void onRestoreClick(Task task);
        void onDeleteClick(Task task);
    }

    private Cursor mCursor;
    private ListFinishedCursorRecyclerViewAdapter.OnTaskClickListener mListener;

    public ListFinishedCursorRecyclerViewAdapter(Cursor cursor, ListFinishedCursorRecyclerViewAdapter.OnTaskClickListener listener) {
        Log.d(TAG, "ListFinishedCursorRecyclerViewAdapter: Constructor called");
        mCursor = cursor;
        mListener = listener;
    }

    @Override
    public ListFinishedCursorRecyclerViewAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_items_finished, parent, false);
        return new ListFinishedCursorRecyclerViewAdapter.TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListFinishedCursorRecyclerViewAdapter.TaskViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: starts");

        if((mCursor == null) || (mCursor.getCount() == 0)) {
            Log.d(TAG, "onBindViewHolder: providing instructions");
            holder.name.setText(R.string.no_finished_task);
            holder.priority.setVisibility(View.GONE);
            holder.description.setVisibility(View.GONE);
            holder.restoreButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            if(!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn't move cursor to position " + position);
            }

            final Task task = new Task(mCursor.getLong(mCursor.getColumnIndex(TasksContract.Columns._ID)),
                    mCursor.getString(mCursor.getColumnIndex(TasksContract.Columns.TASKS_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(TasksContract.Columns.TASKS_DESCRIPTION)),
                    mCursor.getInt(mCursor.getColumnIndex(TasksContract.Columns.TASKS_SORTORDER)),
                    mCursor.getInt(mCursor.getColumnIndex(TasksContract.Columns.TASKS_STATUS)));

            holder.name.setText(task.getName());
            holder.priority.setVisibility(View.VISIBLE);
            holder.priority.setText(String.valueOf(task.getSortOrder()));
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(task.getDescription());
            holder.restoreButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);

            View.OnClickListener buttonListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch(view.getId()) {
                        case R.id.tli_restore_finished:
                            if(mListener != null) {
                                mListener.onRestoreClick(task);
                            }
                            break;
                        case R.id.tli_delete_finished:
                            if(mListener != null) {
                                mListener.onDeleteClick(task);
                            }
                            break;

                        default:
                            Log.d(TAG, "onClick: found unexpected button id");
                    }

                }
            };

            holder.restoreButton.setOnClickListener(buttonListener);
            holder.deleteButton.setOnClickListener(buttonListener);
        }

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: starts");
        if((mCursor == null) || (mCursor.getCount() == 0)) {
            return 1; // fib, because we populate a single ViewHolder with instructions
        } else {
            return mCursor.getCount();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.
     * The returned old Cursor is <em>not</em> closed.
     *
     * @param newCursor The new cursor to be used
     * @return Returns the previously set Cursor, or null if there wasn't one.
     * If the given new Cursor is the same instance as the previously set
     * Cursor, null is also returned.
     */
    Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }

        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if(newCursor != null) {
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            // notify the observers about the lack of a data set
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;

    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "TaskViewHolder";

        TextView name = null;
        TextView priority = null;
        TextView description = null;
        ImageButton restoreButton = null;
        ImageButton deleteButton = null;

        public TaskViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "TaskViewHolder: starts");

            this.name = (TextView) itemView.findViewById(R.id.tli_name_finished);
            this.priority = (TextView) itemView.findViewById(R.id.tli_priority_finished);
            this.description = (TextView) itemView.findViewById(R.id.tli_description_finished);
            this.restoreButton = (ImageButton) itemView.findViewById(R.id.tli_restore_finished);
            this.deleteButton = (ImageButton) itemView.findViewById(R.id.tli_delete_finished);
        }
    }
}


