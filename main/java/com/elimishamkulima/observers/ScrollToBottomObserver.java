package com.elimishamkulima.observers;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elimishamkulima.adapter.ChatsRecyclerAdapter;


public class ScrollToBottomObserver extends RecyclerView.AdapterDataObserver {

    RecyclerView recyclerView;
    ChatsRecyclerAdapter adapter;
    LinearLayoutManager manager;

    public ScrollToBottomObserver(RecyclerView recyclerView, ChatsRecyclerAdapter adapter, LinearLayoutManager manager) {
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.manager = manager;
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);

        int count = adapter.getItemCount();
        int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();
        boolean loading = lastVisiblePosition == -1;
        boolean atBottom = positionStart >= count - 1 && lastVisiblePosition == positionStart - 1;

        if (loading || atBottom) {
            recyclerView.scrollToPosition(positionStart);
        }
    }
}
