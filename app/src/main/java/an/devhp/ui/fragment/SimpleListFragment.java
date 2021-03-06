package an.devhp.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.android.vlayout.VirtualLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

import an.devhp.R;
import an.devhp.manager.AppPref;
import an.devhp.manager.FragmentFactory;
import an.devhp.ui.adapter.SimpleFragmentAdapter;
import an.devhp.ui.listener.ListItemClickListener;
import an.devhp.util.AcUtil;
import an.devhp.util.LsUtil;
import an.devhp.util.ViewUtil;

/**
 * @description: 简单列表
 * @author: Kenny
 * @date: 2017-09-03 17:10
 * @version: 2.0
 */

public abstract class SimpleListFragment extends SimpleFragment {

    private RecyclerView mRecyclerView;

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = onCreateRecyclerView(inflater, container);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = ViewUtil.findById(getView(), R.id.recycler_view);
        if (mRecyclerView == null) {
            throw new IllegalStateException("Recycler view is null");
        }
        initRecyclerView(mRecyclerView);

        setFragmentLists();
    }

    protected void setFragmentLists() {
        if (addFragmentList()) {
            List<SimpleFragment> fragments = new ArrayList<>();
            initData(fragments);
            SimpleFragmentAdapter adapter = new SimpleFragmentAdapter(mActivity, fragments);
            initAdapter(adapter);
            setAdapter(adapter);
        }
    }

    protected boolean addFragmentList() {
        return true;
    }


    protected void initAdapter(final SimpleFragmentAdapter adapter) {
        adapter.setOnItemClickListener(new ListItemClickListener() {
            @Override
            public void onListItemClick(View view, int position) {
                onFragmentListItemClick(position, adapter);
            }
        });
        adapter.setOnItemLongClickListener(new ListItemClickListener() {
            @Override
            public void onListItemClick(View view, int position) {
                onFragmentListItemLongClicked(position, adapter);
            }
        });
    }

    protected void onFragmentListItemLongClicked(int position, SimpleFragmentAdapter adapter) {
        Object t = LsUtil.getLsElement(adapter.getData(), position);
        if (t instanceof SimpleFragment) {
            SimpleFragment sf = (SimpleFragment) t;
            if (AppPref.getInstance().putItemShowAtHome(sf.getSimpleFragmentId())) {
                Toast.makeText(mActivity, "Item add", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onFragmentListItemClick(int position, SimpleFragmentAdapter adapter) {
        if (adapter != null) {
            Object t = LsUtil.getLsElement(adapter.getData(), position);
            if (t instanceof SimpleFragment) {
                AcUtil.startSimpleActivity(mActivity, ((SimpleFragment) t).getSimpleFragmentId(), null);
            }
        }
    }

    protected View onCreateRecyclerView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.view_recycler, container, false);
    }

    protected void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    public void setAdapter(VirtualLayoutAdapter adapter) {
        synchronized (this) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        synchronized (this) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    protected void initData(List<SimpleFragment> fragments) {
        if (getFragmentIds() != null) {
            long[] fragmentIds = getFragmentIds();
            for (long id : fragmentIds) {
                LsUtil.add(fragments, FragmentFactory.create(id));
            }
        }
    }

    public long[] getFragmentIds() {
        return null;
    }

}
