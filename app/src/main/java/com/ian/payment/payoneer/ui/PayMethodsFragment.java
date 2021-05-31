package com.ian.payment.payoneer.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.ian.payment.payoneer.adapters.PayMethodsAdapter;
import com.ian.payment.payoneer.databinding.HomefragmentBinding;
import com.ian.payment.payoneer.model.Applicable;
import com.ian.payment.payoneer.utils.Utility;
import com.ian.payment.payoneer.viewmodel.PaymentMethodsViewModel;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;


@AndroidEntryPoint
public class PayMethodsFragment extends Fragment {
    private static final String TAG = "Home";
    private HomefragmentBinding binding;
    private PaymentMethodsViewModel viewModel;
    private PayMethodsAdapter adapter;
    private List<Applicable> Paymethodlist;
    private Snackbar errorSnackbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomefragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PaymentMethodsViewModel.class);
        errorSnackbar = Snackbar.make(binding.relayout, "", Snackbar.LENGTH_INDEFINITE);

        initRecyclerView();
        observeData();

        loadPaymentList();
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPaymentList();
            }
        });
        errorSnackbar.setAction("TRY AGAIN",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPaymentList();
            }
        });
    }

    private void loadPaymentList() {
        if (Utility.checkInternetConnection(requireContext())) {
            viewModel.getPayMethods();
        } else {
            handleNoConnection();
        }

    }


    private void observeData() {

        viewModel.getListResultState().observe(getViewLifecycleOwner(), listResultViewState -> {
            switch (listResultViewState.getCurrentState()) {

                //loading state
                case 0:
                    if (!binding.swipeRefreshLayout.isRefreshing()) {
                        binding.layoutStates.setVisibility(View.VISIBLE);
                        binding.animationView.setVisibility(View.GONE);

                    }
                    break;
                //Succcess State
                case 1:
                    binding.swipeRefreshLayout.setRefreshing(false);

                    binding.layoutStates.setVisibility(View.GONE);
                    binding.animationView.setVisibility(View.GONE);
                    binding.animationView.cancelAnimation();
                    //progressDialog.dismiss()
                    errorSnackbar.dismiss();
                    Paymethodlist = listResultViewState.getData().getNetworks().getApplicable();

                    adapter.updateList(Paymethodlist);
                    break;
                //Error State
                case -1: // show error
                    binding.swipeRefreshLayout.setRefreshing(false);

                    binding.layoutStates.setVisibility(View.GONE);
                    binding.animationView.setVisibility(View.VISIBLE);
                    binding.animationView.loop(true);
                    binding.animationView.setAnimation("call_failed_animation.json");
                    binding.animationView.playAnimation();
                    errorSnackbar.setText("There has been an error fetching this data");
                    if (Paymethodlist != null) {
                        Paymethodlist.clear();
                    }


                    adapter.notifyDataSetChanged();
                    errorSnackbar.show();

                    break;
                    //EMPTY LIST STATE
                case 2:
                    binding.swipeRefreshLayout.setRefreshing(false);

                    binding.layoutStates.setVisibility(View.GONE);
                    binding.animationView.setVisibility(View.VISIBLE);
                    binding.animationView.loop(true);
                    binding.animationView.setAnimation("empty_animation.json");
                    binding.animationView.playAnimation();
                    errorSnackbar.setText("Currently,there are no available payment methods");
                    if (Paymethodlist != null) {
                        Paymethodlist.clear();
                    }


                    adapter.notifyDataSetChanged();
                    errorSnackbar.show();

                    break;
            }
        });

    }

    private void initRecyclerView() {
        binding.recyclerv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PayMethodsAdapter(getContext(), Paymethodlist);
        binding.recyclerv.setAdapter(adapter);
    }

    private void handleNoConnection() {

        binding.swipeRefreshLayout.setRefreshing(false);
        //progressDialog.dismiss()
        binding.layoutStates.setVisibility(View.GONE);
        binding.animationView.setVisibility(View.VISIBLE);
        binding.animationView.loop(true);
        binding.animationView.setAnimation("no_internet_connection_animation.json");
        binding.animationView.playAnimation();
        errorSnackbar.setText("No Internet Connection");
        if (Paymethodlist != null) {
            Paymethodlist.clear();
        }

        adapter.notifyDataSetChanged();
        errorSnackbar.show();
    }
}



