package ch.hsr.sa.radiotour.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.MaillotPresenter;
import ch.hsr.sa.radiotour.controller.adapter.MaillotsAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import io.realm.RealmList;

public class MaillotsFragment extends Fragment {
    private RecyclerView rvMaillots;
    private RealmList<Maillot> maillots;
    private MaillotsAdapter adapter;
    private Context mContext;
    private TextView emptyTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "MaillotsFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_maillots, container, false);
        this.maillots = new RealmList<>();
        this.adapter = new MaillotsAdapter(maillots, mContext);
        emptyTextView = root.findViewById(R.id.emptyView);
        initComponents(root);
        return root;
    }

    public void initComponents(View root) {
        MaillotPresenter.getInstance().addView(this);
        rvMaillots = root.findViewById(R.id.rvMaillot);
        rvMaillots.setAdapter(adapter);
        initRecyclerListener();
    }

    private void initRecyclerListener() {
        rvMaillots.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void showMaillots(RealmList<Maillot> maillotRealmList) {
        if(maillotRealmList.isEmpty()){
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
        }
        this.maillots.clear();
        this.maillots.addAll(maillotRealmList);
        rvMaillots.swapAdapter(new MaillotsAdapter(maillots, mContext), true);
        rvMaillots.scrollBy(0, 0);
        this.adapter.notifyDataSetChanged();
        Log.d("AFTER", "afternotifyDataSetChanged");
    }

    @Override
    public void onStart() {
        super.onStart();
        MaillotPresenter.getInstance().getAllMaillots();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }
}
